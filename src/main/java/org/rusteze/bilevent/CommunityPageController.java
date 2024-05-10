package org.rusteze.bilevent;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.scene.control.Label;

public class CommunityPageController implements Initializable {
    @FXML
    private Label departmentLabel;
    @FXML
    private Label departmentLabel1;
    @FXML
    private ImageView photo;
    @FXML
    private Button editBtn;
    @FXML
    private Button createBtn;
    @FXML
    private Button joinBtn;
    @FXML
    private HBox eventPanel;
    @FXML
    private VBox memberPanel;

    private static Community community;

    public void displayEvents() {
        ArrayList<Event> currentEvents = community.getCurrentEvents();
        for(Event e : currentEvents) {
            EventPane eventPane = new EventPane(e);
            eventPanel.getChildren().add(eventPane);
            HBox.setMargin(eventPane, new Insets(100, 220, 0, 0));
            eventPanel.setAlignment(Pos.BASELINE_LEFT);
        }
    }
    public void displayMembers() {
        ArrayList<User> members = community.getMembers();
        members.add(new User("Rıfat", "Ruhi123", "rıfat@mail.com"));
        members.add(new User("Selim", "Ruhi1234", "selim@mail.com"));
        members.add(new User("Berkant", "Ruhi12345", "berkant@mail.com"));
        members.add(new User("Devran", "Ruhi123456", "devran@mail.com"));

        for(User member : members) {
            UserPane userPane = new UserPane(member);
            memberPanel.getChildren().add(userPane);
            VBox.setMargin(userPane, new Insets(0,0,10,0));
        }
    }

    public void setPage() {
        departmentLabel1.setText("Current Events Of " + community.getName());
        departmentLabel.setText(community.getName());
        photo.setImage(community.getPhoto());

        if(!community.isAdmin(HelloApplication.sessionUser)) {
            editBtn.setVisible(false);
            createBtn.setVisible(false);
        }
        if(community.getMembers().contains(HelloApplication.sessionUser)) {
            joinBtn.setText("Leave");
        } else {
            joinBtn.setText("Join");
        }
        displayEvents();
        displayMembers();
    }
    public void joinBtn(ActionEvent event) {
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        confirmation.initOwner(stage);
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

        try {
            Parent root = FXMLLoader.load(getClass().getResource("homepage.fxml"));
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
    public void onHoverRed(MouseEvent mouseEvent) {
        ((Button)mouseEvent.getSource()).setStyle("-fx-background-color: #e00000; -fx-background-radius: 15px");
        ((Button)mouseEvent.getSource()).setCursor(Cursor.HAND);
    }
    public void exitHoverRed(MouseEvent mouseEvent) {
        ((Button)mouseEvent.getSource()).setStyle("-fx-background-color: #ff0000; -fx-background-radius: 12px");
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
