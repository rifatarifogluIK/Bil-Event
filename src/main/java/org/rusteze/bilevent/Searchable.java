package org.rusteze.bilevent;

import org.bson.types.ObjectId;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;

public interface Searchable {
    public static ArrayList<Searchable> allSearchables = new ArrayList<Searchable>();

    public boolean findName(String name);

    public boolean findOrganizer(String organizer);

    public default boolean findLocation(String location) {
        return false;
    }

    public default boolean findDate(LocalDate date) {
        return false;
    }

    public static ArrayList<Searchable> search(String name, String organizer, String location, LocalDate date) {
        ArrayList<Searchable> found = new ArrayList<>();

        for (Searchable s : allSearchables) {
            if (!location.isBlank() && s.findLocation(location)) {
                if (!found.contains(s)) {
                    found.add(s);
                }
            } else if (!location.isBlank()) {
                found.remove(s);
            }

            if (date != null && s.findDate(date)) {
                if (!found.contains(s)) {
                    found.add(s);
                }
            } else if (date != null) {
                found.remove(s);
            }

            if (!organizer.isBlank() && s.findOrganizer(organizer)) {
                if (!found.contains(s)) {
                    found.add(s);
                }
            } else if (!organizer.isBlank()) {
                found.remove(s);
            }

            if (!name.isBlank() && s.findName(name)) {
                if (!found.contains(s)) {
                    found.add(s);
                }
            } else if (!name.isBlank()) {
                found.remove(s);
            }
        }
        return allSearchables;

        /*for(Searchable s: allSearchables)
        {
            if(s instanceof User)
            {
                founded.add(s);
            }
            else if(s instanceof PersonalEvent && s.find(organizer))
            {
                founded.add(s);
            }
            else if(s instanceof CommunityEvent && s.find(name))
            {
                founded.add(s);
            }
            else if(s instanceof Community && s.find(name))
            {
                founded.add(s);
            }
        }

        if (location != null || date != null) {
            for (Searchable s : allSearchables) {
                if (s instanceof Event) {
                    Event event = (Event) s;
                    if (location != null && !location.isEmpty() && !event.getLocation().equals(location)) {
                        continue;  // Skip if location does not match
                    }
                    if (date != null && !event.getDate().equals(date)) {
                        continue;  // Skip if date does not match
                    }
                    founded.add(event);  // Add if location and date match
                }
            }
        }
        return founded;*/
    }
}
