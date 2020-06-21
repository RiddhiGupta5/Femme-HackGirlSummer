package com.riddhi.women_safety_app;

public class Request {

    private String id;
    private String sender_id;
    private int sender;
    private String receiver_id;
    private int is_approved;
    private String journey_id;
    private String sender_name;
    private String start;
    private String date;
    private String time;
    private String destination;

    public Request(String id, int sender, String sender_id, String receiver_id, int is_approved, String journey_id, String sender_name, String start, String destination, String date, String time) {
        this.id = id;
        this.sender_id = sender_id;
        this.sender = sender;
        this.receiver_id = receiver_id;
        this.is_approved = is_approved;
        this.journey_id = journey_id;
        this.sender_name = sender_name;
        this.start = start;
        this.destination = destination;
        this.date = date;
        this.time = time;
    }

    public Request() {
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

    public String getReceiver_id() {
        return receiver_id;
    }

    public int getIs_approved() {
        return is_approved;
    }

    public String getJourney_id() {
        return journey_id;
    }

    public String getSender_name() {
        return sender_name;
    }

    public String getStart() {
        return start;
    }

    public String getDestination() {
        return destination;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }
}
