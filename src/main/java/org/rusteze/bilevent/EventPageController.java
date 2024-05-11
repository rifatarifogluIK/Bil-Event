package org.rusteze.bilevent;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class EventPageController implements Initializable {

    @FXML
    Button sendButton;
    @FXML
    Label eventName;
    @FXML
    Label dateLabel;
    @FXML
    Label organizerLabel;
    @FXML
    Label locationLabel;
    @FXML
    Label attendCount;
    @FXML
    Button enrollBtn;
    @FXML
    ListView<Message> messageListView;
    @FXML
    TextField messageInputField1;
    @FXML
    VBox attendeePanel;
    @FXML
    ImageView eventPhoto;
    @FXML
    Label eventDescription;

    private static Event event;
    private User currentUser;

    public void enrollBtn(ActionEvent event) {

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        confirmation.initOwner(stage);

        confirmation.setTitle("Confirmation Dialog");
        confirmation.setHeaderText("Are You Sure You Want To " + enrollBtn.getText() + " This Event?");

        confirmation.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                if (enrollBtn.getText().equals("Enroll")) {
                    HelloApplication.sessionUser.enrollEvent(this.event);
                    setPage();
                } else if (enrollBtn.getText().equals("Leave")) {
                    HelloApplication.sessionUser.leaveEvent(this.event);
                    setPage();
                }
            }
        });


    }

    public void backBtn(ActionEvent event) {

        try {
            Parent root = FXMLLoader.load(getClass().getResource("homepage.fxml"));
            Scene scene = ((Node) event.getSource()).getScene();
            scene.setRoot(root);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void onHover(MouseEvent mouseEvent) {
        ((Button) mouseEvent.getSource()).setStyle("-fx-background-color: #61aef7; -fx-font-family: \"Trebuchet\"; -fx-font-size: 15px; -fx-background-radius: 18px");
        ((Button) mouseEvent.getSource()).setCursor(Cursor.HAND);
    }

    public void exitHover(MouseEvent mouseEvent) {
        ((Button) mouseEvent.getSource()).setStyle("-fx-background-color: #B5DBFF; -fx-font-family: \"Trebuchet\"; -fx-font-size: 15px; -fx-background-radius: 12px");
        ((Button) mouseEvent.getSource()).setCursor(Cursor.DEFAULT);
    }

    public void onHoverWhite(MouseEvent mouseEvent) {
        ((Button) mouseEvent.getSource()).setStyle("-fx-background-color: #dfdfdf; -fx-background-radius: 12px");
        ((Button) mouseEvent.getSource()).setCursor(Cursor.HAND);
    }

    public void exitHoverWhite(MouseEvent mouseEvent) {
        ((Button) mouseEvent.getSource()).setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 12px");
        ((Button) mouseEvent.getSource()).setCursor(Cursor.DEFAULT);
    }

    public static void setEvent(Event event) {
        EventPageController.event = event;
    }

    public void setPage() {
        refresh();
        eventName.setText(event.getName());
        eventPhoto.setImage(event.getPhoto());
        dateLabel.setText(dateLabel.getText() + " " + event.getDate());
        organizerLabel.setText(organizerLabel.getText() + " " + event.getOrganizer());
        locationLabel.setText(locationLabel.getText() + " " + event.getLocation());
        attendCount.setText(attendCount.getText() + " " + event.getAttendees().size());
        eventDescription.setText(event.getDescription());
        enrollBtn.setText("Enroll");
        if (HelloApplication.sessionUser.getEnrolledEvents().contains(event)) {
            enrollBtn.setText("Leave");
        }
        displayMembers();
    }
    public void displayMembers() {
        ArrayList<User> attendees = event.getAttendees();

        for(User attendee : attendees) {
            UserPane userPane = new UserPane(attendee);
            attendeePanel.getChildren().add(userPane);
            VBox.setMargin(userPane, new Insets(0,0,10,0));
        }
    }

    public void refresh() {
        dateLabel.setText("Date: ");
        organizerLabel.setText("Organizer: ");
        locationLabel.setText("Location: ");
        attendCount.setText("Attend Count:");
    }

    public void setChatSpace() {
        ChatSpace chatSpace = event.getChatSpace();
        messageListView.setItems(chatSpace.getMessages());
        chatSpace.addMessage("Welcome to the " + event.getName() + " Chat!", "System");
        messageInputField1.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                sendChatMessage();
                event.consume();
            }
        });
    }

    public void sendChatMessage() {
        String messageText = messageInputField1.getText();
        ChatSpace chatSpace = event.getChatSpace();
        if (!messageText.isEmpty()) {
            chatSpace.addMessage(messageText, currentUser.getUsername());
            messageInputField1.clear();
        }
    }
    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setPage();
        setChatSpace();
        setCurrentUser(HelloApplication.sessionUser);
    }
}
