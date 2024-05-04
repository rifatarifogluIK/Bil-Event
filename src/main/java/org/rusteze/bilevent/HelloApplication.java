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
        String uri = "mongodb://demo:123@139.179.217.206:27017/";
        ServerApi serverApi = ServerApi.builder()
                .version(ServerApiVersion.V1)
                .build();
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(uri))
                .serverApi(serverApi)
                .build();
        try (MongoClient client = MongoClients.create(settings)){
            MongoDatabase db = client.getDatabase("demo");
            MongoCollection collectionTest = db.getCollection("test");
            Document doc = (Document)collectionTest.find().first();
            System.out.println(doc.toJson());
        }catch (Exception e){
            System.out.println("Can not connect");
        }
        sessionUser = new User("Selim", "pass", "email");
        sessionUser.createCommunity("F1");
        sessionUser.createCommunity("F2");
        sessionUser.createCommunity("F3");
        sessionUser.createCommunity("F4");
        sessionUser.createCommunity("F5");
        sessionUser.createCommunity("F6");
        sessionUser.createCommunity("F7");
        sessionUser.createCommunity("F8");

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