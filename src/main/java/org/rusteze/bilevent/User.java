package org.rusteze.bilevent;

import javafx.scene.image.Image;

import java.time.LocalDate;
import java.util.ArrayList;

public class User {

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

    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;

        communities = new ArrayList<Community>();
        enrolledEvents = new ArrayList<Event>();
        attendedEvents = new ArrayList<Event>();
        createdEvents = new ArrayList<Event>();
        recommendations = new Recommendation(this);
        rating = 0;
        ratingCount = 0;
    }

    public boolean authentication() {
        return true;
    }

    public void enrollEvent(Event event) {

        enrolledEvents.add(binarySearch(event), event);
        event.addAttendee(this);

    }

    public void createEvent(String name, String description, LocalDate date) {

        Event event = new PersonalEvent(this, name, description, date);
        event.addAttendee(this);
        event.getAdmins().add(this);
        enrolledEvents.add(event);
        createdEvents.add(event);
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

    public void createCommunity(String name, Image photo) {

        Community community = new Community(name, photo);
        community.getAdminList().add(this);
        community.getMembers().add(this);
        communities.add(community);
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

    public String getUsername() {
        return username;
    }
}