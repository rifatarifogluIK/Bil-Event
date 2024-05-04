package org.rusteze.bilevent;

import java.time.LocalDate;
import java.util.ArrayList;
import java.awt.image.BufferedImage;

public class User {

    private String username;
    private String password;
    private String email;
    private BufferedImage photo;
    private ArrayList<User> friends;
    private Arraylist<Community> communities;
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

    }

    public void enrollEvent(Event event) {

        enrolledEvents.add(binarySearch(event), event);
        event.getAttendees().add(this);

    }

    public void createEvent(String name, String description, LocalDate date) {

        Event event = new PersonalEvent(name, description, date);
        event.getAttendees().add(this);
        event.getAdmins().add(this);
        enrolledEvents.add(event);
        createdEvents.add(event);
    }

    public void createCommunity(String name) {

        Community community = new Community(name);
        community.getAdmins().add(this);
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
    public Arraylist<Community> getCommunities() {
        return communities;
    }
    public Recommendation getRecommendations() {
        return recommendations;
    }
    public double getRating() {
        return rating / ratingCount;
    }

    public String getUsername() {
        return username;
    }
}