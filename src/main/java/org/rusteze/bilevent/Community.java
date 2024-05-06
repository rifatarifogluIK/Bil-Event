package org.rusteze.bilevent;

import javafx.scene.image.Image;

import java.time.LocalDate;
import java.util.ArrayList;

public class Community implements Searchable{

    private String name;
    private ArrayList<User> members;
    private ArrayList<User> adminList;
    private ArrayList<Event> currentEvents;
    private ArrayList<Event> pastEvents;
    private Image photo;
    private double rating;
    private int ratingCount;

    public Community(String name, Image photo) {
        this.name = name;
        this.members = new ArrayList<>();
        this.adminList = new ArrayList<>();
        this.currentEvents = new ArrayList<>();
        this.pastEvents = new ArrayList<>();
        this.photo = photo;
        this.rating = 0.0;
        this.ratingCount = 0;
    }

    public void addMember(User user){
        members.add(user);
    }

    public void setAdmin(User user){
        if(members.contains(user)){
            adminList.add(user);
        }
    }

    public void createEvent(String name, String description, LocalDate date){
        Event event = new CommunityEvent(this, name, description, date);
        adminList.forEach(admin -> event.getAdmins().add(admin));
        currentEvents.add(event);
    }

    public void handlePassedEvent(){
        boolean stop = false;

        for(int i = 0; i < currentEvents.size() && !stop; i++){
            Event current = currentEvents.get(i);
            if(current.getDate().isBefore(LocalDate.now())){
                currentEvents.remove(current);
                pastEvents.add(current);
                i--;
            }else{
                stop = true;
            }
        }
    }

    public void addNewRating(int rating){
        this.rating = ((this.rating * ratingCount++) + rating) / ratingCount;
    }

    @Override
    public boolean find(String key) {
        return this.name.equals(key);
    }

    public boolean isAdmin(User user) {
        return adminList.contains(user);
    }
    public String getName() {
        return name;
    }

    public ArrayList<User> getMembers() {
        return members;
    }

    public ArrayList<User> getAdminList() {
        return adminList;
    }

    public ArrayList<Event> getCurrentEvents() {
        return currentEvents;
    }

    public ArrayList<Event> getPastEvents() {
        return pastEvents;
    }

    public Image getPhoto() {
        return photo;
    }

    public double getRating() {
        return rating;
    }

    public int getRatingCount() {
        return ratingCount;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhoto(Image photo) {
        this.photo = photo;
    }
}
