package org.rusteze.bilevent;

import java.time.LocalDate;

public class PersonalEvent extends Event{

    private User organizer;

    public PersonalEvent(User organizer, String name, String description, LocalDate date) {
        super(name, description, date);
        this.organizer = organizer;
    }
}
