package org.rusteze.bilevent;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class PasswordController implements Initializable {

    private static User user;
    @FXML
    TextField passwordField1;
    @FXML
    TextField passwordField2;
    @FXML
    Label warningLabel;

    public void submitBtn(ActionEvent event) {
        if(passwordField1.getText().equals(passwordField2.getText())) {

            user.setPassword(passwordField1.getText());
            Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
            try {
                Parent root = FXMLLoader.load(getClass().getResource("LogIn.fxml"));
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            warningLabel.setVisible(true);
        }
    }

    public void logInBtn(ActionEvent event) {
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("LogIn.fxml"));
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void setUser(User user) {
        PasswordController.user = user;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        warningLabel.setVisible(false);
    }
}
