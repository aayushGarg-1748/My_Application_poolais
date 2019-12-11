package com.example.myapplication.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    EditText emailId, password, confirmPassword, txt_username, txt_fullName;
    Button btn;
    TextView signIn;
    String gender;
    private RadioButton radioButton;
    private RadioButton radioButton1;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore Post;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //hides actionbar
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_signup);

        //create a toolbar to overlook actionbar
        Toolbar toolbar = findViewById(R.id.toolbar2);
        toolbar.setTitle("SignUp");

        //gets the signUp time
        final Intent i = new Intent(MainActivity.this, login.class);
        final Date currentTime = Calendar.getInstance().getTime();

        //all the declarations
        init();

        //defines a progress dialog

        //set an on click listener on the button(SignUp)
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                {
                    //strings
                    final String email = emailId.getText().toString();
                    final String pwd = password.getText().toString();
                    final String con = confirmPassword.getText().toString();
                    final String fullName = txt_fullName.getText().toString();
                    final String username = txt_username.getText().toString();
                    final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);

                    //valid email pattern
                    final String validate =
                            "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                                    + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                                    + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                                    + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                                    + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                                    + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";

                    //valid email verification
                    Matcher matcher = Pattern.compile(validate).matcher(email);

                    //map to send data into the database
                    final HashMap<String, Object> map = new HashMap<>();
                    map.put("username", txt_username.getText().toString());
                    map.put("fullname", txt_fullName.getText().toString());
                    map.put("email", emailId.getText().toString());
                    map.put("date", currentTime.toString());

                    //conditions to check if the gender is not empty
                    if (radioButton.isChecked()) {
                        gender = "Female";
                    } else if (radioButton1.isChecked()) {
                        gender = "Male";
                    } else {
                        Toast.makeText(MainActivity.this, "Gender Is Not Selected!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    //updates gender and then also adds that to the first map
                    final HashMap<String, String> map1 = new HashMap<>();
                    map1.put("gender", gender);

                    final HashMap<String, Integer> map2 = new HashMap<>();
                    map2.put("coins",0);
                    map2.put("credits", 100);

                    final HashMap<String, Boolean> map3 = new HashMap<>();
                    map3.put("premium",false);

                    map.putAll(map1);
                    map.putAll(map2);
                    map.putAll(map3);

                    //checks if anything is written at all in the fields
                    if (pwd.isEmpty() && email.isEmpty() && con.isEmpty() && username.isEmpty() && fullName.isEmpty()) {
                        Toast.makeText(MainActivity.this, "Fields are empty!", Toast.LENGTH_SHORT).show();
                    }
                    //if full name is not written
                    else if (fullName.isEmpty()) {
                        txt_fullName.setError("Please Enter Your Full Name");
                        txt_fullName.requestFocus();

                    }
                    //if username is not written
                    else if (username.isEmpty()) {
                        txt_username.setError("Please Enter A Valid Username!");
                        txt_username.requestFocus();
                    }
                    //if email is empty
                    else if (email.isEmpty()) {
                        emailId.setError("Provide an valid email address");
                        emailId.requestFocus();
                    }
                    //if pwd is empty
                    else if (pwd.isEmpty()) {
                        password.setError("Enter your password");
                        password.requestFocus();
                    }
                    //if confirm pwd is empty
                    else if (con.isEmpty()) {
                        confirmPassword.setError("Enter confirm password");
                        confirmPassword.requestFocus();
                    }
                    //if the password are same
                    else if (!(pwd.contentEquals(con))) {
                        confirmPassword.setError("passwords don't match!");
                        confirmPassword.requestFocus();
                    }
                    //if the pattern of valid email is != to entered email
                    else if (!(matcher.matches())) {
                        emailId.setError("email is not valid");
                        emailId.requestFocus();
                    }
                    //checks if the pwd matches confirm pwd

                    else if (pwd.contentEquals(con)) {

                        //double checks if the pwd and email are not empty
                        if (!(pwd.isEmpty() && email.isEmpty())) {

                            //shows a progress dialog to keep user in the app
                            progressDialog.setTitle("Loading");
                            progressDialog.setMessage("Loading please wait!");
                            progressDialog.show();

                            //removes the button
                            btn.setVisibility(View.GONE);

                            //creates an user with Email And Pwd.
                            firebaseAuth.createUserWithEmailAndPassword(email, pwd).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {

                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    //checks if the task is completed or not
                                    //if not
                                    if (!task.isSuccessful()) {
                                        //removes the progress dialog
                                        progressDialog.dismiss();
                                        verification();
                                    }

                                    // if yes
                                    else {

                                        try {

                                            DocumentReference locationref = Post.collection("Users")
                                                    .document (FirebaseAuth.getInstance().getCurrentUser().getUid());
                                                    //uploads the map
                                                    locationref.set(map)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            //removes the progress dialog
                                                            progressDialog.dismiss();
                                                            //sends verification mail
                                                            firebaseAuth.getCurrentUser()
                                                                    .sendEmailVerification()
                                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                        @Override
                                                                        //if task is successful
                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                            //makes a toast
                                                                            try{
                                                                                Toast.makeText(MainActivity.this, "Signup successful " + "verification mail sent to your mail", Toast.LENGTH_SHORT).show();

                                                                                FirebaseAuth.getInstance().signOut();
                                                                                //goes to login
                                                                                startActivity(i);
                                                                                //makes the button  visible
                                                                                btn();
                                                                            }
                                                                            catch (NullPointerException e){
                                                                                Toast.makeText(MainActivity.this, "app failed", Toast.LENGTH_SHORT).show();
                                                                            }
                                                                        }
                                                                    }).addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception e) {
                                                                    //makes a toast
                                                                    Toast.makeText(MainActivity.this, "SignUp failed! ", Toast.LENGTH_SHORT).show();
                                                                    //makes the button visible
                                                                    btn();
                                                                }
                                                            });
                                                        }
                                                    });

                                        } catch (NullPointerException e) {
                                            Toast.makeText(MainActivity.this, "app failed", Toast.LENGTH_SHORT).show();
                                        }
                                        //creates a id of the user in the database users section with his uid

                                    }
                                }
                            });
                        }
                        //just in case
                        else {
                            Toast.makeText(MainActivity.this, "Error Ocuured! ", Toast.LENGTH_SHORT).show();
                            btn();
                        }
                    }

                    //just in case
                    else {
                        confirmPassword.setError("passwords don't match!");
                        confirmPassword.requestFocus();
                    }
                }
            }
        });

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(i);
            }
        });


    }

    public void verification() {

        Toast.makeText(MainActivity.this, "SignUp Failed!", Toast.LENGTH_SHORT).show();
        btn.setVisibility(View.VISIBLE);
    }

    public void btn() {
        btn.setVisibility(View.VISIBLE);

    }

    public void init() {

        firebaseAuth = FirebaseAuth.getInstance();
        emailId = findViewById(R.id.emailsignup);
        password = findViewById(R.id.password1);
        txt_username = findViewById(R.id.UserName);
        txt_fullName = findViewById(R.id.Fullname);
        confirmPassword = findViewById(R.id.confirmpassword);
        btn = findViewById(R.id.signup);
        signIn = findViewById(R.id.useless);
        Post = FirebaseFirestore.getInstance();
        radioButton = findViewById(R.id.female);
        radioButton1 = findViewById(R.id.male);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    }


}
