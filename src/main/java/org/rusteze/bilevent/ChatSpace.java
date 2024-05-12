package org.rusteze.bilevent;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import com.mongodb.BasicDBList;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.Updates;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.bson.BsonArray;
import org.bson.Document;
import org.bson.conversions.Bson;

public class ChatSpace implements ConvertibleWithDocument<ChatSpace> {
    private ObservableList<Message> messages;
    private Event parent;

    public ChatSpace(Event parent) {
        this.messages = FXCollections.observableArrayList();
        this.parent = parent;
    }

    public void addMessage(String text, String username) {
        Message newMessage = new Message(text, username);
        messages.add(newMessage);

        //Database_Part begin
        Document newMessageDoc = newMessage.toDocument();
        Document query = new Document().append("_id", parent.getId());
        Bson update = Updates.addToSet("messages", newMessageDoc);
        UpdateOptions options = new UpdateOptions().upsert(true);
        HelloApplication.db.getCollection("Event").updateOne(query, update, options);
        //end
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
        ((ArrayList<Document>)doc.get("messages")).forEach(e -> {
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
