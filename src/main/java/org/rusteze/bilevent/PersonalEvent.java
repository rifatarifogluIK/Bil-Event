package org.rusteze.bilevent;

import javafx.scene.image.Image;
import org.bson.Document;

import java.io.FileNotFoundException;
import java.time.LocalDate;

public class PersonalEvent extends Event{

    private User organizer;

    public PersonalEvent(User organizer, String name, String description, String location, LocalDate date, Image image) {
        super(name, description, location, date, image);
        this.organizer = organizer;
    }

    public PersonalEvent(Document doc) throws FileNotFoundException {
        super();
        super.fromDocument(doc);
    }

    public User getOrganizer() {
        return organizer;
    }

    public void setOrganizer(User organizer) {
        this.organizer = organizer;
    }

    @Override
    public boolean find(String key) {
        if (organizer != null) {
            return key.equals(organizer.getUsername());
        }
        return false;
    }
}
