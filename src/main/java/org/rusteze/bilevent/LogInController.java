package org.rusteze.bilevent;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.Node;

public class LogInController implements Initializable {

    @FXML
    private Button accountButton;
    @FXML
    private AnchorPane slidingPane;
    
    private Stage stage;
    private Parent root;

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
    public void logIn(ActionEvent event) throws IOException {
        //TODO authentication
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("homepage.fxml"));
        stage.setScene(new Scene(root));
        stage.show();
    }
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {

    }

}