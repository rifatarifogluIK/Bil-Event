package org.rusteze.bilevent;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.io.IOException;

public class UserPane extends Pane {

    public User user;
    HBox contentBox;

    public UserPane(User user) {
        this.user = user;

        setPrefHeight(50);
        setPrefWidth(200);
        Pane innerPane = new Pane();
        innerPane.setPrefWidth(190);
        innerPane.setPrefHeight(50);
        innerPane.setLayoutX(30);
        innerPane.setStyle("-fx-background-color: #7d87ff; -fx-background-radius: 18px;");
        innerPane.setOnMouseEntered(this::onHover);
        innerPane.setOnMouseExited(this::exitHover);
        innerPane.setOnMouseClicked(this::onClick);
        contentBox = new HBox();

        Image image = user.getPhoto();
        ImageView profilePicture = new ImageView(image);
        profilePicture.setFitHeight(30);
        profilePicture.setFitWidth(30);

        Label userName = new Label(user.getUsername());
        userName.setFont(Font.font("Trebuchet MS", FontWeight.BOLD,14));
        userName.setPrefWidth(70);
        contentBox.getChildren().add(profilePicture);
        contentBox.getChildren().add(userName);
        HBox.setMargin(profilePicture, new Insets(10,10,0,10));
        HBox.setMargin(userName, new Insets(10,0,0,0));

        innerPane.getChildren().add(contentBox);
        getChildren().add(innerPane);
    }
    public void onClick(MouseEvent event) {

        try {
            ProfileController.setUser(user);
            Parent root = FXMLLoader.load(getClass().getResource("ProfilePage.fxml"));
            Scene scene = ((Node) event.getSource()).getScene();
            scene.setRoot(root);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void onHover(MouseEvent event) {
        ((Node) event.getSource()).setCursor(Cursor.HAND);
        ((Node) event.getSource()).setStyle("-fx-background-color: #5e6af8; -fx-background-radius: 21px;");
    }
    public void exitHover(MouseEvent event) {
        ((Node) event.getSource()).setCursor(Cursor.DEFAULT);
        ((Node)event.getSource()).setStyle("-fx-background-color: #7d87ff; -fx-background-radius: 18px;");
    }
}
