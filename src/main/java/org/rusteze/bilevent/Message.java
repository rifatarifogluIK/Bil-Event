package org.rusteze.bilevent;

public class Message {
    private String text;
    private String username;

    public Message(String text, String username) {
        this.text = text;
        this.username = username;
    }

    @Override
    public String toString() {
        return username + ": " + text; // This will be used by ListView to display messages
    }
}