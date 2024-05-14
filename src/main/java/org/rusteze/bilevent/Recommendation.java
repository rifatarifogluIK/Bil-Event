package org.rusteze.bilevent;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;

public class Recommendation {

    private User user;
    private ArrayList<Event> eventRec;
    private ArrayList<Community> communityRec;

    public Recommendation(User user) {
        this.user = user;
        eventRec = new ArrayList<Event>();
        communityRec = new ArrayList<Community>();
        recommend();
    }

    public void recommend(){
        if(user.getAttendedEvents().size() + user.getEnrolledEvents().size() == 0){
            return;
        }

        ArrayList<Event> enrollableEvents = new ArrayList<>();

        Enumeration<Event> allEvents = Event.allEvents.elements();
        while (allEvents.hasMoreElements()){
            Event temp = allEvents.nextElement();
            if(temp.getDate().isAfter(LocalDate.now()) && !(user.getAttendedEvents().contains(temp) || user.getEnrolledEvents().contains(temp))){
                enrollableEvents.add(temp);
            }
        }

        int[] enrolledCalculation = new int[CreateEventController.attributeList.length];
        boolean[][] allCalculation = new boolean[enrollableEvents.size()][];

        for(int i = 0; i < enrollableEvents.size(); i++){
            allCalculation[i] = calculateEvent(enrollableEvents.get(i));
        }

        for(int i = 0; i < user.getEnrolledEvents().size(); i++){
            boolean[] temp = calculateEvent(user.getEnrolledEvents().get(i));
            for(int j = 0; j < CreateEventController.attributeList.length; j++){
                enrolledCalculation[j] += (temp[j]) ? 1 : 0;
            }
        }

        for(int i = user.getEnrolledEvents().size(); i < user.getAttendedEvents().size() + user.getEnrolledEvents().size(); i++){
            boolean[] temp = calculateEvent(user.getAttendedEvents().get(i - user.getEnrolledEvents().size()));
            for(int j = 0; j < CreateEventController.attributeList.length; j++){
                enrolledCalculation[j] += (temp[j]) ? 1 : 0;
            }
        }

        for(int i = 0; i < CreateEventController.attributeList.length; i++){
            enrolledCalculation[i] /= user.getAttendedEvents().size() + user.getEnrolledEvents().size();
        }

        for(int i = 0; i < allCalculation.length; i++){
            if(isAcceptable(allCalculation[i], enrolledCalculation)){
                eventRec.add(enrollableEvents.get(i));
            }
        }
    }

    public boolean[] calculateEvent(Event event){
        boolean[] calculation = new boolean[CreateEventController.attributeList.length];
        Arrays.fill(calculation, false);

        for(int i = 0; i < calculation.length; i++){
            calculation[i] = event.getAttributes().contains(CreateEventController.attributeList[i]);
        }

        return calculation;
    }

    public boolean isAcceptable(boolean[] event, int[] calculation){
        boolean result = true;

        for(int i = 0; i < calculation.length; i++){
            if(event[i] && calculation[i] < 0.3){
                result = false;
            }else if (!event[i] && calculation[i] > 0.3) {
                result = false;
            }
        }

        return result;
    }

    public User getUser() {
        return user;
    }

    public ArrayList<Event> getEventRec() {
        return eventRec;
    }

    public ArrayList<Community> getCommunityRec() {
        return communityRec;
    }
}
