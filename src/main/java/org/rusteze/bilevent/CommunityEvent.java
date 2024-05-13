package org.rusteze.bilevent;

import javafx.scene.image.Image;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.io.FileNotFoundException;
import java.time.LocalDate;

public class CommunityEvent extends Event{

    private Community community;

    public CommunityEvent(Community community, String name, String description, String location, LocalDate date, Image image) {
        super(name, description, location, date, image, community.getAdmin());
        this.community = community;
    }

    public CommunityEvent(Document doc) throws FileNotFoundException {
        super();
        super.fromDocument(doc);
        this.community = Community.allCommunities.get((ObjectId)doc.get("community"));
    }

    public Community getOrganizer() {
        return community;
    }

    //Only usable from database loading
    public void setCommunity(Community community) {
        this.community = community;
    }

    @Override
    public boolean findOrganizer(String organizer) {

        return organizer.equals(community.getName());

    }

    @Override
    public Document toDocument() {
        return super.toDocument().append("community", this.getId()).append("isPersonal", false);
    }
    @Override
    public Event fromDocument(Document doc) throws FileNotFoundException {
        super.fromDocument(doc);
        this.community = Community.allCommunities.get(doc.get("community"));
        return this;
    }
}
