package org.rusteze.bilevent;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.ResourceBundle;

public class DiscoverController implements SceneHandler, Initializable {

    @FXML
    private VBox buttonPanel;
    @FXML
    private VBox popCommunities;
    @FXML
    private VBox friendPanel;
    @FXML
    Label profileName;
    @FXML
    ImageView profilePicture;

    public void setPage() {

        Enumeration<Community> popular = Community.popularCommunities.elements();
        while (popular.hasMoreElements()) {
            Community community = popular.nextElement();
            CommunityPane communityPane = new CommunityPane(community);
            popCommunities.getChildren().add(communityPane);
            VBox.setMargin(communityPane, new Insets(0, 0, 40, 0));
        }
        profileName.setText(HelloApplication.sessionUser.getUsername());
        profilePicture.setImage(HelloApplication.sessionUser.getPhoto());
    }

    public void onHover(MouseEvent mouseEvent) {
        ((Button)mouseEvent.getSource()).setStyle("-fx-background-color: #61aef7; -fx-font-family: \"Trebuchet\"; -fx-font-size: 15px; -fx-background-radius: 18px");
        ((Button)mouseEvent.getSource()).setCursor(Cursor.HAND);
    }
    public void exitHover(MouseEvent mouseEvent) {
        ((Button)mouseEvent.getSource()).setStyle("-fx-background-color: #B5DBFF; -fx-font-family: \"Trebuchet\"; -fx-font-size: 15px; -fx-background-radius: 12px");
        ((Button)mouseEvent.getSource()).setCursor(Cursor.DEFAULT);
    }
    public void onHoverWhite(MouseEvent mouseEvent) {
        ((Button)mouseEvent.getSource()).setStyle("-fx-background-color: #dfdfdf; -fx-background-radius: 12px");
        ((Button)mouseEvent.getSource()).setCursor(Cursor.HAND);
    }
    public void exitHoverWhite(MouseEvent mouseEvent) {
        ((Button)mouseEvent.getSource()).setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 12px");
        ((Button)mouseEvent.getSource()).setCursor(Cursor.DEFAULT);
    }
    public void onVBoxHover(MouseEvent mouseEvent) {
        DropShadow dropShadow = new DropShadow();
        dropShadow.setRadius(20);
        dropShadow.setOffsetX(0);
        dropShadow.setOffsetY(0);
        dropShadow.setColor(Color.rgb(0, 0, 0, 1));
        ((Node)mouseEvent.getSource()).setEffect(dropShadow);
    }
    public void exitVBoxHover(MouseEvent mouseEvent) {

        DropShadow dropShadow = new DropShadow();
        dropShadow.setRadius(20);
        dropShadow.setOffsetX(0);
        dropShadow.setOffsetY(0);
        dropShadow.setColor(Color.rgb(0, 0, 0, 0));
        ((Node)mouseEvent.getSource()).setEffect(dropShadow);
    }
    @Override
    public void displayFriends() {
        ArrayList<User> friends = HelloApplication.sessionUser.getFriends();

        for(User friend : friends) {
            UserPane userPane = new UserPane(friend);
            friendPanel.getChildren().add(userPane);
            VBox.setMargin(userPane, new Insets(0,0,10,0));
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
    public void createBtn(ActionEvent event) {

        try {
            Parent root = FXMLLoader.load(getClass().getResource("CreateCommunity.fxml"));
            Scene scene = ((Node) event.getSource()).getScene();
            scene.setRoot(root);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void createEventBtn(ActionEvent event) {


        try {
            CreateEventController.setCreatorCommunity(null);
            Parent root = FXMLLoader.load(getClass().getResource("createevent.fxml"));
            Scene scene = ((Node) event.getSource()).getScene();
            scene.setRoot(root);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void logOutBtn(ActionEvent event) {

        try {
            Parent root = FXMLLoader.load(getClass().getResource("LogIn.fxml"));
            Stage stage =(Stage)((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void homeBtn(ActionEvent event) {

        try {
            Parent root = FXMLLoader.load(getClass().getResource("homepage.fxml"));
            Scene scene = ((Node) event.getSource()).getScene();
            scene.setRoot(root);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void calendarBtn(ActionEvent event) {

        try {
            Parent root = FXMLLoader.load(getClass().getResource("calendarpage.fxml"));
            Scene scene = ((Node) event.getSource()).getScene();
            scene.setRoot(root);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void discoverBtn(ActionEvent event) {

        try {
            Parent root = FXMLLoader.load(getClass().getResource("discovercommunity.fxml"));
            Scene scene = ((Node) event.getSource()).getScene();
            scene.setRoot(root);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void searchBtn(ActionEvent event) {

        try {
            Parent root = FXMLLoader.load(getClass().getResource("advancedSearch.fxml"));
            Scene scene = ((Node) event.getSource()).getScene();
            scene.setRoot(root);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void profileBtn(MouseEvent event) {
        try {
            ProfileController.setUser(HelloApplication.sessionUser);
            Parent root = FXMLLoader.load(getClass().getResource("ProfilePage.fxml"));
            Scene scene = ((Node) event.getSource()).getScene();
            scene.setRoot(root);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        createCommunityButtons();
        displayFriends();
        setPage();
    }
}
