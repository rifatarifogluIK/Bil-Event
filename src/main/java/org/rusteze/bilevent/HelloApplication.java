package org.rusteze.bilevent;

import com.mongodb.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import com.mongodb.client.*;
import org.bson.*;
import java.io.IOException;
import java.util.ArrayList;

public class HelloApplication extends Application {

    // Added this to test UI before implementing user authentication
    public static User sessionUser;
    public static ArrayList<Community> popularCommunities = new ArrayList<Community>();

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
        popularCommunities.add(new Community("CS Department", null));
        popularCommunities.add(new Community("EEE Department", null));
        popularCommunities.add(new Community("IE Department", null));
        popularCommunities.add(new Community("F1 Club", null));
        popularCommunities.add(new Community("ME Department", null));
        popularCommunities.add(new Community("ME Department", null));
        popularCommunities.add(new Community("ME Department", null));
        
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