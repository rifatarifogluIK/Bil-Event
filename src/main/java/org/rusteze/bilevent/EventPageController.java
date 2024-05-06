package org.rusteze.bilevent;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class EventPageController implements Initializable {


    @FXML
    private Label eventName;

    private ListView<Message> messageListView;
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

    public static void setEvent(Event event) {
        EventPageController.event = event;
    }
    public void setPage() {
        eventName.setText(event.getName());
    }
    public void setChatSpace() {
        ChatSpace chatSpace = event.getChatSpace();
        messageListView.setItems(chatSpace.getMessages());
        chatSpace.addMessage("Welcome to the " + event.getName() + " Chat!", "System" );
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setPage();
    }
}
