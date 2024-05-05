package org.rusteze.bilevent;

import com.mongodb.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import com.mongodb.client.*;
import org.bson.*;

import java.io.IOException;

public class HelloApplication extends Application {

    // Added this to test UI before implementing user authentication
    public static User sessionUser;

    @Override
    public void start(Stage stage) throws IOException {
        String uri = "mongodb://bil:event@139.179.217.206:27017/";
        ServerApi serverApi = ServerApi.builder()
                .version(ServerApiVersion.V1)
                .build();
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(uri))
                .serverApi(serverApi)
                .build();
        try (MongoClient client = MongoClients.create(settings)){
            MongoDatabase db = client.getDatabase("bil_event");
        }catch (Exception e){
            System.out.println("Can not connect");
        }
        sessionUser = new User("Selim", "pass", "email");
        sessionUser.createCommunity("F1", null);
        sessionUser.createCommunity("F2", null);
        sessionUser.createCommunity("F3", null);
        sessionUser.createCommunity("F4", null);
        sessionUser.createCommunity("F5", null);
        sessionUser.createCommunity("F6", null);
        sessionUser.createCommunity("F7", null);
        sessionUser.createCommunity("F8", null);

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("LogIn.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Bil-Event");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}