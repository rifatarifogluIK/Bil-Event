package org.rusteze.bilevent;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.NodeOrientation;
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
import javafx.scene.layout.Pane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.event.ActionEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class CommunityPane extends SearchContainer {

    private Community community;
    private Button button1;
    public CommunityPane(Community community) {
        this.community = community;
        button1 = new Button();
        button1.setFont(new Font(15));
        button1.setAlignment(Pos.BASELINE_CENTER);
        button1.setOnMouseEntered(this::onHoverWhite);
        button1.setOnMouseExited(this::exitHoverWhite);
        button1.setPrefHeight(40);
        button1.setPrefWidth(80);
        HBox.setMargin(button1, new Insets(10, 0, 0, 20));
        buttonPanel.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
        button1.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 12px");
        nameLabel.setText(community.getName());
        button1.setText("Join");
        button2.setText("Details");
        buttonPanel.getChildren().add(button1);
        if(community.getMembers().contains(HelloApplication.sessionUser)) {
            button1.setText("Leave");
        }
        button1.setOnAction(this::joinBtn);
        button2.setOnAction(this::detailsBtn);
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
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            try {
                Parent root = FXMLLoader.load(getClass().getResource("discovercommunity.fxml"));
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
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