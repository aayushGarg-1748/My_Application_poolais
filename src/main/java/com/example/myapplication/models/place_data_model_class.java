package com.example.myapplication.models;

import android.net.Uri;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.model.OpeningHours;

public class place_data_model_class {

    private String id, name, address, phoneNumber;
    private Uri websiteUri;
    private LatLng latLng;
    private float userRatings;
    private double rating;
    private float priceRating;

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public place_data_model_class(double rating) {
        this.rating = rating;
    }

    private OpeningHours opening_hours;

    public OpeningHours getOpening_hours() {
        return opening_hours;
    }

    public void setOpening_hours(OpeningHours opening_hours) {
        this.opening_hours = opening_hours;
    }

    public place_data_model_class(OpeningHours opening_hours) {
        this.opening_hours = opening_hours;
    }

    public place_data_model_class() {
    }

    public place_data_model_class(String id, String name, String address, String phoneNumber, Uri websiteUri, LatLng latLng, float userRatings, float priceRating) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.websiteUri = websiteUri;
        this.latLng = latLng;
        this.userRatings = userRatings;
        this.priceRating = priceRating;

     }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Uri getWebsiteUri() {
        return websiteUri;
    }

    public void setWebsiteUri(Uri websiteUri) {
        this.websiteUri = websiteUri;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public float getUserRatings() {
        return userRatings;
    }

    public void setUserRatings(float userRatings) {
        this.userRatings = userRatings;
    }

    public float getPriceRating() {
        return priceRating;
    }

    public void setPriceRating(float priceRating) {
        this.priceRating = priceRating;
    }

    @Override
    public String toString() {
        return "place_data_model_class{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", websiteUri=" + websiteUri +
                ", latLng=" + latLng +
                ", userRatings=" + userRatings +
                ", rating=" + rating +
                ", priceRating=" + priceRating +
                ", opening_hours=" + opening_hours +
                '}';
    }
}
