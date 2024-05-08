package org.rusteze.bilevent;

import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;

public interface Searchable {
    public static ArrayList<Searchable> allSearchables = new ArrayList<Searchable>();
    public boolean find(String key);
    public static ArrayList<Searchable> search(String key){
        ArrayList<Searchable> founded = new ArrayList<>();

        for(Searchable element : allSearchables){
            if(element.find(key)){
                founded.add(element);
            }
        }

        return founded;
    }
}
