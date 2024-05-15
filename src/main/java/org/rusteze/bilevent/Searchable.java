package org.rusteze.bilevent;

import org.bson.types.ObjectId;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Enumeration;
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
    public static ArrayList<Searchable> search(String name, String organizer, String location, LocalDate date){
        allSearchables.clear();

        Enumeration<Event> events = Event.allEvents.elements();
        while (events.hasMoreElements()){
            allSearchables.add(events.nextElement());
        }

        Enumeration<User> users = User.allUsers.elements();
        while (users.hasMoreElements()){
            allSearchables.add(users.nextElement());
        }

        Enumeration<Community> communities = Community.allCommunities.elements();
        while (communities.hasMoreElements()){
            allSearchables.add(communities.nextElement());
        }

        ArrayList<Searchable> found = new ArrayList<>();

        for (Searchable s : allSearchables) {
            boolean removed = false;
            if (!removed && location != null && s.findLocation(location)) {
                if (!found.contains(s)) {
                    found.add(s);
                }
            } else if (location != null && s instanceof Event) {
                found.remove(s);
                removed = true;
            }

            if (!removed && date != null && s.findDate(date)) {
                if (!found.contains(s)) {
                    found.add(s);
                }
            } else if (date != null) {
                found.remove(s);
                removed = true;
            }

            if (!removed && !organizer.isBlank() && s.findOrganizer(organizer)) {
                if (!found.contains(s)) {
                    found.add(s);
                }
            } else if (!organizer.isBlank()) {
                found.remove(s);
                removed = true;
            }

            if (!removed && !name.isBlank() && s.findName(name)) {
                if (!found.contains(s)) {
                    found.add(s);
                }
            } else if (!name.isBlank()) {
                found.remove(s);
                removed = true;
            }
        }
        return found;

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
