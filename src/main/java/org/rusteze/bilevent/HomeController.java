package org.rusteze.bilevent;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.scene.image.ImageView;
import javafx.geometry.Insets;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;

public class HomeController implements SceneHandler, Initializable {

    @FXML
    VBox buttonPanel;
    @FXML
    HBox eventPanel;
    @FXML
    VBox day1;
    @FXML
    VBox day2;
    @FXML
    VBox day3;
    @FXML
    VBox day4;
    @FXML
    VBox day5;
    @FXML
    VBox friendPanel;
    @FXML
    Label profileName;
    @FXML
    ImageView profilePicture;

    public void displayEvents() {
    //TODO
        ArrayList<Event> recommendations = HelloApplication.sessionUser.getEventRec();

        for(Event e : recommendations) {
            EventPane eventPane = new EventPane(e);
            eventPanel.getChildren().add(eventPane);
            HBox.setMargin(eventPane, new Insets(100, 30, 0, 0));
            eventPanel.setAlignment(Pos.BASELINE_LEFT);
        }
        weeklyView();
    }

    public void weeklyView() {
        LocalDate today = LocalDate.now();
        day1.setAlignment(Pos.TOP_CENTER);
        day2.setAlignment(Pos.TOP_CENTER);
        day3.setAlignment(Pos.TOP_CENTER);
        day4.setAlignment(Pos.TOP_CENTER);
        day5.setAlignment(Pos.TOP_CENTER);
        Label dayLabel1 = new Label(today.plusDays(0).getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.getDefault()));
        dayLabel1.setFont(Font.font("Trebuchet MS", FontWeight.BOLD, 24));
        dayLabel1.setPadding(new Insets(10,0,0,0));
        day1.getChildren().add(dayLabel1);
        Label dayLabel2 = new Label(today.plusDays(1).getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.getDefault()));
        dayLabel2.setFont(Font.font("Trebuchet MS", FontWeight.BOLD, 24));
        dayLabel2.setPadding(new Insets(10,0,0,0));
        day2.getChildren().add(dayLabel2);
        Label dayLabel3 = new Label(today.plusDays(2).getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.getDefault()));
        dayLabel3.setFont(Font.font("Trebuchet MS", FontWeight.BOLD, 24));
        dayLabel3.setPadding(new Insets(10,0,0,0));
        day3.getChildren().add(dayLabel3);
        Label dayLabel4 = new Label(today.plusDays(3).getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.getDefault()));
        dayLabel4.setFont(Font.font("Trebuchet MS", FontWeight.BOLD, 24));
        dayLabel4.setPadding(new Insets(10,0,0,0));
        day4.getChildren().add(dayLabel4);
        Label dayLabel5 = new Label(today.plusDays(4).getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.getDefault()));
        dayLabel5.setFont(Font.font("Trebuchet MS", FontWeight.BOLD, 24));
        dayLabel5.setPadding(new Insets(10,0,0,0));
        day5.getChildren().add(dayLabel5);


        for(Event event: HelloApplication.sessionUser.getThisWeekEvents()) {
            if(event.getDate().isEqual(today) && day1.getChildren().size() <= 4) {
                EventDisplay eventDisplay = new EventDisplay(event);
                day1.getChildren().add(eventDisplay);
                VBox.setMargin(eventDisplay, new Insets(5, 0, 0, 0));
            } else if (event.getDate().isEqual(today.plusDays(1)) && day2.getChildren().size() <= 4) {
                EventDisplay eventDisplay = new EventDisplay(event);
                day2.getChildren().add(eventDisplay);
                VBox.setMargin(eventDisplay, new Insets(5, 0, 0, 0));
            }
            else if (event.getDate().isEqual(today.plusDays(2)) && day3.getChildren().size() <= 4) {
                EventDisplay eventDisplay = new EventDisplay(event);
                day3.getChildren().add(eventDisplay);
                VBox.setMargin(eventDisplay, new Insets(5, 0, 0, 0));
            }else if (event.getDate().isEqual(today.plusDays(3)) && day4.getChildren().size() <= 4) {
                EventDisplay eventDisplay = new EventDisplay(event);
                day4.getChildren().add(eventDisplay);
                VBox.setMargin(eventDisplay, new Insets(5, 0, 0, 0));
            }
            else if (event.getDate().isEqual(today.plusDays(4)) && day5.getChildren().size() <= 4) {
                EventDisplay eventDisplay = new EventDisplay(event);
                day5.getChildren().add(eventDisplay);
                VBox.setMargin(eventDisplay, new Insets(5, 0, 0, 0));
            }

        }
    }
    public void onVBoxHover(MouseEvent mouseEvent) {
        DropShadow dropShadow = new DropShadow();
        dropShadow.setRadius(20);
        dropShadow.setOffsetX(0);
        dropShadow.setOffsetY(0);
        dropShadow.setColor(Color.rgb(0, 0, 0, 0.5));
        ((VBox)mouseEvent.getSource()).setEffect(dropShadow);
    }
    public void exitVBoxHover(MouseEvent mouseEvent) {

        DropShadow dropShadow = new DropShadow();
        dropShadow.setRadius(20);
        dropShadow.setOffsetX(0);
        dropShadow.setOffsetY(0);
        dropShadow.setColor(Color.rgb(0, 0, 0, 0));
        ((VBox)mouseEvent.getSource()).setEffect(dropShadow);
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        displayEvents();
        displayFriends();
        createCommunityButtons();
        profileName.setText(HelloApplication.sessionUser.getUsername());
        profilePicture.setImage(HelloApplication.sessionUser.getPhoto());
    }
}
