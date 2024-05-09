package org.rusteze.bilevent;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import java.io.File;

public class UserPane extends Pane {

    private User user;

    public UserPane(User user) {
        this.user = user;

        setPrefHeight(50);
        setPrefWidth(250);
        Pane innerPane = new Pane();
        innerPane.setPrefWidth(200);
        innerPane.setPrefHeight(50);
        innerPane.setLayoutX(25);
        innerPane.setStyle("-fx-background-color: #7d87ff; -fx-background-radius: 18px;");
        innerPane.setOnMouseEntered(this::onHover);
        innerPane.setOnMouseExited(this::exitHover);
        HBox contentBox = new HBox();
        ImageView profilePicture = new ImageView(user.getPhoto());
        profilePicture.setFitHeight(30);
        profilePicture.setFitWidth(30);
        Label userName = new Label(user.getUsername());
        userName.setFont(Font.font("Trebuchet MS", FontWeight.BOLD,20));
        contentBox.getChildren().add(profilePicture);
        contentBox.getChildren().add(userName);
        HBox.setMargin(profilePicture, new Insets(10,10,0,10));
        HBox.setMargin(userName, new Insets(10,0,0,0));

        innerPane.getChildren().add(contentBox);
        getChildren().add(innerPane);
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