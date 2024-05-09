package org.rusteze.bilevent;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
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
import java.time.LocalDate;

public class CreateEventController {

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
        String name = eventName.getText();
        String description = eventDesc.getText();
        String location = eventLocation.getText();
        LocalDate date = eventDate.getValue();
        Image image = imageView.getImage();

        HelloApplication.sessionUser.createEvent(name, description, location, date, image);

        File saveFile = new File("src/main/resources/org/rusteze/bilevent/ImageDB", name + "Photo.png");
        Image fxImage = imageView.getImage();
        BufferedImage bImage = SwingFXUtils.fromFXImage(fxImage, null);
        try {
            ImageIO.write(bImage, "png", saveFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("homepage.fxml"));
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void backBtn(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("homepage.fxml"));
            stage.setScene(new Scene(root));
            stage.show();
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
}
