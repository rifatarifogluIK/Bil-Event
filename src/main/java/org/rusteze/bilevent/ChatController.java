package org.rusteze.bilevent;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;

public class ChatController {
    @FXML
    private ListView<Message> messageListView;

    private ChatSpace chatSpace;

    @FXML
    public void initialize() {
        chatSpace = new ChatSpace();
        messageListView.setItems(chatSpace.getMessages());

       // chatSpace.addMessage("Hello, World!", "CxTurkO");
        //chatSpace.addMessage("Hi there, What have you been up to?", "ALoser");
    }
}

