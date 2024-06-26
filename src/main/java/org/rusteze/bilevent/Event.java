package org.rusteze.bilevent;

import com.mongodb.BasicDBList;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.Updates;
import javafx.scene.image.Image;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

public abstract class Event implements Searchable, ConvertibleWithDocument<Event> {

    public static Dictionary<ObjectId, Event> allEvents = new Hashtable<>();

    private String name;
    private String description;
    private LocalDate date;
    private ChatSpace chatSpace;
    private ArrayList<User> attendees;
    private String location;
    private User admin;
    private Image photo;
    private String imageName;
    private double rating;
    private int ratingCount;
    private ArrayList<String> attributes;
    private ObjectId id;

    public Event(String name, String description, String location, LocalDate date, String imageName, User admin) {
        this.id = ObjectId.get();
        this.name = name;
        this.description = description;
        this.date = date;
        this.attendees = new ArrayList<>();
        this.attributes = new ArrayList<>();
        this.admin = admin;
        this.location = location;
        if(imageName == null) {
            this.imageName = "emptyEvent.jpg";
        } else{
            this.imageName = imageName;
        }
        File file = new File("src/main/resources/org/rusteze/bilevent/ImageDB/" + imageName);
        photo = new Image(file.toURI().toString());
        chatSpace = new ChatSpace(this);
        allSearchables.add(this);
    }

    public Event(){
        this.attendees = new ArrayList<>();
        this.attributes = new ArrayList<>();
    }

    public void addAttendee(User user)
    {
        if (!attendees.contains(user)) {
            attendees.add(user);

            //Database_Part begin
            ObjectId newAttendeeId = user.getId();
            Document query = new Document().append("_id", this.id);
            Bson update = Updates.addToSet("attendees", newAttendeeId);
            UpdateOptions options = new UpdateOptions().upsert(true);
            HelloApplication.db.getCollection("Event").updateOne(query, update, options);
            //end
        }
    }

    public void removeAttendee(User user) {
        attendees.remove(user);
        //Database_Part begin
        ObjectId newAttendeeId = user.getId();
        Document query = new Document().append("_id", this.id);
        Bson update = Updates.pull("attendees", newAttendeeId);
        UpdateOptions options = new UpdateOptions().upsert(true);
        HelloApplication.db.getCollection("Event").updateOne(query, update, options);
        //end
    }

    public void addAttribute(String attribute) {
        this.attributes.add(attribute);

        //Database_Part begin
        Document query = new Document().append("_id", this.id);
        Bson update = Updates.addToSet("attributes", attribute);
        UpdateOptions options = new UpdateOptions().upsert(true);
        HelloApplication.db.getCollection("Event").updateOne(query, update, options);
        //end
    }

    public boolean isAdmin(User user)
    {
        return this.admin.getId().equals(user.getId());
    }

    public int daysRemaining()
    {
        LocalDate plannedDate = this.date;
        LocalDate currentDate = LocalDate.now();
        return (int)ChronoUnit.DAYS.between(currentDate, plannedDate);
    }

    public boolean isThisWeek() {
        LocalDate currentDate = date;
        LocalDate mostRecentMonday = currentDate.with(DayOfWeek.MONDAY);
        int dateOfMonday = mostRecentMonday.getDayOfMonth();
        int today = currentDate.getDayOfMonth();
        return (today >= dateOfMonday && today < dateOfMonday + 7);
    }

    public void addNewRating(int rating)
    {
        this.rating = ((this.rating * ratingCount++) + rating) / ratingCount;

        //Database_Part begin
        Document query = new Document().append("_id", this.id);
        Bson update = Updates.combine(Updates.set("rating", this.rating), Updates.set("ratingCount", this.ratingCount));
        UpdateOptions options = new UpdateOptions().upsert(true);
        HelloApplication.db.getCollection("Event").updateOne(query, update, options);
        //end
    }
    @Override
    public boolean findName(String name) {
        return this.name.equals(name);
    }
    @Override
    public boolean findLocation(String location) {
        return this.location.equals(location);
    }

    @Override
    public boolean findDate(LocalDate date) {
        return this.date.isEqual(date);
    }

    public String getName() {
        return name;
    }
    public String getDescription() {
        return description;
    }
    public Image getPhoto() {
        return photo;
    }
    public ArrayList<String> getAttributes() {
        return attributes;
    }
    public User getAdmin() {
        return admin;
    }
    public abstract Searchable getOrganizer();
    public ArrayList<User> getAttendees() {
        return attendees;
    }
    public ChatSpace getChatSpace() {
        return chatSpace;
    }
    public LocalDate getDate() {
        return date;
    }
    public ObjectId getId() {
        return id;
    }
    public String getLocation() {
        return location;
    }
    public void setAdmin(User admin) {
        this.admin = admin;
    }

    public double getRating() {
        return rating;
    }

    public void setImageName(String imageURI) {
        this.imageName = imageURI;
    }

    public void setPhoto(Image photo) {
        this.photo = photo;
    }

    public String getImageName() {
        return imageName;
    }

    @Override
    public Document toDocument() {
        Document doc = new Document();

        BasicDBList attendeesArr = new BasicDBList();
        attendees.forEach(e -> attendeesArr.add(e.getId()));
        BasicDBList attributesArr = new BasicDBList();
        attributes.forEach(e -> attributesArr.add(e));

        doc.append("_id", this.id)
                .append("name", this.name)
                .append("description", this.description)
                .append("date", this.date.toString())
                .append("location", this.location)
                .append("admin", this.admin.getId())
                .append("photo", this.imageName)
                .append("rating", this.rating)
                .append("ratingCount", this.ratingCount)
                .append("messages", this.chatSpace.toDocument().get("messages"))
                .append("attendees", attendeesArr)
                .append("attributes", attributesArr);

        return doc;
    }
    @Override
    public Event fromDocument(Document doc) throws FileNotFoundException {
        this.id = (ObjectId)doc.get("_id");
        this.name = (String)doc.get("name");
        this.description = (String)doc.get("description");
        this.date = LocalDate.parse((String)doc.get("date"));
        this.chatSpace = new ChatSpace(this).fromDocument(doc);
        this.location = (String)doc.get("location");
        this.imageName = (String)doc.get("photo");
        this.photo = new Image(new FileInputStream("src/main/resources/org/rusteze/bilevent/ImageDB/" + this.imageName));
        this.rating = (double)doc.get("rating");
        this.ratingCount = (int)doc.get("ratingCount");

        return this;
    }
}
