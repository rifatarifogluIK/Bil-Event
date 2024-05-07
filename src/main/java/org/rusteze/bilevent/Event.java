package org.rusteze.bilevent;

import javafx.scene.image.Image;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

public abstract class Event {

    public static Dictionary<ObjectId, Event> allEvents = new Hashtable<>();

    private String name;
    private String description;
    private LocalDate date;
    private ChatSpace chatSpace;
    private ArrayList<User> attendees;
    private String location;
    private ArrayList<User> admins;
    private Image photo;
    private double rating;
    private int ratingCount;
    private ArrayList<String> attributes;
    private ObjectId id;

    public Event(String name, String description, LocalDate date) {
        this.name = name;
        this.description = description;
        this.date = date;
        attendees = new ArrayList<>();
        attributes = new ArrayList<>();
        admins = new ArrayList<>();
        File file = new File("src/main/resources/org/rusteze/bilevent/Images/emptyEvent.jpg");
        Image image = new Image(file.toURI().toString());
        this.photo = image;
        chatSpace = new ChatSpace();
        this.id = ObjectId.get();
    }

    public Event(Document doc) throws FileNotFoundException {
        this.name = (String)doc.get("name");
        this.description = (String)doc.get("description");
        this.date = LocalDate.parse((String)doc.get("date"));
        this.chatSpace = new ChatSpace(doc);
        this.attendees = new ArrayList<>();
        this.admins = new ArrayList<>();
        this.attributes = new ArrayList<>();
        this.location = (String)doc.get("location");
        this.photo = new Image(new FileInputStream((String)doc.get("photo")));
        this.rating = (double)doc.get("rating");
        this.ratingCount = (int)doc.get("ratingCount");
        this.id = (ObjectId)doc.get("_id");
    }

    public void addAttendee(User user)
    {
        //in case user is already in the current attendees
        for(User u: attendees)
        {
            if(u.getUsername().equals(user.getUsername()))
                //we can make another pop screen for this part.
                return;
        }
        attendees.add(user);
    }
    public void removeAttendee(User user) {
        for (User u : attendees) {
            if (u.getUsername().equals(user.getUsername())) {
                attendees.remove(u);
                return;
            }
        }
        //maybe we can make a pop-up screen to show in case there is no user with the given info.
    }

    public boolean isAdmin(User user)
    {
        for(User u: admins)
        {
            if(u.getUsername().equals(user.getUsername())) {
                return true;
            }
        }
        return false;
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
        this.rating += rate;
        ratingCount++;
    }
    public double currentAverageRating()
    {
        double RatingWithoutRounding = rating / ratingCount;

        //rounds the rating to 1 decimal places
        return Math.round(RatingWithoutRounding * 10.0) / 10.0;
    }

    public String getName() {
        return name;
    }

    public Image getPhoto() {
        return photo;
    }

    public ArrayList<String> getAttributes() {
        return attributes;
    }

    public ArrayList<User> getAdmins() {
        return admins;
    }

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
}
