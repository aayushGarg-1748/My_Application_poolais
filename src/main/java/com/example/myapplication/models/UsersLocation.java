package com.example.myapplication.models;

import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class UsersLocation {

    private GeoPoint geo_point;
    private @ServerTimestamp Date timestamp;
    private Users user;

    public UsersLocation() {

    }

    @Override
    public String toString() {
        return "UsersLocation{" +
                "geo_point=" + geo_point +
                ", timestamp=" + timestamp +
                ", user=" + user +
                '}';
    }

    public GeoPoint getGeo_point() {
        return geo_point;
    }

    public void setGeo_point(GeoPoint geo_point) {
        this.geo_point = geo_point;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public UsersLocation(GeoPoint geo_point, Date timestamp, Users user) {
        this.geo_point = geo_point;
        this.timestamp = timestamp;
        this.user = user;
    }
}
