package com.example.myapplication.ui;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.models.place_data_model_class;

public class buy_premium_membership extends AppCompatActivity {


    private static final int AUTOCOMPLETE_REQUEST_CODE = 1;
    private static final String api_key = "AIzaSyBoxnqKGM8Qlawzj1U_ZUa5TqsOqQGugMk";
    private place_data_model_class mPlace;
    private static final String TAG = "buy_premium_membership";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_premium_membership);

        Button button = findViewById(R.id.button);
        Button button1 = findViewById(R.id.button1);
    }

}
