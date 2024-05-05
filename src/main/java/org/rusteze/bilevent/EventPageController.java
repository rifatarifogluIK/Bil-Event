package org.rusteze.bilevent;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class EventPageController{
    private ListView<Message> messageListView;
    private ChatSpace chatSpace;
    private TextField messageInputField1;

    private static Event event;


    public void backBtn(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("homepage.fxml"));
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void initialize() {
        chatSpace = new ChatSpace();
        messageListView.setItems(chatSpace.getMessages());

        chatSpace.addMessage("Welcome to the " + event.getName() + " Chat!", "System" );
    }
    private void SendMessage() {
        String text = messageInputField1.getText();
        String username = "You";
        if(!text.isEmpty()) {
            chatSpace.addMessage(text, username);
            messageInputField1.clear();
        }
    }
}
