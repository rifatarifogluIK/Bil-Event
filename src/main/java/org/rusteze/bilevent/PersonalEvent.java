package org.rusteze.bilevent;

import org.bson.Document;

import java.io.FileNotFoundException;
import java.time.LocalDate;

public class PersonalEvent extends Event{

    private User organizer;

    public PersonalEvent(User organizer, String name, String description, String location, LocalDate date) {
        super(name, description, location, date);
        this.organizer = organizer;
    }

    public PersonalEvent(Document doc) throws FileNotFoundException {
        super(doc);
    }

    public User getOrganizer() {
        return organizer;
    }

    public void setOrganizer(User organizer) {
        this.organizer = organizer;
    }

    @Override
    public boolean find(String key) {
        return false;
    }
}
