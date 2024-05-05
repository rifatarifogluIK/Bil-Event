package org.rusteze.bilevent;

import java.time.LocalDate;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ChatSpace {
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
}
