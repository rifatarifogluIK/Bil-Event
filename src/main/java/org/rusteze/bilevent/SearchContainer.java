package org.rusteze.bilevent;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.io.IOException;

public class SearchContainer extends Pane {

    Button button2;
    Label nameLabel;
    HBox buttonPanel;
    Pane innerPane;

    public SearchContainer() {
        innerPane = new Pane();
        innerPane.setPrefHeight(60);
        innerPane.setPrefWidth(950);
        innerPane.setLayoutX(43);
        setPrefWidth(1036);
        setPrefHeight(35);
        innerPane.setStyle("-fx-background-color: #FF7F7F; -fx-background-radius: 8px");

        BorderPane borderPane = new BorderPane();
        nameLabel = new Label();
        nameLabel.setFont(Font.font("Trebuchet MS", FontWeight.BOLD, 30));
        BorderPane.setMargin(nameLabel, new Insets(10,0,0,40));
        nameLabel.setPrefWidth(700);
        nameLabel.setAlignment(Pos.BASELINE_LEFT);
        BorderPane.setAlignment(nameLabel, Pos.CENTER);
        borderPane.setLeft(nameLabel);

        buttonPanel = new HBox();


        button2 = new Button();
        button2.setFont(new Font(15));

        button2.setAlignment(Pos.BASELINE_CENTER);
        button2.setOnMouseEntered(this::onHoverWhite);
        button2.setOnMouseExited(this::exitHoverWhite);
        button2.setPrefHeight(40);
        button2.setPrefWidth(80);
        HBox.setMargin(button2, new Insets(10, 0, 0, 20));
        button2.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 12px");
        buttonPanel.getChildren().add(button2);

        borderPane.setRight(buttonPanel);
        innerPane.getChildren().add(borderPane);
        getChildren().add(innerPane);
    }

    public void onHoverWhite(MouseEvent mouseEvent) {
        ((Button)mouseEvent.getSource()).setStyle("-fx-background-color: #d1d0d0; -fx-background-radius: 12px");
        ((Button)mouseEvent.getSource()).setCursor(Cursor.HAND);
    }
    public void exitHoverWhite(MouseEvent mouseEvent) {
        ((Button)mouseEvent.getSource()).setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 12px");
        ((Button)mouseEvent.getSource()).setCursor(Cursor.DEFAULT);
    }


}
