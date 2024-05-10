package org.rusteze.bilevent;

import javafx.scene.image.Image;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;

public class User implements Searchable{

    public static Dictionary<ObjectId, User> allUsers = new Hashtable<>();

    private String username;
    private String password;
    private String email;
    private Image photo;
    private ArrayList<User> friends;
    private ArrayList<Community> communities;
    private ArrayList<Event> enrolledEvents;
    private ArrayList<Event> attendedEvents;
    private ArrayList<Event> createdEvents;
    private Recommendation recommendations;
    private double rating;
    private int ratingCount;
    private ObjectId id;

    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;

        communities = new ArrayList<Community>();
        enrolledEvents = new ArrayList<Event>();
        attendedEvents = new ArrayList<Event>();
        createdEvents = new ArrayList<Event>();
        friends = new ArrayList<User>();
        File emptyPP = new File("src/main/resources/org/rusteze/bilevent/Images/UserIcon.png");
        photo = new Image(emptyPP.toURI().toString());
        recommendations = new Recommendation(this);
        rating = 0;
        ratingCount = 0;
        id = ObjectId.get();
    }

    public User(Document doc){
        this.username = (String)doc.get("username");
        this.password = (String)doc.get("password");
        this.email = (String)doc.get("email");
        communities = new ArrayList<Community>();
        enrolledEvents = new ArrayList<Event>();
        attendedEvents = new ArrayList<Event>();
        createdEvents = new ArrayList<Event>();
        friends = new ArrayList<User>();
        recommendations = new Recommendation(this);
        rating = (double)doc.get("rating");
        ratingCount = (int)doc.get("ratingCount");
        id = ObjectId.get();
    }

    public boolean authentication() {
        return true;
    }

    public void enrollEvent(Event event) {

        enrolledEvents.add(binarySearch(event), event);
        event.addAttendee(this);

    }
    public ArrayList<Event> getThisWeekEvents() {
        ArrayList<Event> result = new ArrayList<Event>();
        for(int i = 0; i < enrolledEvents.size(); i++) {
            if(enrolledEvents.get(i).isThisWeek()) {
                result.add(enrolledEvents.get(i));
            } else {
                return result;
            }
        }
        return result;
    }

    public Event createEvent(String name, String description, String location, LocalDate date, Image image) {

        Event event = new PersonalEvent(this, name, description, location, date, image);
        event.addAttendee(this);
        event.getAdmins().add(this);
        enrolledEvents.add(event);
        createdEvents.add(event);
        return event;
    }
    public void joinCommunity(Community community) {
        community.addMember(this);
        this.communities.add(community);
    }
    public void removeCommunity(Community community) {
        communities.remove(community);
        community.getMembers().remove(this);
        community.getAdminList().remove(this);
    }

    public Community createCommunity(String name, String description, Image photo) {

        Community community = new Community(name, description, photo);
        community.getAdminList().add(this);
        community.getMembers().add(this);
        communities.add(community);
        return community;
    }
    /**
     *
     * @param event
     * @return the index of the event in the enrolled arraylist sorted by the days remaining.
     */
    private int binarySearch(Event event) {

        int left = 0;
        int right = enrolledEvents.size() - 1;
        int middle = (left + right) / 2;

        while (left <= right) {

            middle = (left + right) / 2;
            if (enrolledEvents.get(middle).daysRemaining() < event.daysRemaining()) {
                left = middle + 1;
            } else if (enrolledEvents.get(middle).daysRemaining() > event.daysRemaining()) {
                right = middle - 1;
            }

        }
        return left;
    }
    public void leaveEvent(Event event) {
        enrolledEvents.remove(event);
        event.getAttendees().remove(this);
    }

    public boolean hasEventPassed(Event event) {

        LocalDate eventDate = event.getDate();
        LocalDate today = LocalDate.now();

        return today.isAfter(eventDate);
    }

    public void handlePassedEvent() {
        int counter = 0;
        for(Event e : enrolledEvents) {
            if(hasEventPassed(e)) {
                counter++;
            } else {
                break;
            }
        }
        for(int i = 0; i < counter; i++) {

            attendedEvents.add(enrolledEvents.getFirst());
            enrolledEvents.removeFirst();
        }
    }

    public void addNewRating(int rating) {

        this.rating += rating;
        ratingCount++;
    }

    @Override
    public boolean find(String key) {
        return this.username.equals(key);
    }

    public void addFriend(User user) {
        friends.add(user);
    }
    public ArrayList<Event> getEnrolledEvents() {
        return enrolledEvents;
    }
    public ArrayList<Event> getAttendedEvents() {
        return attendedEvents;
    }
    public ArrayList<Event> getCreatedEvents() {
        return createdEvents;
    }
    public ArrayList<Community> getCommunities() {
        return communities;
    }
    public ArrayList<Event> getEventRec() {
        return recommendations.getEventRec();
    }
    public ArrayList<Community> getCommunityRec() {
        return recommendations.getCommunityRec();
    }
    public double getRating() {
        return rating / ratingCount;
    }

    public ArrayList<User> getFriends() {
        return friends;
    }

    public void setPhoto(Image photo) {
        this.photo = photo;
    }

    @Override
    public String toString() {
        return username;
    }

    public Image getPhoto() {
        return photo;
    }

    public String getUsername() {
        return username;
    }
}