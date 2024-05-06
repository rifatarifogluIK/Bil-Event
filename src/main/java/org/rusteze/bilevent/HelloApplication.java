package org.rusteze.bilevent;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import com.mongodb.*;
import com.mongodb.client.*;
import org.bson.*;
import org.bson.types.ObjectId;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

public class HelloApplication extends Application {

    public static int POPULAR_LIMIT = 5;

    // Added this to test UI before implementing user authentication
    public static User sessionUser;

    @Override
    public void start(Stage stage) throws IOException{
        String uri = "mongodb://bil:event@139.179.217.206:27017/";
        MongoDatabase db;
        ServerApi serverApi = ServerApi.builder()
                .version(ServerApiVersion.V1)
                .build();
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(uri))
                .serverApi(serverApi)
                .build();
        try (MongoClient client = MongoClients.create(settings)){
            db = client.getDatabase("bil_event");
        }catch (Exception e){
            System.out.println("Can not connect");
            return;
        }

        //update(db);

        sessionUser = new User("Selim", "pass", "email");
        /*Community.popularCommunities.add(new Community("CS Department", null));
        Community.popularCommunities.add(new Community("EEE Department", null));
        Community.popularCommunities.add(new Community("IE Department", null));
        Community.popularCommunities.add(new Community("F1 Club", null));
        Community.popularCommunities.add(new Community("ME Department", null));
        Community.popularCommunities.add(new Community("ME Department", null));
        Community.popularCommunities.add(new Community("ME Department", null));*/

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("LogIn.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Bil-Event");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    public static void update(MongoDatabase db) throws FileNotFoundException {
        MongoCollection<Document> events = db.getCollection("Event");
        MongoCollection<Document> communities = db.getCollection("Community");
        MongoCollection<Document> users = db.getCollection("User");

        List<Document> eventList = events.find().into(new ArrayList<>());
        List<Document> communityList = communities.find().into(new ArrayList<>());
        List<Document> userList = users.find().into(new ArrayList<>());

        for(Document doc : eventList){
            if(Event.allEvents.get((ObjectId)doc.get("_id")) == null){
                if((boolean)doc.get("isPersonal")){
                    Event.allEvents.put((ObjectId)doc.get("_id"), new PersonalEvent(doc));
                }else {
                    Event.allEvents.put((ObjectId)doc.get("_id"), new CommunityEvent(doc));
                }
            }
        }

        for(Document doc : communityList){
            if(Community.allCommunities.get((ObjectId)doc.get("_id")) == null){
                Community.allCommunities.put((ObjectId)doc.get("_id"), new Community(doc));
            }
        }
        Enumeration<Community> allCommunities = Community.allCommunities.elements();
        while(allCommunities.hasMoreElements()){
            Community temp = allCommunities.nextElement();
            if(temp.getMembers().size() >= POPULAR_LIMIT){
                Community.popularCommunities.put(temp.getId(), temp);
            }
        }

        for(Document doc : userList){
            if(User.allUsers.get((ObjectId)doc.get("_id")) == null){
                User.allUsers.put((ObjectId)doc.get("_id"), new User(doc));
            }
        }
    }
}