package org.rusteze.bilevent;

import javafx.event.ActionEvent;
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
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.DayOfWeek;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;

public class CalendarController implements SceneHandler, Initializable {

    @FXML
    private VBox buttonPanel;
    @FXML
    ImageView nextWeekBtn;
    @FXML
    ImageView prevWeekBtn;
    @FXML
    Label weekLabel;
    @FXML
    VBox monday;
    @FXML
    VBox tuesday;
    @FXML
    VBox wednesday;
    @FXML
    VBox thursday;
    @FXML
    VBox friday;
    @FXML
    VBox saturday;
    @FXML
    VBox sunday;
    @FXML
    VBox friendPanel;
    LocalDate weekStart;


    public void nextWeek(MouseEvent mouseEvent) {
        weekStart = weekStart.plusDays(7);
        clearWeek();
        setPage();
    }

    public void prevWeek(MouseEvent mouseEvent) {
        weekStart = weekStart.minusDays(7);
        clearWeek();
        setPage();
    }
    public void clearWeek() {
        monday.getChildren().subList(1, monday.getChildren().size()).clear();
        tuesday.getChildren().subList(1, tuesday.getChildren().size()).clear();
        wednesday.getChildren().subList(1, wednesday.getChildren().size()).clear();
        thursday.getChildren().subList(1, thursday.getChildren().size()).clear();
        friday.getChildren().subList(1, friday.getChildren().size()).clear();
        saturday.getChildren().subList(1, saturday.getChildren().size()).clear();
        sunday.getChildren().subList(1, sunday.getChildren().size()).clear();
    }
    public void setWeek() {
        LocalDate currentDate = LocalDate.now();
        LocalDate mostRecentMonday = currentDate.with(DayOfWeek.MONDAY);
        this.weekStart = mostRecentMonday;
    }

    public void setPage() {

        int dateOfMonday = weekStart.getDayOfMonth();
        int dateOfSunday = dateOfMonday + 6;
        String month = weekStart.getMonth().getDisplayName(TextStyle.FULL, Locale.getDefault());
        weekLabel.setText(dateOfMonday + " - " + weekStart.plusDays(6).getDayOfMonth() + " " + month);

        ArrayList<Event> displayedEvents = HelloApplication.sessionUser.getEnrolledEvents();

        for (Event e : displayedEvents) { //TODO HelloApplication.sessionUser.getEnrolledEvents()

            LocalDate eventLocalDate = e.getDate();
            int eventDate = eventLocalDate.getDayOfMonth();

            if (eventLocalDate.isAfter(weekStart.minusDays(1)) && eventLocalDate.isBefore(weekStart.plusDays(7))) {
                EventDisplay eventDisplay = new EventDisplay(e);
                if (eventDate == dateOfMonday) {
                    if(monday.getChildren().size() <= 4) {
                        monday.getChildren().add(eventDisplay);
                    }
                } else if (eventDate == dateOfMonday + 1) {
                    if(tuesday.getChildren().size() <= 4) {
                        tuesday.getChildren().add(eventDisplay);
                    }
                } else if (eventDate == dateOfMonday + 2) {
                    if(wednesday.getChildren().size() <= 4) {
                        wednesday.getChildren().add(eventDisplay);
                    }
                } else if (eventDate == dateOfMonday + 3) {
                    if(thursday.getChildren().size() <= 4) {
                        thursday.getChildren().add(eventDisplay);
                    }
                } else if (eventDate == dateOfMonday + 4) {
                    if(friday.getChildren().size() <= 4) {
                        friday.getChildren().add(eventDisplay);
                    }
                } else if (eventDate == dateOfMonday + 5) {
                    if(saturday.getChildren().size() <= 4) {
                        saturday.getChildren().add(eventDisplay);
                    }
                } else if (eventDate == dateOfMonday + 6) {
                    if(sunday.getChildren().size() <= 4) {
                        sunday.getChildren().add(eventDisplay);
                    }
                }
                VBox.setMargin(eventDisplay, new Insets(5, 0, 0, 0));
            }
        }
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
        ArrayList<Community> communities = HelloApplication.sessionUser.getCommunities();

        int buttonCount = communities.size();

        for (int i = 0; i < buttonCount; i++) {
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
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setWeek();
        createCommunityButtons();
        displayFriends();
        setPage();
    }
}
