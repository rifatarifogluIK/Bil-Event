package org.rusteze.bilevent;

import org.bson.Document;

import java.io.FileNotFoundException;

public class Message implements ConvertibleWithDocument<Message> {
    private String text;
    private String username;

    public Message(String text, String username) {
        this.text = text;
        this.username = username;
    }

    public Message(){
    }

    @Override
    public String toString() {
        return username + ": " + text; // This will be used by ListView to display messages
    }
    @Override
    public Document toDocument(){
        Document doc = new Document();

        doc.append("text", this.text)
                .append("username", this.username);

        return doc;
    }
    @Override
    public Message fromDocument(Document doc) throws FileNotFoundException {
        this.text = (String)doc.get("text");
        this.username = (String)doc.get("username");
        return this;
    }
}