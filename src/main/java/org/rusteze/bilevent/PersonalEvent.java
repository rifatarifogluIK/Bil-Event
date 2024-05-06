package org.rusteze.bilevent;

import org.bson.Document;

import java.io.FileNotFoundException;
import java.time.LocalDate;

public class PersonalEvent extends Event{

    private User organizer;

    public PersonalEvent(User organizer, String name, String description, LocalDate date) {
        super(name, description, date);
        this.organizer = organizer;
    }

    public PersonalEvent(Document doc) throws FileNotFoundException {
        super(doc);
        this.organizer = User.allUsers.get((String)doc.get("organizer"));
    }
}
