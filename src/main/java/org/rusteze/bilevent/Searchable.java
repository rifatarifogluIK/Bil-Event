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
        if(!organizer.equals(""))
        {
            for(Searchable s: allSearchables)
            {
                User organ = (User) s;
                if(organ.find(organizer))
                {
                    founded.add(s);
                }

            }
        }
        if(!name.equals(""))
        {
            for(Searchable s: allSearchables)
            {
                Event event = (Event)s;
                if( event.getName().equals(name))
                {
                    founded.add(event);
                }
            }
        }
        if(location != null || date != null)
        {
            for(Searchable s: allSearchables)
            {
                Event event = (Event)s;
                if(event instanceof PersonalEvent personalEvent)
                {
                    if(personalEvent.find(personalEvent.getName()))
                        founded.add(personalEvent);
                }
                else if( event instanceof CommunityEvent communityEvent)
                {
                    if(communityEvent.find(communityEvent.getName()))
                        founded.add(communityEvent);
                }
            }
        }

        return founded;
    }
}
