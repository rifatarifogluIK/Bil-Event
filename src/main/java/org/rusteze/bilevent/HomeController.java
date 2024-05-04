package org.rusteze.bilevent;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class HomeController implements SceneHandler, Initializable {

    @FXML
    VBox buttonPanel;
    @FXML
    Label eventName1;
    @FXML
    ImageView eventImage1;
    @FXML
    Label eventName2;
    @FXML
    ImageView eventImage2;
    @FXML
    Label eventName3;
    @FXML
    ImageView eventImage3;
    @FXML
    Label eventName4;
    @FXML
    ImageView eventImage4;
    Label[] eventNames = {eventName1, eventName2, eventName3, eventName4};
    ImageView[] eventImages = {eventImage1, eventImage2, eventImage3, eventImage4};
    private static final int EVENT_COUNT = 4;
    private static final int MAX_COMMUNITY_BUTTONS = 5;


    public void displayEvents() {

        ArrayList<Event> recommendations = HelloApplication.sessionUser.getRecommendations();

        for(int i = 0; i < EVENT_COUNT; i++) {
            eventNames[i].setText(recommendations.get(i).getName());
            eventImages[i].setImage(recommendations.get(i).getPhoto());
        }

    }
    @Override
    public void createCommunityButtons() {
        ArrayList<Community> communities= HelloApplication.sessionUser.getCommunities();

        int buttonCount = communities.size() > MAX_COMMUNITY_BUTTONS ? MAX_COMMUNITY_BUTTONS : communities.size();

        for(int i = 0; i < buttonCount; i++) {
            //TODO implement a button type as CommunityButton
            //buttonPanel.add(new CommunityButton(communities.get(i).getName());
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
