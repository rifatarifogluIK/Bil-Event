package org.rusteze.bilevent;

import org.bson.types.ObjectId;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;

public interface Searchable {
    public static ArrayList<Searchable> allSearchables = new ArrayList<Searchable>();
    public boolean find(String key);
    public static ArrayList<Searchable> search(String name, String organizer, String location, LocalDate date){
        ArrayList<Searchable> founded = new ArrayList<>();

        for(Searchable s: allSearchables)
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

        return founded;
    }
}
