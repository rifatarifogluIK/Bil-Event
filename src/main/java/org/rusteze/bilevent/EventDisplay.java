package org.rusteze.bilevent;

import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;

public class EventDisplay extends Pane {
    private Event event;

    public EventDisplay(Event event) {
        this.event = event;
        this.setOnMouseClicked(this::onClick);
        HBox innerContainer = new HBox();
        ImageView image = new ImageView(event.getPhoto());
        image.setFitWidth(20);
        image.setFitHeight(20);
        Label eventName = new Label(event.getName());
        eventName.setFont(Font.font("Trebuchet MS", 20));
        eventName.setStyle("-fx-font-weight: bold;");
        innerContainer.getChildren().add(image);
        innerContainer.getChildren().add(eventName);
        getChildren().add(innerContainer);
        innerContainer.setLayoutX(20);
        setOnMouseEntered(e -> this.setCursor(Cursor.HAND));
        setOnMouseExited(e -> this.setCursor(Cursor.DEFAULT));
    }

    public void onClick(MouseEvent mouseEvent) {
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
