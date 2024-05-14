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
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.Node;

public class LogInController implements Initializable {

    @FXML
    private Button accountButton;
    @FXML
    private AnchorPane slidingPane;
    @FXML
    private TextField emailField;
    @FXML
    private TextField passwordField;
    @FXML
    private Label warningLabel;
    private Stage stage;

    @FXML
    public void clickAccountButton(ActionEvent event) throws IOException {

        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        TranslateTransition transition = new TranslateTransition();
        transition.setDuration(Duration.seconds(1));

        transition.setToX(350);
        transition.setNode(slidingPane);
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
        Parent root = FXMLLoader.load(getClass().getResource("SignUp.fxml"));
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void logIn(ActionEvent event) {
        //TODO authentication
        if (User.userWith(emailField.getText()) != null && User.userWith(emailField.getText()).getPassword().equals(passwordField.getText())) {
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            try {
                HelloApplication.sessionUser = User.userWith(emailField.getText());
                Parent root = FXMLLoader.load(getClass().getResource("homepage.fxml"));
                stage.setScene(new Scene(root));
                stage.show();
                stage.setFullScreen(true);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            warningLabel.setText("Wrong Username or Password!");
            warningLabel.setVisible(true);
        }
    }

    public void forgotPassword(ActionEvent event) {
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        //TODO Rather than looking if it is blank check if email is assigned to an user.
        if (!emailField.getText().isBlank()) {
            try {
                VerificationController.setUserEmail(emailField.getText());
                VerificationController.setUser(User.userWith(emailField.getText()));
                Parent root = FXMLLoader.load(getClass().getResource("Verification.fxml"));
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        } else {
            warningLabel.setText("Please Enter Your E-Mail to Reset Your Password!");
            warningLabel.setVisible(true);
        }
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
    public void initialize(URL arg0, ResourceBundle arg1) {
        warningLabel.setVisible(false);
    }

}