package org.rusteze.bilevent;

import com.mongodb.client.model.UpdateOptions;
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
import java.util.List;
import java.util.stream.Collectors;

public class Community implements Searchable, ConvertibleWithDocument<Community> {

    public static Dictionary<ObjectId, Community> allCommunities = new Hashtable<>();
    public static Dictionary<ObjectId, Community> popularCommunities = new Hashtable<>();

    private String name;
    private String description;
    private ArrayList<User> members;
    private User admin;
    private ArrayList<Event> currentEvents;
    private ArrayList<Event> pastEvents;
    private Image photo;
    private double rating;
    private int ratingCount;
    private ObjectId id;

    public Community(String name, String description, Image photo, User admin) {
        this.name = name;
        this.description = description;
        this.members = new ArrayList<>();
        this.admin = admin;
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
        allSearchables.add(this);
    }

    public Community(){
        this.members = new ArrayList<>();
        this.currentEvents = new ArrayList<>();
        this.pastEvents = new ArrayList<>();
    }

    public void addMember(User user){
        members.add(user);

        //Database_Part begin
        ObjectId newMemberId = user.getId();
        Document query = new Document().append("_id", this.getId());
        Bson update = Updates.addToSet("members", newMemberId);
        UpdateOptions options = new UpdateOptions().upsert(true);
        HelloApplication.db.getCollection("Community").updateOne(query, update, options);
        //end
    }

    public void removeMember(User user){
        if(members.contains(user)){
            members.remove(user);

            //Database_Part begin
            ObjectId newMemberId = user.getId();
            Document query = new Document().append("_id", user.getId());
            Bson update = Updates.pull("members", newMemberId);
            UpdateOptions options = new UpdateOptions().upsert(true);
            HelloApplication.db.getCollection("Community").updateOne(query, update, options);
            //end
        }
    }

    public void changeAdmin(User user){
        if(members.contains(user)){
            this.admin = user;

            //Database_Part begin
            ObjectId newAdminId = user.getId();
            Document query = new Document().append("_id", this.id);
            Bson update = Updates.set("admin", newAdminId);
            UpdateOptions options = new UpdateOptions().upsert(true);
            HelloApplication.db.getCollection("Community").updateOne(query, update, options);
            //end
        }
    }


    public Event createEvent(String name, String description, String location, LocalDate date, Image image){
        Event event = new CommunityEvent(this, name, description, location, date, image);
        Event.allEvents.put(event.getId(), event);

        //Database_Part begin
        HelloApplication.db.getCollection("Event").insertOne(event.toDocument());
        //end

        currentEvents.add(event);
        event.getChatSpace().addMessage("Welcome to the " + event.getName() + " Chat!", "System");

        //Database_Part begin
        ObjectId newEventId = event.getId();
        Document query = new Document().append("_id", this.id);
        Bson update = Updates.addToSet("currentEvents", newEventId);
        UpdateOptions options = new UpdateOptions().upsert(true);
        HelloApplication.db.getCollection("Community").updateOne(query, update, options);
        //end

        return event;
    }

    public void handlePassedEvent(){
        for(int i = 0; i < currentEvents.size(); i++){
            Event current = currentEvents.get(i);
            if(current.getDate().isBefore(LocalDate.now())){
                currentEvents.remove(current);
                pastEvents.add(current);

                //Database_Part begin
                ObjectId currentId = current.getId();
                Document query = new Document().append("_id", this.id);
                Bson update = Updates.combine(Updates.pull("currentEvents", currentId), Updates.addToSet("pastEvents", currentId));
                UpdateOptions options = new UpdateOptions().upsert(true);
                HelloApplication.db.getCollection("Community").updateOne(query, update, options);
                //end

                i--;
            }
        }
    }

    public void addNewRating(int rating){
        this.rating = ((this.rating * ratingCount++) + rating) / ratingCount;

        //Database_Part begin
        Document query = new Document().append("_id", this.id);
        Bson update = Updates.combine(Updates.set("rating", this.rating), Updates.set("ratingCount", this.ratingCount));
        UpdateOptions options = new UpdateOptions().upsert(true);
        HelloApplication.db.getCollection("Community").updateOne(query, update, options);
        //end
    }

    @Override
    public boolean findName(String name) {
        return this.name.equals(name);
    }
    @Override
    public boolean findOrganizer(String organizer) {
        return findName(organizer);
    }

    public boolean isAdmin(User user) {
        return this.admin.getId().equals(user.getId());
    }

    public String getName() {
        return name;
    }
    public String getDescription() {
        return description;
    }
    public ArrayList<User> getMembers() {
        return members;
    }
    public User getAdmin() {
        return admin;
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
    public void setAdmin(User user){
        admin = user;
    }

    @Override
    public String toString() {
        return name;
    }
    @Override
    public Document toDocument(){
        Document doc = new Document();

        List<ObjectId> membersArr = members.stream().map(e -> e.getId()).collect(Collectors.toList());
        List<ObjectId> currentEventsArr = currentEvents.stream().map(e -> e.getId()).collect(Collectors.toList());
        List<ObjectId> pastEventsArr = pastEvents.stream().map(e -> e.getId()).collect(Collectors.toList());

        doc.append("_id", this.id)
                .append("name", this.name)
                .append("description", this.description)
                .append("admin", this.admin.getId())
                .append("photo", this.photo.getUrl())
                .append("rating", this.rating)
                .append("ratingCount", this.ratingCount)
                .append("members", membersArr)
                .append("currentEvents", currentEventsArr)
                .append("pastEvents", pastEventsArr);

        return doc;
    }
    @Override
    public Community fromDocument(Document doc) throws FileNotFoundException{
        this.name = (String)doc.get("name");
        this.description = (String)doc.get("description");
        this.photo = new Image(new FileInputStream((String)doc.get("photo")));
        this.rating = (double)doc.get("rating");
        this.ratingCount = (int)doc.get("ratingCount");
        this.id = (ObjectId)doc.get("_id");

        return this;
    }
}
