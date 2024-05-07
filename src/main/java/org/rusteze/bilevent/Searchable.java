package org.rusteze.bilevent;

import java.util.ArrayList;

public interface Searchable {
    public boolean find(String key);
    public static ArrayList<Searchable> search(String key, ArrayList<Searchable> range){
        ArrayList<Searchable> founded = new ArrayList<>();

        for(Searchable element : range){
            if(element.find(key)){
                founded.add(element);
            }
        }

        return founded;
    }
}
