package org.rusteze.bilevent;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ProfileController implements Initializable {

    private static User user;
    @FXML
    Label userName;
    @FXML
    Label eventLabel;
    @FXML
    HBox eventPanel;
    @FXML
    Button addFriend;
    @FXML
    VBox requestPanel;
    @FXML
    Label requestLabel;
    @FXML
    ScrollPane requestContainer;
    @FXML
    Label promptLabel;
    @FXML
    ImageView photo;

    public void onHoverPhoto(MouseEvent mouseEvent) {
        if(HelloApplication.sessionUser == user) {
            promptLabel.setVisible(true);
        }
    }
    public void exitHoverPhoto(MouseEvent mouseEvent) {
        if(HelloApplication.sessionUser == user) {
            promptLabel.setVisible(false);
        }
    }
    public void onPhotoClick(MouseEvent mouseEvent) {
        if(HelloApplication.sessionUser == user) {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif")
            );

            File selectedFile = fileChooser.showOpenDialog(((Node) mouseEvent.getSource()).getScene().getWindow());
            if (selectedFile != null) {
                Image image = new Image(selectedFile.toURI().toString());
                user.setPhoto(image);
            }
            try {
                Parent root = FXMLLoader.load(getClass().getResource("ProfilePage.fxml"));
                Scene scene = ((Node) mouseEvent.getSource()).getScene();
                scene.setRoot(root);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }
    public void submitBtn() {
        File saveFile = new File("src/main/resources/org/rusteze/bilevent/ImageDB", user.getImageName());

        Image fxImage = photo.getImage();
        BufferedImage bImage = SwingFXUtils.fromFXImage(fxImage, null);
        try {
            ImageIO.write(bImage, "png", saveFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public class RequestPane extends UserPane{

        public RequestPane(User user) {
            super(user);
            File acceptFile = new File("src/main/resources/org/rusteze/bilevent/Images/Accept.png");
            Image acceptIcon = new Image(acceptFile.toURI().toString());
            File rejectFile = new File("src/main/resources/org/rusteze/bilevent/Images/Reject.png");
            Image rejectIcon= new Image(rejectFile.toURI().toString());

            ImageView accept = new ImageView(acceptIcon);
            ImageView reject = new ImageView(rejectIcon);
            accept.setOnMouseClicked(this::accept);
            reject.setOnMouseClicked(this::reject);
            accept.setFitWidth(30);
            reject.setFitWidth(30);
            accept.setFitHeight(30);
            reject.setFitHeight(30);

            contentBox.getChildren().add(accept);
            contentBox.getChildren().add(reject);
        }
        public void accept(MouseEvent event) {
            HelloApplication.sessionUser.addFriend(user);
            user.addFriend(HelloApplication.sessionUser);
        }
        public void reject(MouseEvent event) {
            HelloApplication.sessionUser.getFriendRequests().remove(user);
        }
    }

    public void setPage() {
        userName.setText(user.getUsername());
        eventLabel.setText(user.getUsername() + "'s Enrolled Events");
        addFriend.setText("Add");
        promptLabel.setVisible(false);
        photo.setImage(user.getPhoto());

        if(user == HelloApplication.sessionUser) {
            addFriend.setVisible(false);
            displayRequests();
        } else {
            requestPanel.setVisible(false);
            requestContainer.setVisible(false);
            requestLabel.setVisible(false);
        }
        if(HelloApplication.sessionUser.getFriends().contains(user)) {
            addFriend.setText("Remove");
        } else if(user.getFriendRequests().contains(HelloApplication.sessionUser)) {
            addFriend.setText("Pending");
        }

        displayEvents();
    }
    public void addBtn(ActionEvent event) {

        if(addFriend.getText().equals("Add")) {
            user.getFriendRequests().add(HelloApplication.sessionUser);
            setPage();
        } else if (addFriend.getText().equals("Remove")) {
            HelloApplication.sessionUser.getFriends().remove(user);
            user.getFriends().remove(HelloApplication.sessionUser);
            setPage();
        } else if(addFriend.getText().equals("Pending")) {
            user.getFriendRequests().remove(HelloApplication.sessionUser);
            setPage();
        }
    }
    public void onHover(MouseEvent mouseEvent) {
        ((Button)mouseEvent.getSource()).setStyle("-fx-background-color: #61aef7; -fx-font-family: \"Trebuchet\"; -fx-font-size: 15px; -fx-background-radius: 18px");
        ((Button)mouseEvent.getSource()).setCursor(Cursor.HAND);
    }
    public void exitHover(MouseEvent mouseEvent) {
        ((Button)mouseEvent.getSource()).setStyle("-fx-background-color: #B5DBFF; -fx-font-family: \"Trebuchet\"; -fx-font-size: 15px; -fx-background-radius: 12px");
        ((Button)mouseEvent.getSource()).setCursor(Cursor.DEFAULT);
    }
    public void displayRequests() {
        ArrayList<User> requests = user.getFriendRequests();
        requestPanel.getChildren().clear();

        for(User request : requests) {
            RequestPane userPane = new RequestPane(request);
            requestPanel.getChildren().add(userPane);
            VBox.setMargin(userPane, new Insets(0,0,10,0));
        }
    }

    public void displayEvents() {
        eventPanel.getChildren().clear();
        ArrayList<Event> currentEvents = user.getEnrolledEvents();
        for(Event e : currentEvents) {
            EventPane eventPane = new EventPane(e);
            eventPanel.getChildren().add(eventPane);
            HBox.setMargin(eventPane, new Insets(100, 30, 0, 0));
            eventPanel.setAlignment(Pos.BASELINE_LEFT);
        }
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
    public static void setUser(User user) {
        ProfileController.user = user;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        requestPanel.getChildren().clear();
        setPage();
    }
}
