package org.rusteze.bilevent;

import com.mongodb.BasicDBList;
import com.mongodb.client.model.InsertOneOptions;
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
import java.util.Enumeration;
import java.util.Hashtable;

public class User implements Searchable, ConvertibleWithDocument<User> {

    public static Dictionary<ObjectId, User> allUsers = new Hashtable<>();

    private String username;
    private String password;
    private String email;
    private Image photo;
    private String imageName;
    private ArrayList<User> friends;
    private ArrayList<Community> communities;
    private ArrayList<Event> enrolledEvents;
    private ArrayList<Event> attendedEvents;
    private ArrayList<Event> createdEvents;
    private Recommendation recommendations;
    private double rating;
    private int ratingCount;
    private ObjectId id;
    private ArrayList<User> friendRequests;
    private ArrayList<Event> ratedEvents;

    public User(String username, String password, String email, String imageName) {
        this.username = username;
        this.password = password;
        this.email = email;

        ratedEvents = new ArrayList<Event>();
        friendRequests = new ArrayList<User>();
        communities = new ArrayList<Community>();
        enrolledEvents = new ArrayList<Event>();
        attendedEvents = new ArrayList<Event>();
        createdEvents = new ArrayList<Event>();
        friends = new ArrayList<User>();
        if(imageName == null) {
            this.imageName = "UserIcon.png";
        } else{
            this.imageName = imageName;
        }
        File emptyPP = new File("src/main/resources/org/rusteze/bilevent/Images/" + imageName);
        photo = new Image(emptyPP.toURI().toString());
        rating = 0;
        ratingCount = 0;
        id = ObjectId.get();
        allSearchables.add(this);
    }

    public User(){
        this.communities = new ArrayList<Community>();
        this.enrolledEvents = new ArrayList<Event>();
        this.attendedEvents = new ArrayList<Event>();
        this.createdEvents = new ArrayList<Event>();
        this.friends = new ArrayList<User>();
        this.friendRequests = new ArrayList<User>();
        ratedEvents = new ArrayList<Event>();
    }

    public boolean authentication() {
        return true;
    }

    public void enrollEvent(Event event) {
        //enrolledEvents.add(binarySearch(event), event);
        enrolledEvents.add(event);

        //Database_Part begin
        Document query = new Document().append("_id", this.id);
        Bson update = Updates.addToSet("enrolledEvents", event.getId());
        UpdateOptions options = new UpdateOptions().upsert(true);
        HelloApplication.db.getCollection("User").updateOne(query, update, options);
        //end

        event.addAttendee(this);
        recommendations.recommend();
    }

    public ArrayList<Event> getThisWeekEvents() {
        ArrayList<Event> result = new ArrayList<Event>();
        for(int i = 0; i < enrolledEvents.size(); i++) {
            if(enrolledEvents.get(i).isThisWeek()) {
                result.add(enrolledEvents.get(i));
            }
        }
        return result;
    }

    public Event createEvent(String name, String description, String location, LocalDate date, String imageName) {
        PersonalEvent event = new PersonalEvent(this, name, description, location, date, imageName);
        Event.allEvents.put(event.getId(), event);

        //Database_Part begin
        HelloApplication.db.getCollection("Event").insertOne(event.toDocument());
        //end

        event.addAttendee(this);
        event.getChatSpace().addMessage("Welcome to the " + event.getName() + " Chat!", "System");

        enrolledEvents.add(event);
        createdEvents.add(event);

        //Database_Part begin
        Document query = new Document().append("_id", this.id);
        Bson update = Updates.combine(Updates.addToSet("enrolledEvents", event.getId()), Updates.addToSet("createdEvents", event.getId()));
        UpdateOptions options = new UpdateOptions().upsert(true);
        HelloApplication.db.getCollection("User").updateOne(query, update, options);
        //end
        return event;
    }

    public void joinCommunity(Community community) {
        community.addMember(this);
        this.communities.add(community);

        //Database_Part begin
        Document query = new Document().append("_id", this.id);
        Bson update = Updates.addToSet("communities", community.getId());
        UpdateOptions options = new UpdateOptions().upsert(true);
        HelloApplication.db.getCollection("User").updateOne(query, update, options);
        //end
    }

    public void removeCommunity(Community community) {
        if (community.isAdmin(this)){
            Community.allCommunities.remove(community.getId());

            //Database_Part begin
                HelloApplication.db.getCollection("Community").deleteOne(new Document().append("_id", community.getId()));
            //end
        }else{
            community.removeMember(this);
        }

        communities.remove(community);

        //Database_Part begin
        Document query = new Document().append("_id", this.id);
        Bson update = Updates.pull("communities", community.getId());
        UpdateOptions options = new UpdateOptions().upsert(true);
        HelloApplication.db.getCollection("User").updateOne(query, update, options);
        //end
    }

    public Community createCommunity(String name, String description, String imageName) {
        Community community = new Community(name, description, imageName, this);
        Community.allCommunities.put(community.getId(), community);

        //Database_Part begin
        HelloApplication.db.getCollection("Community").insertOne(community.toDocument());
        //end

        this.joinCommunity(community);

        return community;
    }

    public static User userWith(String email) {
        Enumeration<User> users = allUsers.elements();
        while (users.hasMoreElements()){
            User temp = users.nextElement();
            if(temp.getEmail().equals(email)){
                return temp;
            }
        }
        return null;
    }

    public void leaveEvent(Event event) {
        if (event.isAdmin(this)){
            Event.allEvents.remove(event.getId());

            //Database_Part begin
            HelloApplication.db.getCollection("Event").deleteOne(new Document().append("_id", event.getId()));
            //end
        }else{
            event.removeAttendee(this);
        }

        enrolledEvents.remove(event);

        //Database_Part begin
        Document query = new Document().append("_id", this.id);
        Bson update = Updates.combine(Updates.pull("enrolledEvents", event.getId()), Updates.pull("createdEvents", event.getId()));
        UpdateOptions options = new UpdateOptions().upsert(true);
        HelloApplication.db.getCollection("User").updateOne(query, update, options);
        //end
        recommendations.recommend();
    }

    public boolean hasEventPassed(Event event) {

        LocalDate eventDate = event.getDate();
        LocalDate today = LocalDate.now();

        return today.isAfter(eventDate);
    }

    public void handlePassedEvent() {
        for(int i = 0; i < enrolledEvents.size(); i++) {
            Event current = enrolledEvents.get(i);
            if(hasEventPassed(current)){
                attendedEvents.add(current);
                enrolledEvents.remove(current);

                //Database_Part begin
                Document query = new Document().append("_id", this.id);
                Bson update = Updates.combine(Updates.pull("enrolledEvents", current.getId()), Updates.addToSet("attendedEvents", current.getId()));
                UpdateOptions options = new UpdateOptions().upsert(true);
                HelloApplication.db.getCollection("User").updateOne(query, update, options);
                //end

                i--;
            }
        }
    }

    public void addNewRating(int rating) {
        this.rating = ((this.rating * ratingCount++) + rating) / ratingCount;

        //Database_Part begin
        Document query = new Document().append("_id", this.id);
        Bson update = Updates.combine(Updates.set("rating", this.rating), Updates.set("ratingCount", this.ratingCount));
        UpdateOptions options = new UpdateOptions().upsert(true);
        HelloApplication.db.getCollection("User").updateOne(query, update, options);
        //end
    }

    public void setPassword(String password) {
        this.password = password;

        //Database_Part begin
        Document query = new Document().append("_id", this.id);
        Bson update = Updates.set("password", password);
        UpdateOptions options = new UpdateOptions().upsert(true);
        HelloApplication.db.getCollection("User").updateOne(query, update, options);
        //end
    }

    @Override
    public boolean findName(String name) {
        return this.username.equals(name);
    }
    @Override
    public boolean findOrganizer(String organizer) {
        return findName(organizer);
    }

    public void addFriend(User user) {
        friends.add(user);

        //Database_Part begin
        Document query = new Document().append("_id", this.id);
        Bson update = Updates.addToSet("friends", user.getId());
        UpdateOptions options = new UpdateOptions().upsert(true);
        HelloApplication.db.getCollection("User").updateOne(query, update, options);
        //end
    }

    public void removeFriend(User user){
        friends.remove(user);

        //Database_Part begin
        Document query = new Document().append("_id", this.id);
        Bson update = Updates.pull("friends", user.getId());
        UpdateOptions options = new UpdateOptions().upsert(true);
        HelloApplication.db.getCollection("User").updateOne(query, update, options);
        //end
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
    public double getRating() {
        return rating / ratingCount;
    }
    public ArrayList<User> getFriends() {
        return friends;
    }
    public Image getPhoto() {
        return photo;
    }
    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }
    public String getEmail() {
        return email;
    }
    public int getRatingCount() {
        return ratingCount;
    }
    public ObjectId getId() {
        return id;
    }
    public String getImageName() {
        return imageName;
    }
    public ArrayList<Event> getRatedEvents() {
        return ratedEvents;
    }
    public ArrayList<User> getFriendRequests() {
        return friendRequests;
    }

    public Recommendation getRecommendations() {
        return recommendations;
    }

    public void setPhoto(Image photo) {
        this.photo = photo;
    }
    public void setImageName(String imageName) {
        this.imageName = imageName;

        //Database_Part begin
        Document query = new Document().append("_id", this.id);
        Bson update = Updates.set("photo", this.imageName);
        UpdateOptions options = new UpdateOptions().upsert(true);
        HelloApplication.db.getCollection("User").updateOne(query, update, options);
        //end
    }
    public void setRecommendations(Recommendation recommendations) {
        this.recommendations = recommendations;
    }

    @Override
    public String toString() {
        return username;
    }
    @Override
    public Document toDocument(){
        Document doc = new Document();

        BasicDBList friendsArr = new BasicDBList();
        friends.forEach(e -> friendsArr.add(e.getId()));
        BasicDBList communitiesArr = new BasicDBList();
        communities.forEach(e -> communitiesArr.add(e.getId()));
        BasicDBList enrolledEventsArr = new BasicDBList();
        enrolledEvents.forEach(e -> enrolledEventsArr.add(e.getId()));
        BasicDBList attendedEventsArr = new BasicDBList();
        attendedEvents.forEach(e -> attendedEventsArr.add(e.getId()));
        BasicDBList createdEventsArr = new BasicDBList();
        createdEvents.forEach(e -> createdEventsArr.add(e.getId()));

        doc.append("_id", this.id)
                .append("username", this.username)
                .append("password", this.password)
                .append("email", this.email)
                .append("photo", this.imageName)
                .append("rating", this.rating)
                .append("ratingCount", this.ratingCount)
                .append("friends", friendsArr)
                .append("communities", communitiesArr)
                .append("enrolledEvents", enrolledEventsArr)
                .append("attendedEvents", attendedEventsArr)
                .append("createdEvents", createdEventsArr);

        return doc;
    }
    @Override
    public User fromDocument(Document doc) throws FileNotFoundException {
        this.username = (String)doc.get("username");
        this.password = (String)doc.get("password");
        this.email = (String)doc.get("email");
        this.imageName = (String)doc.get("photo");
        this.photo = new Image(new FileInputStream("src/main/resources/org/rusteze/bilevent/ImageDB/" + imageName));
        this.recommendations = new Recommendation(this);
        this.rating = (double)doc.get("rating");
        this.ratingCount = (int)doc.get("ratingCount");
        this.id = (ObjectId)doc.get("_id");

        return this;
    }
}