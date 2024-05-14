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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
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
    @FXML
    Pane infoPanel;
    @FXML
    Label ratingLabel;

    private static Event event;
    private User currentUser;

    public class RatingStars extends Pane{
        Image star;
        Image filledStar;
        ImageView[] ratingStars;
        boolean clicked;
        int selectedRating;

        public RatingStars() {
            this.clicked = false;
            this.selectedRating = 0;
            File starFile = new File("src/main/resources/org/rusteze/bilevent/Images/star.png");
            star = new Image(starFile.toURI().toString());
            File filledStarFile = new File("src/main/resources/org/rusteze/bilevent/Images/filledStar.png");
            filledStar = new Image(filledStarFile.toURI().toString());
            ratingStars = new ImageView[5];
            setPrefWidth(205);
            setPrefHeight(30);
            setLayoutX(275);
            setLayoutY(200);
            setOnMouseExited(this::exitStarHover);
            infoPanel.getChildren().add(this);
            for(int i = 0; i < ratingStars.length; i++) {
                ratingStars[i] = new ImageView(star);
                this.getChildren().add(ratingStars[i]);
                ratingStars[i].setFitHeight(30);
                ratingStars[i].setFitWidth(30);
                ratingStars[i].setLayoutX((i * 35));
                ratingStars[i].setOnMouseEntered(this::starHover);
                ratingStars[i].setOnMouseExited(this::exitHover);
                ratingStars[i].setOnMouseClicked(this::onClick);
            }
        }
        public void onClick(MouseEvent event) {
            clicked = true;
            Image convertTo = filledStar;
            for(int i = 0; i < ratingStars.length; i++) {
                ratingStars[i].setImage(convertTo);
                if(ratingStars[i] == event.getSource()) {
                    selectedRating = (i + 1);
                    convertTo = star;
                }
            }
            Button submit = new Button("Submit");
            this.getChildren().add(submit);
            submit.setOnAction(this::onSubmit);
            submit.setLayoutX(190);
            submit.setLayoutY(2);
            submit.setPrefWidth(60);
            submit.setPrefHeight(30);
        }
        public void onSubmit(ActionEvent event) {
            EventPageController.getEvent().addNewRating(selectedRating);
            HelloApplication.sessionUser.getRatedEvents().add(EventPageController.getEvent());
            try {
                Parent root = FXMLLoader.load(getClass().getResource("eventPage.fxml"));
                Scene scene = ((Node) event.getSource()).getScene();
                scene.setRoot(root);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        public void starHover(MouseEvent mouseEvent) {
            setCursor(Cursor.HAND);
            if(!clicked) {
                Image convertTo = filledStar;
                for (int i = 0; i < ratingStars.length; i++) {
                    ratingStars[i].setImage(convertTo);
                    if (ratingStars[i] == mouseEvent.getSource()) {
                        convertTo = star;
                    }
                }
            }
        }
        public void exitHover(MouseEvent mouseEvent) {
            setCursor(Cursor.DEFAULT);
        }
        public void exitStarHover(MouseEvent mouseEvent) {
            if(!clicked) {
                for (ImageView ratingStar : ratingStars) {
                    ratingStar.setImage(star);
                }
            }
        }
    }

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
        ratingLabel.setText("Rating: " + event.getRating());
        eventDescription.setText(event.getDescription());

        if (HelloApplication.sessionUser.getEnrolledEvents().contains(event)) {
            enrollBtn.setText("Leave");
        }else {
            enrollBtn.setText("Enroll");
        }
        attendeePanel.getChildren().clear();
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

    public static Event getEvent() {
        return event;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setPage();
        setChatSpace();
        setCurrentUser(HelloApplication.sessionUser);
        if(!HelloApplication.sessionUser.getRatedEvents().contains(event)) {
            RatingStars stars = new RatingStars();
        }
    }
}
