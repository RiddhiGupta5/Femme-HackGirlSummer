package com.riddhi.women_safety_app;

import java.util.ArrayList;
import java.util.List;

public class Journey {

    private String id;
    private String owner_id;
    //Todo: Number of Passengers allowed and already boarded passengers
    private String start;
    private String destination;
    private List<coordinates> path_coordinates;
    private List<String> passenger_id;
    private int sent_invite;
    private String date;
    private String time;

    public Journey() {
    }

    public Journey(String id, String owner_id, String start, String destination, String date, String time) {
        this.id = id;
        this.owner_id = owner_id;
        this.start = start;
        this.destination = destination;
        this.sent_invite = 0;
        this.passenger_id = new ArrayList<>();
        this.path_coordinates = new ArrayList<>();
        this.date = date;
        this.time = time;
    }

    public Journey(String id, String owner_id, String start, String destination, List<coordinates> path_coordinates, String date, String time) {
        this.id = id;
        this.owner_id = owner_id;
        this.start = start;
        this.destination = destination;
        this.path_coordinates = path_coordinates;
        this.passenger_id = new ArrayList<>();
        this.sent_invite = 0;
        this.date = date;
        this.time = time;
    }

    public Journey(String id, String owner_id, String start, String destination, List<coordinates> path_coordinates, List<String> passenger_id, String date, String time) {
        this.id = id;
        this.owner_id = owner_id;
        this.start = start;
        this.destination = destination;
        this.path_coordinates = path_coordinates;
        this.passenger_id = passenger_id;
        this.sent_invite = 0;
        this.date = date;
        this.time = time;
    }

    public void addPathCoordinates(coordinates c){
        if(path_coordinates==null){
            path_coordinates = new ArrayList<>();
        }
        path_coordinates.add(c);
    }

    public void addPassengerId(String id){
        if(passenger_id==null){
            passenger_id = new ArrayList<>();
        }
        passenger_id.add(id);
    }

    public String getId() {
        return id;
    }

    public String getOwner_id() {
        return owner_id;
    }

    public String getStart() {
        return start;
    }

    public String getDestination() {
        return destination;
    }

    public List<coordinates> getPath_coordinates() {
        return path_coordinates;
    }

    public List<String> getPassenger_id() {
        return passenger_id;
    }

    public int getSent_invite() {
        return sent_invite;
    }

    public void setSent_invite(int sent_invite) {
        this.sent_invite = sent_invite;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }
}
