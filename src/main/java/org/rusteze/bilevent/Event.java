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
    private double rating;
    private int ratingCount;
    private ArrayList<String> attributes;
    private ObjectId id;

    public Event(String name, String description, String location, LocalDate date, Image image, User admin) {
        this.name = name;
        this.description = description;
        this.date = date;
        this.photo = image;
        this.attendees = new ArrayList<>();
        this.attributes = new ArrayList<>();
        this.admin = admin;
        this.location = location;
        this.photo = image;
        if(image == null) {
            File file = new File("src/main/resources/org/rusteze/bilevent/Images/emptyEvent.jpg");
            Image emptyImage = new Image(file.toURI().toString());
            photo = emptyImage;
        }
        chatSpace = new ChatSpace(this);
        this.id = ObjectId.get();
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
        if (attendees.contains(user)) {
            attendees.remove(user);

            //Database_Part begin
            ObjectId newAttendeeId = user.getId();
            Document query = new Document().append("_id", this.id);
            Bson update = Updates.pull("attendees", newAttendeeId);
            UpdateOptions options = new UpdateOptions().upsert(true);
            HelloApplication.db.getCollection("Event").updateOne(query, update, options);
            //end
        }
        //maybe we can make a pop-up screen to show in case there is no user with the given info.
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

    public void addNewRating(int rate)
    {
        this.rating = ((this.rating * ratingCount++) + rating) / ratingCount;

        //Database_Part begin
        Document query = new Document().append("_id", this.id);
        Bson update = Updates.combine(Updates.set("rating", this.rating), Updates.set("ratingCount", this.ratingCount));
        UpdateOptions options = new UpdateOptions().upsert(true);
        HelloApplication.db.getCollection("Event").updateOne(query, update, options);
        //end
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
                .append("photo", this.photo.getUrl())
                .append("rating", this.rating)
                .append("ratingCount", this.ratingCount)
                .append("chatSpace", this.chatSpace.toDocument())
                .append("attendees", attendeesArr)
                .append("attributes", attributesArr);

        return doc;
    }
    @Override
    public Event fromDocument(Document doc) throws FileNotFoundException {
        this.name = (String)doc.get("name");
        this.description = (String)doc.get("description");
        this.date = LocalDate.parse((String)doc.get("date"));
        this.chatSpace = new ChatSpace(this).fromDocument(doc);
        this.location = (String)doc.get("location");
        this.photo = new Image(new FileInputStream((String)doc.get("photo")));
        this.rating = (double)doc.get("rating");
        this.ratingCount = (int)doc.get("ratingCount");
        this.id = (ObjectId)doc.get("_id");

        return this;
    }
}
