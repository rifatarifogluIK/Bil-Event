package org.rusteze.bilevent;

import javafx.scene.image.Image;
import org.bson.Document;

import java.io.FileNotFoundException;
import java.time.LocalDate;

public class CommunityEvent extends Event{

    private Community community;

    public CommunityEvent(Community community, String name, String description, String location, LocalDate date, Image image) {
        super(name, description, location, date, image);
        this.community = community;
    }

    public CommunityEvent(Document doc) throws FileNotFoundException {
        super();
        super.fromDocument(doc);
    }

    public Community getOrganizer() {
        return community;
    }

    public void setCommunity(Community community) {
        this.community = community;
    }

    @Override
    public boolean find(String key) {
        if(community != null)
        {
            return key.equals(community.getName());
        }
        return false;
    }
}
