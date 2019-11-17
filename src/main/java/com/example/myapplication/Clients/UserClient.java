package com.example.myapplication.Clients;

import android.app.Application;

import com.example.myapplication.models.Users;


public class UserClient extends Application {

    private Users user = null;

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

}