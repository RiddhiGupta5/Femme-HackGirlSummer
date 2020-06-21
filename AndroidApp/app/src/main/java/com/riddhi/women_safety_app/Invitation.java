package com.riddhi.women_safety_app;

import java.util.ArrayList;
import java.util.List;

public class Invitation {

    private String id;
    private String sender_id;
    private int sender;
    private String start;
    private String destination;
    private String sender_name;
    private String journey_id;
    private List<String> sent_request;
    private String date;
    private String time;


    public Invitation() {
    }

    public Invitation(String sender_id, int sender, String start, String destination, String sender_name, String id, String date, String time){
        this.sender_id = sender_id;
        this.sender = sender;
        this.destination = destination;
        this.start = start;
        this.sender_name = sender_name;
        this.id = id;
        this.sent_request = new ArrayList<>();
        this.date = date;
        this.time = time;
    }

    public Invitation(String sender_id, int sender, String start, String destination, String sender_name, String id, String journey_id, String date, String time){
        this.sender_id = sender_id;
        this.sender = sender;
        this.destination = destination;
        this.start = start;
        this.sender_name = sender_name;
        this.id = id;
        this.journey_id = journey_id;
        this.sent_request = new ArrayList<>();
        this.date = date;
        this.time = time;
    }

    public Invitation(String id, String sender_id, int sender, String start, String destination, String sender_name, String journey_id, List<String> sent_request, String date, String time) {
        this.id = id;
        this.sender_id = sender_id;
        this.sender = sender;
        this.start = start;
        this.destination = destination;
        this.sender_name = sender_name;
        this.journey_id = journey_id;
        this.sent_request = sent_request;
        this.date = date;
        this.time = time;
    }

    public void addSentRequest(String id){
        if(sent_request==null){
            this.sent_request = new ArrayList<>();
        }
        sent_request.add(id);
    }

    public String getId() {
        return id;
    }

    public String getSender_id() {
        return sender_id;
    }

    public int getSender() {
        return sender;
    }

    public String getStart() {
        return start;
    }

    public String getDestination() {
        return destination;
    }

    public String getJourney_id() {
        return journey_id;
    }

    public String getSender_name() {
        return sender_name;
    }

    public List<String> getSent_request(){return sent_request;}

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }
}
