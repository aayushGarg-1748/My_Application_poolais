package com.example.myapplication.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import static com.example.myapplication.Clients.Constants.SHARED_PREFS;
import static com.example.myapplication.Clients.Constants.TEXT;

public class user_info extends AppCompatActivity {
    FirebaseFirestore Post;
    TextView username1, fullname1, gender1, email1;
    ImageView edit;
    String fullnam="";
    String gende = "";
    String email = "";
    String Userid = "";
    private static final String TAG = "user_info";
    private String UserId;
    //private RecyclerView recyclerView;
    //private usersAdapter adapter;
    Button userLogout;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        loaddata();

        fullname1 = findViewById(R.id.fullNameuserinfo);
        username1 = findViewById(R.id.userNameuserinfo);
        email1 = findViewById(R.id.emailuserinfo);
        gender1 = findViewById(R.id.genderuserinfo);
        userLogout = findViewById(R.id.userLogout);
        //sharedPref = getPreferences(MODE_PRIVATE);
        //String UserId = sharedPref.getString("firebasekey", "");

        Post = FirebaseFirestore.getInstance();
        DocumentReference locationref = Post.collection("Users")
                .document (UserId);

        locationref.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                {
                    if (e != null){
                        Log.d(TAG, "onEvent:  error"+ e.getMessage());
                    }
                    if (documentSnapshot.exists()) {
                        email = ("Email : " + documentSnapshot.getString("email"));
                        gende = ("Gender : " + documentSnapshot.getString("gender"));
                        fullnam = ("FullName : " + documentSnapshot.getString("fullname"));
                        Userid = ("UserName : " + documentSnapshot.getString("username"));

                        Log.d(TAG, "onEvent: DocumentSnapshot called" );
                        fullname1.setText(fullnam);
                        username1.setText(Userid);
                        gender1.setText(gende);
                        email1.setText(email);
                    }
                }
            }
        });
        edit = findViewById(R.id.Edit);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(user_info.this, UserId, Toast.LENGTH_SHORT).show();
            }
        });

        //recyclerView = (RecyclerView) findViewById(R.id.recycler);
        //recyclerView.setLayoutManager(new LinearLayoutManager(this));


        //FirebaseRecyclerOptions<Users> options =
        //      new FirebaseRecyclerOptions.Builder<Users>()
        //            .setQuery(Post, Users.class)
        //          .build();

        //adapter = new usersAdapter(options, this);

        //recyclerView.setAdapter(adapter);
        // }

        //@Override
        //protected void onStart() {
        //  super.onStart();
        //adapter.startListening();
        //}

        //@Override
        //protected void onStop() {
        // super.onStop();
        //adapter.stopListening();
// }

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        userLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(user_info.this , MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });


    }

    private void loaddata() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        UserId = sharedPreferences.getString(TEXT, "");
    }
}

