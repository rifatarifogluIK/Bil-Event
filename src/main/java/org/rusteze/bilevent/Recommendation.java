package org.rusteze.bilevent;

import java.time.LocalDate;
import java.util.ArrayList;

public class Recommendation {

    private User user;
    private ArrayList<Event> eventRec;
    private ArrayList<Community> communityRec;

    public Recommendation(User user) {
        this.user = user;
        eventRec = new ArrayList<Event>();
        communityRec = new ArrayList<Community>();
    }

    public User getUser() {
        return user;
    }

    public ArrayList<Event> getEventRec() {
        return eventRec;
    }

    public ArrayList<Community> getCommunityRec() {
        return communityRec;
    }
}
