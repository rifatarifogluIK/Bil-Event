package org.rusteze.bilevent;

import com.mongodb.BasicDBList;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.mongodb.client.model.Updates;
import javafx.scene.image.Image;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;

public class Community implements Searchable, ConvertibleToDocument{

    public static Dictionary<ObjectId, Community> allCommunities = new Hashtable<>();
    public static Dictionary<ObjectId, Community> popularCommunities = new Hashtable<>();

    private String name;
    private String description;
    private ArrayList<User> members;
    private ArrayList<User> admins;
    private ArrayList<Event> currentEvents;
    private ArrayList<Event> pastEvents;
    private Image photo;
    private double rating;
    private int ratingCount;
    private ObjectId id;

    public Community(String name, String description, Image photo) {
        this.name = name;
        this.description = description;
        this.members = new ArrayList<>();
        this.admins = new ArrayList<>();
        this.currentEvents = new ArrayList<>();
        this.pastEvents = new ArrayList<>();
        this.photo = photo;
        if(photo == null) {
            File file = new File("src/main/resources/org/rusteze/bilevent/Images/Logo.PNG");
            Image emptyImage = new Image(file.toURI().toString());
            this.photo = emptyImage;
        }
        this.rating = 0.0;
        this.ratingCount = 0;
        this.id = ObjectId.get();
    }

    public Community(Document doc) throws FileNotFoundException {
        this.name = (String)doc.get("name");
        this.description = (String)doc.get("description");
        this.members = new ArrayList<>();
        this.admins = new ArrayList<>();
        this.currentEvents = new ArrayList<>();
        this.pastEvents = new ArrayList<>();
        this.photo = new Image(new FileInputStream((String)doc.get("photo")));
        this.rating = (double)doc.get("rating");
        this.ratingCount = (int)doc.get("ratingCount");
        this.id = (ObjectId)doc.get("_id");
    }

    public void addMember(User user){
        members.add(user);
    }

    public void setAdmin(User user){
        if(members.contains(user)){
            admins.add(user);
        }
    }

    public Event createEvent(String name, String description, String location, LocalDate date, Image image){
        Event event = new CommunityEvent(this, name, description, location, date, image);
        admins.forEach(admin -> event.getAdmins().add(admin));
        currentEvents.add(event);
        return event;
    }

    public void handlePassedEvent(){
        boolean stop = false;

        for(int i = 0; i < currentEvents.size() && !stop; i++){
            Event current = currentEvents.get(i);
            if(current.getDate().isBefore(LocalDate.now())){
                currentEvents.remove(current);
                pastEvents.add(current);
                i--;
            }else{
                stop = true;
            }
        }
    }

    public void addNewRating(int rating){
        this.rating = ((this.rating * ratingCount++) + rating) / ratingCount;
    }

    @Override
    public boolean find(String key) {
        return this.name.equals(key);
    }

    public boolean isAdmin(User user) {
        return admins.contains(user);
    }

    public String getName() {
        return name;
    }
    public ArrayList<User> getMembers() {
        return members;
    }
    public ArrayList<User> getAdmins() {
        return admins;
    }
    public ArrayList<Event> getCurrentEvents() {
        return currentEvents;
    }
    public ArrayList<Event> getPastEvents() {
        return pastEvents;
    }
    public Image getPhoto() {
        return photo;
    }
    public double getRating() {
        return rating;
    }
    public int getRatingCount() {
        return ratingCount;
    }
    public ObjectId getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public void setName(String name) {
        this.name = name;
    }
    public void setPhoto(Image photo) {
        this.photo = photo;
    }

    @Override
    public String toString() {
        return name;
    }
    @Override
    public Document toDocument(){
        Document doc = new Document();

        BasicDBList membersArr = new BasicDBList();
        members.forEach(e -> membersArr.add(e.getId()));
        BasicDBList adminsArr = new BasicDBList();
        admins.forEach(e -> adminsArr.add(e.getId()));
        BasicDBList currentEventsArr = new BasicDBList();
        currentEvents.forEach(e -> currentEventsArr.add(e.getId()));
        BasicDBList pastEventsArr = new BasicDBList();
        pastEvents.forEach(e -> pastEventsArr.add(e.getId()));

        doc.append("_id", this.id)
                .append("name", this.name)
                .append("description", this.description)
                .append("photo", this.photo.getUrl())
                .append("rating", this.rating)
                .append("ratingCount", this.ratingCount)
                .append("members", membersArr)
                .append("admins", adminsArr)
                .append("currentEvents", currentEventsArr)
                .append("pastEvents", pastEventsArr);

        return doc;
    }
}
