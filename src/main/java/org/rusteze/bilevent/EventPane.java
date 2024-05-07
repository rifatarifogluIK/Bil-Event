package org.rusteze.bilevent;

import javafx.fxml.FXMLLoader;
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

    public EventPane(Event event) {

        this.event = event;

        VBox innerBox = new VBox();
        Image photo = event.getPhoto();
        ImageView imageView = new ImageView(photo);
        imageView.setFitWidth(200);
        imageView.setFitHeight(180);
        Label eventName = new Label(event.getName());
        eventName.setFont(Font.font("Trebuchet MS", 20));
        innerBox.setAlignment(Pos.CENTER);
        innerBox.getChildren().add(imageView);
        innerBox.getChildren().add(eventName);
        getChildren().add(innerBox);
        setOnMouseClicked(this::eventClicked);
        setPrefHeight(400);
        setPrefWidth(400);
        setOnMouseEntered(e -> this.setCursor(Cursor.HAND));
        setOnMouseExited(e -> this.setCursor(Cursor.DEFAULT));
    }

    private void eventClicked(MouseEvent mouseEvent) {
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        try {
            EventPageController.setEvent(this.event);
            Parent root = FXMLLoader.load(getClass().getResource("eventPage.fxml"));
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
