package org.rusteze.bilevent;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;

public class EventPane extends Pane {

    private Event event;
    private ImageView imageView;

    public EventPane(Event event) {

        this.event = event;

        VBox innerBox = new VBox();
        Image photo = event.getPhoto();
        imageView = new ImageView(photo);
        imageView.setFitWidth(200);
        imageView.setFitHeight(180);
        imageView.setStyle("-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.2), 10, 0, 0, 0);");
        Label eventName = new Label(event.getName());
        eventName.setFont(Font.font("Trebuchet MS", 20));
        eventName.setPadding(new Insets(5,0,0,0));
        innerBox.setAlignment(Pos.CENTER);
        innerBox.getChildren().add(imageView);
        innerBox.getChildren().add(eventName);
        getChildren().add(innerBox);
        setOnMouseClicked(this::eventClicked);
        setPrefHeight(200);
        imageView.setOnMouseEntered(this::onHover);
        imageView.setOnMouseExited(this::exitHover);
        setOnMouseEntered(e -> this.setCursor(Cursor.HAND));
        setOnMouseExited(e -> this.setCursor(Cursor.DEFAULT));
    }
    public void onHover(MouseEvent mouseEvent) {

        imageView.setStyle("-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.5), 20, 0, 0, 0);");
    }
    public void exitHover(MouseEvent mouseEvent) {

        imageView.setStyle("-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.2), 10, 0, 0, 0);");
    }

    private void eventClicked(MouseEvent mouseEvent) {

        try {
            EventPageController.setEvent(this.event);
            Parent root = FXMLLoader.load(getClass().getResource("eventPage.fxml"));
            Scene scene = ((Node) mouseEvent.getSource()).getScene();
            scene.setRoot(root);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
