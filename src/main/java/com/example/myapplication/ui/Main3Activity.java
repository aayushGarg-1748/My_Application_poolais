package com.example.myapplication.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class Main3Activity extends AppCompatActivity {

    FirebaseAuth firebaseAuth1 = FirebaseAuth.getInstance();
    final FirebaseUser firebaseUser = firebaseAuth1.getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_main3);

        // checks for a pre existing signIn if found directs to login
        if (firebaseUser != null){
            startActivity(new Intent(Main3Activity.this, user_info.class));
        }
        else {
            startActivity(new Intent(Main3Activity.this, MainActivity.class));
        }
    }
}
