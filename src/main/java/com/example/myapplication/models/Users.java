package com.example.myapplication.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class Users implements Parcelable {

    private @ServerTimestamp Date timestamp;
    private String fullname;
    private String username;
    private String email;
    private String gender;
    private int guide_coupons;
    private int coins;
    private int search_coupons;
    private boolean premium;

    public Users(Date timestamp, String fullname, String username, String email, String gender, int guide_coupons, int coins, int search_coupons, boolean premium) {
        this.timestamp = timestamp;
        this.fullname = fullname;
        this.username = username;
        this.email = email;
        this.gender = gender;
        this.guide_coupons = guide_coupons;
        this.coins = coins;
        this.search_coupons = search_coupons;
        this.premium = premium;
    }

    public Users() {
    }

    protected Users(Parcel in) {
        fullname = in.readString();
        username = in.readString();
        email = in.readString();
        gender = in.readString();
        guide_coupons = in.readInt();
        coins = in.readInt();
        search_coupons = in.readInt();
        premium = in.readByte() != 0;
    }

    public static final Creator<Users> CREATOR = new Creator<Users>() {
        @Override
        public Users createFromParcel(Parcel in) {
            return new Users(in);
        }

        @Override
        public Users[] newArray(int size) {
            return new Users[size];
        }
    };

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getGuide_coupons() {
        return guide_coupons;
    }

    public void setGuide_coupons(int guide_coupons) {
        this.guide_coupons = guide_coupons;
    }

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public int getSearch_coupons() {
        return search_coupons;
    }

    public void setSearch_coupons(int search_coupons) {
        this.search_coupons = search_coupons;
    }

    public boolean isPremium() {
        return premium;
    }

    public void setPremium(boolean premium) {
        this.premium = premium;
    }
    @Override
    public String toString() {
        return "Users{" +
                "timestamp=" + timestamp +
                ", fullname='" + fullname + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", gender='" + gender + '\'' +
                ", guide_coupons=" + guide_coupons +
                ", coins=" + coins +
                ", search_coupons=" + search_coupons +
                ", premium=" + premium +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(email);
        dest.writeString(fullname);
        dest.writeString(username);
        dest.writeString(gender);
        dest.writeInt(coins);
        dest.writeInt(search_coupons);
        dest.writeInt(guide_coupons);
        dest.writeBoolean(premium);
    }
}
