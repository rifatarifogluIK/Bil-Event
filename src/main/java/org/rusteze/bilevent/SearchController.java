package org.rusteze.bilevent;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;

public class SearchController implements SceneHandler, Initializable {

    @FXML
    private VBox buttonPanel;
    @FXML
    private VBox searchResults;
    @FXML
    private TextField nameSearch;
    @FXML
    private TextField organizerSearch;
    @FXML
    private DatePicker dateSearch;
    @FXML
    private ChoiceBox<String> eventLocation;
    @FXML
    private VBox friendPanel;

    private class UserContainer extends SearchContainer {
        private User user;

        public UserContainer(User user) {
            this.user = user;
            nameLabel.setText(user.getUsername());
            button2.setText("Profile");
            innerPane.setStyle("-fx-background-color: #FFEE93; -fx-background-radius: 8px");
            buttonPanel.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
        }
    }

    private class EventContainer extends SearchContainer {
        private Event event;

        public EventContainer(Event event) {
            this.event = event;
            nameLabel.setText(event.getName());
            nameLabel.setPrefWidth(200);
            button2.setText("Details");
            button2.setOnAction(this::detailsBtn);
            innerPane.setStyle("-fx-background-color: #B5DBFF; -fx-background-radius: 8px");
            buttonPanel.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
            VBox infoBox = new VBox();
            Label dateLabel = new Label("Date: " + event.getDate());
            Label organizerLabel = new Label("Organizer: " + event.getOrganizer());
            infoBox.getChildren().add(dateLabel);
            infoBox.getChildren().add(organizerLabel);
            infoBox.setAlignment(Pos.CENTER_RIGHT);
            dateLabel.setFont(Font.font("Trebuchet MS", FontWeight.BOLD,20));
            organizerLabel.setFont(Font.font("Trebuchet MS", FontWeight.BOLD, 20));
            buttonPanel.getChildren().add(infoBox);
            infoBox.setPrefWidth(500);
        }

        public void detailsBtn(ActionEvent event) {

            try {
                EventPageController.setEvent(this.event);
                Parent root = FXMLLoader.load(getClass().getResource("eventPage.fxml"));
                Scene scene = ((Node) event.getSource()).getScene();
                scene.setRoot(root);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public class CommunityContainer extends SearchContainer {

        private Community community;

        public CommunityContainer(Community community) {
            this.community = community;
            nameLabel.setText(community.getName());
            button2.setText("Details");
            button2.setOnAction(this::detailsBtn);
            buttonPanel.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
        }
        public void detailsBtn(ActionEvent event) {

            try {
                CommunityPageController.setCommunity(community);
                Parent root = FXMLLoader.load(getClass().getResource("departmentpage.fxml"));
                Scene scene = ((Node) event.getSource()).getScene();
                scene.setRoot(root);
            } catch (IOException e) {
                throw new RuntimeException(e);
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

    public void onHover(MouseEvent mouseEvent) {
        ((Button) mouseEvent.getSource()).setStyle("-fx-background-color: #61aef7; -fx-font-family: \"Trebuchet\"; -fx-font-size: 15px; -fx-background-radius: 18px");
        ((Button) mouseEvent.getSource()).setCursor(Cursor.HAND);
    }

    public void exitHover(MouseEvent mouseEvent) {
        ((Button) mouseEvent.getSource()).setStyle("-fx-background-color: #B5DBFF; -fx-font-family: \"Trebuchet\"; -fx-font-size: 15px; -fx-background-radius: 12px");
        ((Button) mouseEvent.getSource()).setCursor(Cursor.DEFAULT);
    }

    public void onHoverWhite(MouseEvent mouseEvent) {
        ((Button) mouseEvent.getSource()).setStyle("-fx-background-color: #dfdfdf; -fx-background-radius: 12px");
        ((Button) mouseEvent.getSource()).setCursor(Cursor.HAND);
    }

    public void exitHoverWhite(MouseEvent mouseEvent) {
        ((Button) mouseEvent.getSource()).setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 12px");
        ((Button) mouseEvent.getSource()).setCursor(Cursor.DEFAULT);
    }

    public void showResults(ActionEvent event) {

        searchResults.getChildren().clear();
        String name = nameSearch.getText();
        String organizer = organizerSearch.getText();
        LocalDate date = dateSearch.getValue();
        String location = eventLocation.getValue();

        ArrayList<Searchable> results = Searchable.search(name, organizer, location, date);

        for (Searchable result : results) {
            if (result instanceof Event) {
                EventContainer eventResult = new EventContainer((Event) result);
                searchResults.getChildren().add(eventResult);
                VBox.setMargin(eventResult, new Insets(0, 0, 40, 0));
            } else if (result instanceof Community) {
                CommunityContainer communityResult = new CommunityContainer((Community) result);
                searchResults.getChildren().add(communityResult);
                VBox.setMargin(communityResult, new Insets(0, 0, 40, 0));
            } else if (result instanceof User) {
                UserContainer userResult = new UserContainer((User) result);
                searchResults.getChildren().add(userResult);
                VBox.setMargin(userResult, new Insets(0, 0, 40, 0));
            }
        }
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
        eventLocation.getItems().clear();
        eventLocation.getItems().addAll(CreateEventController.locations);
        createCommunityButtons();
        displayFriends();
    }
}
