package org.rusteze.bilevent;

import java.time.LocalDate;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.bson.Document;

public class ChatSpace {
    private ObservableList<Message> messages;

    public ChatSpace() {
        this.messages = FXCollections.observableArrayList();
    }

    public ChatSpace(Document doc){
        ArrayList<Message> temp = new ArrayList<>();
        ((Document)doc.get("messages")).values().forEach(e -> temp.add(new Message((Document)e)));
        this.messages = FXCollections.observableArrayList(temp);
    }

    public void addMessage(String text, String username) {
        Message newMessage = new Message(text, username);
        messages.add(newMessage);
    }

    public ObservableList<Message> getMessages() {
        return messages;
    }
}
