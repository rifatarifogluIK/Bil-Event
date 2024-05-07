package org.rusteze.bilevent;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.scene.Cursor;
import java.io.IOException;

public class CommunityButton extends Button {

    private Community community;

    CommunityButton(Community community) {
        this.community = community;
        setText(community.getName());
        setStyle("-fx-background-color: #B5DBFF; -fx-font-family: \"Trebuchet\"; -fx-font-size: 15px; -fx-background-radius: 12px");
        setPrefHeight(40);
        setPrefWidth(200);
        setAlignment(Pos.BASELINE_LEFT);
        setOnAction(this::CommunityBtn);
        setOnMouseEntered(e -> onHover());
        setOnMouseExited(e -> exitHover());
    }

    public void onHover() {
        this.setStyle("-fx-background-color: #61aef7; -fx-font-family: \"Trebuchet\"; -fx-font-size: 15px; -fx-background-radius: 18px");
        this.setCursor(Cursor.HAND);
    }
    public void exitHover() {
        this.setStyle("-fx-background-color: #B5DBFF; -fx-font-family: \"Trebuchet\"; -fx-font-size: 15px; -fx-background-radius: 12px");
        this.setCursor(Cursor.DEFAULT);
    }

    public  void CommunityBtn(ActionEvent event) {
        CommunityPageController.setCommunity(((CommunityButton) event.getSource()).getCommunity());

        try {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("departmentpage.fxml"));
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Community getCommunity() {
        return community;
    }
}
