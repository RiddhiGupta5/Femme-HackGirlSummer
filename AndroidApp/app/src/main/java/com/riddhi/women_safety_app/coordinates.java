package com.riddhi.women_safety_app;

class coordinates {

    private double latitude;
    private double longitude;
    private double distance;

    public coordinates() {
    }

    public coordinates(double latitude, double longitude, double distance) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.distance = distance;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getDistance() {
        return distance;
    }
}
