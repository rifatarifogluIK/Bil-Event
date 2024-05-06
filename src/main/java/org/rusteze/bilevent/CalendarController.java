package org.rusteze.bilevent;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
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
    LocalDate weekStart;

    private class EventDisplay extends Pane {
        private Event event;

        public EventDisplay(Event event) {
            this.event = event;
            this.setOnMouseClicked(this::onClick);
            HBox innerContainer = new HBox();
            ImageView image = new ImageView(event.getPhoto());
            image.setFitWidth(12);
            image.setFitHeight(12);
            Label eventName = new Label(event.getName());
            eventName.setFont(Font.font("Trebuchet MS", 10));
            eventName.setStyle("-fx-font-weight: bold;");
            innerContainer.getChildren().add(image);
            innerContainer.getChildren().add(eventName);
            getChildren().add(innerContainer);
            innerContainer.setLayoutX(10);
        }

        public void onClick(MouseEvent mouseEvent) {
            Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
            try {
                EventPageController.setEvent(this.event);
                Parent root = FXMLLoader.load(getClass().getResource("eventPage.fxml"));
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

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

        ArrayList<Event> recommendations = new ArrayList<Event>();
        recommendations.add(new PersonalEvent(HelloApplication.sessionUser, " MayFest", "aaa", LocalDate.of(2024, 5, 6)));
        recommendations.add(new PersonalEvent(HelloApplication.sessionUser, " Concert", "aaa", LocalDate.of(2024, 5, 6)));
        recommendations.add(new PersonalEvent(HelloApplication.sessionUser, " F1 Race", "aaa", LocalDate.of(2024, 5, 7)));
        recommendations.add(new PersonalEvent(HelloApplication.sessionUser, " deneme4", "aaa", LocalDate.of(2024, 5, 9)));
        recommendations.add(new PersonalEvent(HelloApplication.sessionUser, " deneme4", "aaa", LocalDate.of(2024, 5, 10)));
        recommendations.add(new PersonalEvent(HelloApplication.sessionUser, " deneme4", "aaa", LocalDate.of(2024, 5, 11)));
        recommendations.add(new PersonalEvent(HelloApplication.sessionUser, " deneme4", "aaa", LocalDate.of(2024, 5, 12)));
        recommendations.add(new PersonalEvent(HelloApplication.sessionUser, " deneme4", "aaa", LocalDate.of(2024, 5, 15)));

        for (Event e : recommendations) { //TODO HelloApplication.sessionUser.getEnrolledEvents()

            LocalDate eventLocalDate = e.getDate();
            int eventDate = eventLocalDate.getDayOfMonth();

            if (eventLocalDate.isAfter(weekStart.minusDays(1)) && eventLocalDate.isBefore(weekStart.plusDays(7))) {
                EventDisplay eventDisplay = new EventDisplay(e);
                if (eventDate == dateOfMonday) {
                    monday.getChildren().add(eventDisplay);
                } else if (eventDate == dateOfMonday + 1) {
                    tuesday.getChildren().add(eventDisplay);
                } else if (eventDate == dateOfMonday + 2) {
                    wednesday.getChildren().add(eventDisplay);
                } else if (eventDate == dateOfMonday + 3) {
                    thursday.getChildren().add(eventDisplay);
                } else if (eventDate == dateOfMonday + 4) {
                    friday.getChildren().add(eventDisplay);
                } else if (eventDate == dateOfMonday + 5) {
                    saturday.getChildren().add(eventDisplay);
                } else if (eventDate == dateOfMonday + 6) {
                    sunday.getChildren().add(eventDisplay);
                }
                VBox.setMargin(eventDisplay, new Insets(5, 0, 0, 0));
            }
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
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("CreateCommunity.fxml"));
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

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
        setWeek();
        createCommunityButtons();
        setPage();
    }
}
