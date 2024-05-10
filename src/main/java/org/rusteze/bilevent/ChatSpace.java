package org.rusteze.bilevent;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.bson.BsonArray;
import org.bson.Document;

public class ChatSpace implements ConvertibleWithDocument<ChatSpace> {
    private ObservableList<Message> messages;

    public ChatSpace() {
        this.messages = FXCollections.observableArrayList();
    }

    public void addMessage(String text, String username) {
        Message newMessage = new Message(text, username);
        messages.add(newMessage);
    }

    public ObservableList<Message> getMessages() {
        return messages;
    }

    @Override
    public Document toDocument(){
        Document doc = new Document();

        BsonArray messagesArr = new BsonArray();
        messages.forEach(e -> messagesArr.add(e.toDocument().toBsonDocument()));

        doc.append("messages", messagesArr);

        return doc;
    }
    @Override
    public ChatSpace fromDocument(Document doc) throws FileNotFoundException {
        ArrayList<Message> temp = new ArrayList<>();
        ((Document)doc.get("messages")).values().forEach(e -> {
            try {
                temp.add(new Message().fromDocument((Document)e));
            } catch (FileNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        });
        this.messages = FXCollections.observableArrayList(temp);

        return this;
    }
}
