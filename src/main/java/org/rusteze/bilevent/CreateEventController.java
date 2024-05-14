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
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
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
    ChoiceBox<String> eventLocation;

    public static String[] locations = {"A Building", "B Building", "C Building", "V1", "Odeon", "MayFest", "EE-102"};
    public static String[] attributeList = {"Sport", "Music", "Art", "Computer Science", "Game", "Festival"};
    public static Community creatorCommunity;
    CheckBox[] attributes;
    @FXML
    ImageView imageView;
    @FXML
    Label warningMessage;
    @FXML
    HBox eventAttributes;

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
    public void setEventAttributes() {

        attributes = new CheckBox[attributeList.length];
        for(int i = 0; i < attributeList.length; i++) {
            attributes[i] = new CheckBox(attributeList[i]);
            eventAttributes.getChildren().add(attributes[i]);
            HBox.setMargin(attributes[i], new Insets(0,8,0,0));
        }
    }

    public void initializeEvent(ActionEvent event) {

        if(!eventName.getText().isBlank() && !eventDesc.getText().isBlank() && eventLocation.getValue() != null && eventDate.getValue() != null) {

            String name = eventName.getText();
            String description = eventDesc.getText();
            String location = eventLocation.getValue();
            LocalDate date = eventDate.getValue();
            Image image = imageView.getImage();
            Event createdEvent;

            if(creatorCommunity == null) {
                createdEvent = HelloApplication.sessionUser.createEvent(name, description, location, date, image);
            } else {
                createdEvent = creatorCommunity.createEvent(name,description,location,date,image);
            }

            for(CheckBox box : attributes) {
                if(box.isSelected()) {
                    createdEvent.addAttribute(box.getText());
                }
            }

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

    public static void setCreatorCommunity(Community creatorCommunity) {
        CreateEventController.creatorCommunity = creatorCommunity;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        eventLocation.getItems().clear();
        eventLocation.getItems().addAll(locations);
        setEventAttributes();
        warningMessage.setVisible(false);
    }
}
