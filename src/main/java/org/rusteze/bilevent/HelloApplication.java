package org.rusteze.bilevent;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import com.mongodb.*;
import com.mongodb.client.*;
import org.bson.*;
import org.bson.types.ObjectId;

import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

public class HelloApplication extends Application {

    public static int POPULAR_LIMIT = 3;

    // Added this to test UI before implementing user authentication
    public static User sessionUser;
    public static MongoDatabase db;

    @Override
    public void start(Stage stage) throws IOException{
        String uri = "mongodb://bil:event@139.179.217.206:27017/?authSource=bil_event";
        ServerApi serverApi = ServerApi.builder()
                .version(ServerApiVersion.V1)
                .build();
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(uri))
                .serverApi(serverApi)
                .build();
        MongoClient client = MongoClients.create(settings);
        db = client.getDatabase("bil_event");

        update();
//        ActionListener actionListener = new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                try {
//                    update();
//                } catch (FileNotFoundException ex) {
//                    throw new RuntimeException(ex);
//                }
//            }
//        };
//        Timer timer = new Timer(3000, actionListener);
//        timer.start();

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("LogIn.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Bil-Event");
        stage.setScene(scene);
        File file = new File("src/main/resources/org/rusteze/bilevent/Images/Logo.PNG");
        Image icon = new Image(file.toURI().toString());
        stage.getIcons().add(icon);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    public static void update() throws FileNotFoundException {
        ObjectId userId = null;
        if(sessionUser != null) {
            userId = sessionUser.getId();
        }
        User.allUsers = new Hashtable<>();
        Event.allEvents = new Hashtable<>();
        Community.allCommunities = new Hashtable<>();
        Community.popularCommunities = new Hashtable<>();
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
                Community.allCommunities.put((ObjectId)doc.get("_id"), new Community().fromDocument(doc));
            }
        }

        for(Document doc : userList){
            if(User.allUsers.get((ObjectId)doc.get("_id")) == null){
                User.allUsers.put((ObjectId)doc.get("_id"), new User().fromDocument(doc));
            }
        }

        Enumeration<ObjectId> communityKeys = Community.allCommunities.keys();
        while (communityKeys.hasMoreElements()){
            ObjectId key = communityKeys.nextElement();
            Community com = Community.allCommunities.get(key);
            Document doc = communities.find(new Document("_id", key)).first();

            if (doc.get("members") != null) {
                ((ArrayList<ObjectId>)doc.get("members")).forEach(e -> com.getMembers().add(User.allUsers.get(e)));
            }
            if (doc.get("currentEvents") != null) {
                ((ArrayList<ObjectId>)doc.get("currentEvents")).forEach(e -> com.getCurrentEvents().add(Event.allEvents.get(e)));
            }
            if (doc.get("pastEvents") != null) {
                ((ArrayList<ObjectId>)doc.get("pastEvents")).forEach(e -> com.getPastEvents().add(Event.allEvents.get(e)));
            }
            com.setAdmin(User.allUsers.get(doc.get("admin")));
        }

        Enumeration<ObjectId> eventKeys = Event.allEvents.keys();
        while (eventKeys.hasMoreElements()){
            ObjectId key = eventKeys.nextElement();
            Event event = Event.allEvents.get(key);
            Document doc = events.find(new Document("_id", key)).first();

            if (doc.get("attendees") != null) {
                ((ArrayList<ObjectId>)doc.get("attendees")).forEach(e -> event.getAttendees().add(User.allUsers.get(e)));
            }
            if (doc.get("attributes") != null) {
                ((ArrayList<String>)doc.get("attributes")).forEach(e -> event.getAttributes().add(e));
            }
            event.setAdmin(User.allUsers.get(doc.get("admin")));
        }

        Enumeration<ObjectId> userKeys = User.allUsers.keys();
        while (userKeys.hasMoreElements()){
            ObjectId key = userKeys.nextElement();
            User user = User.allUsers.get(key);
            Document doc = users.find(new Document("_id", key)).first();

            if(doc.get("communities") != null){
                ((ArrayList<ObjectId>)doc.get("communities")).forEach(e -> user.getCommunities().add(Community.allCommunities.get(e)));
            }
            if(doc.get("enrolledEvents") != null){
                ((ArrayList<ObjectId>)doc.get("enrolledEvents")).forEach(e -> user.getEnrolledEvents().add(Event.allEvents.get(e)));
            }
            if(doc.get("attendedEvents") != null){
                ((ArrayList<ObjectId>)doc.get("attendedEvents")).forEach(e -> user.getAttendedEvents().add(Event.allEvents.get(e)));
            }
            if(doc.get("createdEvents") != null){
                ((ArrayList<ObjectId>)doc.get("createdEvents")).forEach(e -> user.getCreatedEvents().add(Event.allEvents.get(e)));
            }
            if(doc.get("friends") != null){
                ((ArrayList<ObjectId>)doc.get("friends")).forEach(e -> user.getFriends().add(User.allUsers.get(e)));
            }
        }

        Enumeration<Community> allCommunities = Community.allCommunities.elements();
        while(allCommunities.hasMoreElements()){
            Community temp = allCommunities.nextElement();
            if(temp.getMembers().size() >= POPULAR_LIMIT){
                Community.popularCommunities.put(temp.getId(), temp);
            }
        }


        Enumeration<ObjectId> allEventsKeys = Event.allEvents.keys();
        while(allEventsKeys.hasMoreElements()){
            ObjectId key = allEventsKeys.nextElement();
            Event temp = Event.allEvents.get(key);
            if(temp instanceof CommunityEvent){
                ((CommunityEvent) temp).setCommunity(Community.allCommunities.get(events.find(new Document("_id", key)).first().get("community")));
            }else if(temp instanceof PersonalEvent){
                ((PersonalEvent) temp).setOrganizer(User.allUsers.get(events.find(new Document("_id", key)).first().get("organizer")));
            }
        }

        Enumeration<User> allUsers = User.allUsers.elements();
        while(allUsers.hasMoreElements()){
            User temp = allUsers.nextElement();
            temp.setRecommendations(new Recommendation(temp));
        }

        if(userId != null) {
            sessionUser = User.allUsers.get(userId);
        }
    }

    public static User getSessionUserName() {
        return sessionUser;
    }
}