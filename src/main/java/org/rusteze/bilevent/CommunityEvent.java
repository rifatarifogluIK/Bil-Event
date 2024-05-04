package org.rusteze.bilevent;

import java.time.LocalDate;

public class CommunityEvent extends Event{

    private Community community;

    public CommunityEvent(Community community, String name, String description, LocalDate date) {
        super(name, description, date);
        this.community = community;
    }
}
