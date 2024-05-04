package org.rusteze.bilevent;

import java.awt.image.BufferedImage;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

public abstract class Event {
    private String name;
    private String description;
    private LocalDate date;
    private ChatSpace chatSpace;
    private ArrayList<User> attendees;
    private String location;
    private ArrayList<User> admins;
    private BufferedImage photo;
    private double rating;
    private int ratingCount;
    private ArrayList<String> attributes;

    public Event(String name, String description, LocalDate date) {
        this.name = name;
        this.description = description;
        this.date = date;
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

    public ArrayList<String> getAttributes() {
        return attributes;
    }

    public ArrayList<User> getAdmins() {
        return admins;
    }

    public ArrayList<User> getAttendees() {
        return attendees;
    }

}
