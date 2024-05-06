package org.rusteze.bilevent;

import javafx.scene.image.Image;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;

public class Community implements Searchable{

    public static Dictionary<ObjectId, Community> allCommunities = new Hashtable<>();
    public static Dictionary<ObjectId, Community> popularCommunities = new Hashtable<>();

    private String name;
    private ArrayList<User> members;
    private ArrayList<User> adminList;
    private ArrayList<Event> currentEvents;
    private ArrayList<Event> pastEvents;
    private Image photo;
    private double rating;
    private int ratingCount;
    private ObjectId id;

    public Community(String name, Image photo) {
        this.name = name;
        this.members = new ArrayList<>();
        this.adminList = new ArrayList<>();
        this.currentEvents = new ArrayList<>();
        this.pastEvents = new ArrayList<>();
        this.photo = photo;
        this.rating = 0.0;
        this.ratingCount = 0;
        this.id = ObjectId.get();
    }

    public Community(Document doc) throws FileNotFoundException {
        this.name = (String)doc.get("name");
        this.members = new ArrayList<>();
        this.adminList = new ArrayList<>();
        this.currentEvents = new ArrayList<>();
        this.pastEvents = new ArrayList<>();
        this.photo = new Image(new FileInputStream((String)doc.get("photo")));
        this.rating = (double)doc.get("rating");
        this.ratingCount = (int)doc.get("ratingCount");
        this.id = (ObjectId)doc.get("_id");

        ((Document)doc.get("members")).values().forEach(e -> members.add(User.allUsers.get(e.toString())));
        ((Document)doc.get("adminList")).values().forEach(e -> adminList.add(User.allUsers.get(e.toString())));
        ((Document)doc.get("currentEvents")).values().forEach(e -> currentEvents.add(Event.allEvents.get(e.toString())));
        ((Document)doc.get("pastEvents")).values().forEach(e -> pastEvents.add(Event.allEvents.get(e.toString())));
    }

    public void addMember(User user){
        members.add(user);
    }

    public void setAdmin(User user){
        if(members.contains(user)){
            adminList.add(user);
        }
    }

    public void createEvent(String name, String description, LocalDate date){
        Event event = new CommunityEvent(this, name, description, date);
        adminList.forEach(admin -> event.getAdmins().add(admin));
        currentEvents.add(event);
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
        return adminList.contains(user);
    }
    public String getName() {
        return name;
    }

    public ArrayList<User> getMembers() {
        return members;
    }

    public ArrayList<User> getAdminList() {
        return adminList;
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

    public void setName(String name) {
        this.name = name;
    }

    public void setPhoto(Image photo) {
        this.photo = photo;
    }
}
