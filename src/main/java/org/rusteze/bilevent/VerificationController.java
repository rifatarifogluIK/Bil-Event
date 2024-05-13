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
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class VerificationController implements Initializable {

    @FXML
    TextField codeField;
    @FXML
    Label warningLabel;
    private static String sentCode;
    private static User user;
    private static String userEmail;

    public void sendEmail() {

        final String username = "bilevent.noreply@gmail.com";
        final String password = "ewycmttzehcmlkdz";

        Random random = new Random();

        String code = String.valueOf(random.nextInt(89999) + 10000);
        sentCode = code;

        Properties prop = new Properties();
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(prop,
                new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {

            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress("bilevent.noreply@gmail.com"));
            message.setRecipients(
                    MimeMessage.RecipientType.TO,
                    InternetAddress.parse(userEmail)
            );
            message.setSubject("Verification Code");
            message.setText("Verification Code: " + code);

            Transport.send(message);

        } catch (MessagingException e) {
            e.printStackTrace();
        }

    }
    public void submitBtn(ActionEvent event) {
        if(codeField.getText().equals(sentCode)) {
            Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
            try {
                PasswordController.setUser(user);
                Parent root = FXMLLoader.load(getClass().getResource("PasswordReset.fxml"));
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
        VerificationController.user = user;
    }

    public static void setUserEmail(String userEmail) {
        VerificationController.userEmail = userEmail;
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
    public void initialize(URL url, ResourceBundle resourceBundle) {
        warningLabel.setVisible(false);
        sendEmail();
    }
}
