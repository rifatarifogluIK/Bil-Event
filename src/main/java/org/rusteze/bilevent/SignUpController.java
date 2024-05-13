package org.rusteze.bilevent;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class SignUpController implements Initializable{

    @FXML
    private Button swapToLogIn;
    @FXML
    private AnchorPane slidingPane1;
    @FXML
    private Label warningLabel;
    @FXML
    private TextField nameField;
    @FXML
    private TextField mailField;
    @FXML
    private PasswordField passField1;
    @FXML
    private PasswordField passField2;

    private Stage stage;
    private Parent root;

    public void signUpButton(ActionEvent event) throws IOException {

        if(!nameField.getText().isBlank() && !mailField.getText().isBlank() && !passField1.getText().isBlank() && !passField2.getText().isBlank()) {
            if(passField1.getText().equals(passField2.getText())) {
                String userName = nameField.getText();
                String email = mailField.getText();
                String password = passField1.getText();

                //TODO database stuff
                User user = new User(userName, password, email);
                clickAccountButton(event);
            } else {
                warningLabel.setText("Passwords does not match!");
                warningLabel.setVisible(true);
            }
        } else {
            warningLabel.setText("You have one or more fields empty!");
            warningLabel.setVisible(true);
        }
    }

    @FXML
    public void clickAccountButton(ActionEvent event) throws IOException {

        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        TranslateTransition transition = new TranslateTransition();
        transition.setDuration(Duration.seconds(1));
        
        transition.setToX(-350);
        transition.setNode(slidingPane1);
        transition.play();
        transition.setOnFinished(e -> {
            try {
                endOfTransition();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
    }
    public void endOfTransition() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("LogIn.fxml"));
        stage.setScene(new Scene(root));
        stage.show();
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
    public void initialize(URL location, ResourceBundle resources) {
        warningLabel.setVisible(false);
    }
}
