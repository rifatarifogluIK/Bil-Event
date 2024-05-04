package org.rusteze.bilevent;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.scene.control.Label;

public class CommunityPageController implements SceneHandler, Initializable {
    @FXML
    private Label departmentLabel;
    private static Community community;

    public void setPage() {
        departmentLabel.setText(community.getName());
    }
    @Override
    public void createCommunityButtons() {

    }

    @Override
    public void homeBtn(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("homepage.fxml"));
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void calendarBtn(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("calendarpage.fxml"));
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void discoverBtn(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("discovercommunity.fxml"));
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void searchBtn(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("advancedSearch.fxml"));
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void setCommunity(Community community) {
        CommunityPageController.community = community;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setPage();
    }
}
