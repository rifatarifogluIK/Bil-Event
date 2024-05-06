package org.rusteze.bilevent;

import org.bson.Document;

import java.io.FileNotFoundException;
import java.time.LocalDate;

public class CommunityEvent extends Event{

    private Community community;

    public CommunityEvent(Community community, String name, String description, LocalDate date) {
        super(name, description, date);
        this.community = community;
    }

    public CommunityEvent(Document doc) throws FileNotFoundException {
        super(doc);
        this.community = Community.allCommunities.get((String)doc.get("community"));
    }
}
