package org.rusteze.bilevent;

import javafx.scene.image.Image;
import org.bson.Document;
import org.bson.types.ObjectId;

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
        this.organizer = User.allUsers.get((ObjectId)doc.get("organizer"));
    }

    public User getOrganizer() {
        return organizer;
    }

    public void setOrganizer(User organizer) {
        this.organizer = organizer;
    }

    @Override
    public boolean findOrganizer(String organizer) {

        return organizer.equals(this.organizer.getUsername());

    }
}
