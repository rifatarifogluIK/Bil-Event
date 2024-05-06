package org.rusteze.bilevent;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.image.ImageView;
import javafx.geometry.Insets;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class HomeController implements SceneHandler, Initializable {

    @FXML
    VBox buttonPanel;
    @FXML
    HBox eventPanel;

    public void displayEvents() {
    //TODO
        ArrayList<Event> recommendations = new ArrayList<Event>();
        recommendations.add(new PersonalEvent(HelloApplication.sessionUser, " MayFest", "aaa", null));
        recommendations.add(new PersonalEvent(HelloApplication.sessionUser, " Concert", "aaa", null));
        recommendations.add(new PersonalEvent(HelloApplication.sessionUser, " F1 Race", "aaa", null));
        recommendations.add(new PersonalEvent(HelloApplication.sessionUser, " deneme4", "aaa", null));
        recommendations.add(new PersonalEvent(HelloApplication.sessionUser, " deneme4", "aaa", null));
        recommendations.add(new PersonalEvent(HelloApplication.sessionUser, " deneme4", "aaa", null));
        recommendations.add(new PersonalEvent(HelloApplication.sessionUser, " deneme4", "aaa", null));
        recommendations.add(new PersonalEvent(HelloApplication.sessionUser, " deneme4", "aaa", null));
        for(Event e : recommendations) {
            EventPane eventPane = new EventPane(e);
            eventPanel.getChildren().add(eventPane);
            HBox.setMargin(eventPane, new Insets(0, 100, 0, 0));
            eventPanel.setAlignment(Pos.CENTER_LEFT);
        }

    }
    @Override
    public void createCommunityButtons() {
        ArrayList<Community> communities= HelloApplication.sessionUser.getCommunities();

        int buttonCount = communities.size();

        for(int i = 0; i < buttonCount; i++) {
            CommunityButton comBtn = new CommunityButton(communities.get(i));
            buttonPanel.getChildren().add(comBtn);
            VBox.setMargin(comBtn, new Insets(0, 15, 0, 0));
        }
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        displayEvents();
        createCommunityButtons();
    }
}
