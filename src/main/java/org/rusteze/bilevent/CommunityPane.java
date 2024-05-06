package org.rusteze.bilevent;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.event.ActionEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class CommunityPane extends Pane {

    private Community community;

    public CommunityPane(Community community) {
        this.community = community;
        Pane innerPane = new Pane();
        innerPane.setPrefHeight(35);
        innerPane.setPrefWidth(380);
        innerPane.setLayoutX(10);
        setPrefHeight(35);
        setPrefWidth(395);
        innerPane.setStyle("-fx-background-color: #FF7F7F; -fx-background-radius: 8px");

        BorderPane borderPane = new BorderPane();
        Label label = new Label(community.getName());
        label.setFont(Font.font("Trebuchet MS", 15));
        BorderPane.setMargin(label, new Insets(0,0,0,10));
        label.setTextFill(Color.WHITE);
        label.setPrefWidth(250);
        label.setAlignment(Pos.CENTER_LEFT);
        BorderPane.setAlignment(label, Pos.CENTER);
        borderPane.setLeft(label);

        HBox buttonPanel = new HBox();
        Button joinBtn = new Button("Join");

        if(community.getMembers().contains(HelloApplication.sessionUser)) {
            joinBtn.setText("Leave");
        }
        Button detailsBtn = new Button("Details");
        joinBtn.setAlignment(Pos.CENTER);
        detailsBtn.setAlignment(Pos.CENTER);

        joinBtn.setOnAction(this::joinBtn);
        detailsBtn.setOnAction(this::detailsBtn);
        joinBtn.setPrefWidth(50);

        HBox.setMargin(joinBtn, new Insets(5, 10, 0, 0));
        HBox.setMargin(detailsBtn, new Insets(5, 10, 0, 0));
        buttonPanel.setAlignment(Pos.CENTER_RIGHT);
        detailsBtn.setAlignment(Pos.CENTER_RIGHT);
        joinBtn.setAlignment(Pos.CENTER_RIGHT);

        joinBtn.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 10px");
        detailsBtn.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 10px");

        buttonPanel.getChildren().add(joinBtn);
        buttonPanel.getChildren().add(detailsBtn);

        borderPane.setRight(buttonPanel);
        BorderPane.setAlignment(buttonPanel, Pos.CENTER_RIGHT);
        innerPane.getChildren().add(borderPane);
        getChildren().add(innerPane);
    }

    private void joinBtn(ActionEvent event) {

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmation Dialog");
        confirmation.setHeaderText("Are you sure you want to " + ((Button)event.getSource()).getText().toLowerCase() + " this community?");

        confirmation.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK && ((Button)event.getSource()).getText().equals("Join")) {
                HelloApplication.sessionUser.joinCommunity(community);
            } else if(response == ButtonType.OK && ((Button)event.getSource()).getText().equals("Leave")) {
                HelloApplication.sessionUser.removeCommunity(community);
            }
        });

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("discovercommunity.fxml"));
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void detailsBtn(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        try {
            CommunityPageController.setCommunity(community);
            Parent root = FXMLLoader.load(getClass().getResource("departmentpage.fxml"));
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}