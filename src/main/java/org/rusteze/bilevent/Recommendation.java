package org.rusteze.bilevent;

import java.util.ArrayList;

public class Recommendation {

    private User user;
    private ArrayList<Event> recommendations;

    public Recommendation(User user) {
        this.user = user;
        recommendations = new ArrayList<Event>();
    }

    public User getUser() {
        return user;
    }

    public ArrayList<Event> getRecommendations() {
        return recommendations;
    }
}
