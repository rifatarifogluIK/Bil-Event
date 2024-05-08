package org.rusteze.bilevent;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class EventPageController implements Initializable {


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

    ListView<Message> messageListView;
    TextField messageInputField1;
    private static Event event;

    public void enrollBtn(ActionEvent event) {

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
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
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("homepage.fxml"));
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void createEventBtn(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("createevent.fxml"));
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void logOutBtn(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("LogIn.fxml"));
            stage.setScene(new Scene(root));
            stage.show();
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
        dateLabel.setText(dateLabel.getText() + " " + event.getDate());
        organizerLabel.setText(organizerLabel.getText() + " " + event.getOrganizer());
        locationLabel.setText(locationLabel.getText() + " " + event.getLocation());
        attendCount.setText(attendCount.getText() + " " + event.getAttendees().size());
        enrollBtn.setText("Enroll");
        if (HelloApplication.sessionUser.getEnrolledEvents().contains(event)) {
            enrollBtn.setText("Leave");
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
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setPage();
    }
}
