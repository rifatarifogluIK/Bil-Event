package org.rusteze.bilevent;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.scene.control.Label;

public class CommunityPageController implements Initializable {
    @FXML
    private Label departmentLabel;
    @FXML
    private Button editBtn;
    @FXML
    private Button createBtn;
    @FXML
    private Button joinBtn;
    private static Community community;

    public void setPage() {
        departmentLabel.setText(community.getName());

        if(!community.isAdmin(HelloApplication.sessionUser)) {
            editBtn.setVisible(false);
            createBtn.setVisible(false);
        }
        if(community.getMembers().contains(HelloApplication.sessionUser)) {
            joinBtn.setText("Leave");
        } else {
            joinBtn.setText("Join");
        }
    }
    public void joinBtn(ActionEvent event) {
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmation Dialog");
        confirmation.setHeaderText("Are you sure you want to " + joinBtn.getText().toLowerCase() + " this community?");

        confirmation.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK && joinBtn.getText().equals("Join")) {
                HelloApplication.sessionUser.joinCommunity(community);
                setPage();
            } else if(response == ButtonType.OK && joinBtn.getText().equals("Leave")) {
                HelloApplication.sessionUser.removeCommunity(community);
                setPage();
            }
        });
    }
    public void backBtn(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("homepage.fxml"));
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
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

    public static void setCommunity(Community community) {
        CommunityPageController.community = community;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setPage();
    }
}
