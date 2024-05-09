package org.rusteze.bilevent;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.awt.image.BufferedImage;
import javafx.embed.swing.SwingFXUtils;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class CreateEventController implements Initializable {

    @FXML
    TextField eventName;
    @FXML
    TextField eventDesc;
    @FXML
    DatePicker eventDate;
    @FXML
    TextField eventLocation;
    @FXML
    ImageView imageView;
    @FXML
    Label warningMessage;

    public void uploadBtn(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif")
        );

        File selectedFile = fileChooser.showOpenDialog(((Node)event.getSource()).getScene().getWindow());
        if (selectedFile != null) {
            Image image = new Image(selectedFile.toURI().toString());
            imageView.setImage(image);
        }
    }

    public void initializeEvent(ActionEvent event) {

        if(!eventName.getText().isBlank() && !eventDesc.getText().isBlank() && !eventLocation.getText().isBlank() && eventDate.getValue() != null) {
            String name = eventName.getText();
            String description = eventDesc.getText();
            String location = eventLocation.getText();
            LocalDate date = eventDate.getValue();
            Image image = imageView.getImage();

            Event createdEvent = HelloApplication.sessionUser.createEvent(name, description, location, date, image);

            LocalDateTime localDateTime = LocalDateTime.now();
            String formattedDateTime = localDateTime.format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            File saveFile = new File("src/main/resources/org/rusteze/bilevent/ImageDB", HelloApplication.sessionUser.getUsername() + "_" + formattedDateTime + ".png");

            Image fxImage = imageView.getImage();
            BufferedImage bImage = SwingFXUtils.fromFXImage(fxImage, null);
            try {
                ImageIO.write(bImage, "png", saveFile);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            
            try {
                EventPageController.setEvent(createdEvent);
                Parent root = FXMLLoader.load(getClass().getResource("eventpage.fxml"));
                Scene scene = ((Node) event.getSource()).getScene();
                scene.setRoot(root);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            warningMessage.setVisible(true);
        }

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
    public void onHoverRed(MouseEvent mouseEvent) {
        ((Button)mouseEvent.getSource()).setStyle("-fx-background-color: #e00000; -fx-background-radius: 15px");
        ((Button)mouseEvent.getSource()).setCursor(Cursor.HAND);
    }
    public void exitHoverRed(MouseEvent mouseEvent) {
        ((Button)mouseEvent.getSource()).setStyle("-fx-background-color: #ff0000; -fx-background-radius: 12px");
        ((Button)mouseEvent.getSource()).setCursor(Cursor.DEFAULT);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        warningMessage.setVisible(false);
    }
}
