package com.example.myapplication.ui;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.myapplication.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class login extends AppCompatActivity {
    //FirebaseAuth auth = FirebaseAuth.getInstance();
    public static final String SHARED_PREFS = "sharedprefs";
    public static final String TEXT = "text";
    private static final String TAG = "main";
    EditText emailid, password;
    Button btn1;
    TextView signup, verify;
    FirebaseAuth mFirebaseAuth;
    Toolbar toolbar;
    private static final int ERROR_DIALOG_REQUEST = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        //Objects.requireNonNull(getSupportActionBar()).hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //toolbar = findViewById(R.id.toolbar);
        //toolbar.setTitle("Login");

        mFirebaseAuth = FirebaseAuth.getInstance();
        emailid = findViewById(R.id.email);
        password = findViewById(R.id.pwd1);
        btn1 = findViewById(R.id.btn11);
        signup = findViewById(R.id.cid);
        verify = findViewById(R.id.sendverificationmail);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                {
                    final ProgressDialog progressDialog = new ProgressDialog(login.this);
                    final String email1 = emailid.getText().toString();
                    final String pwd1 = password.getText().toString();
                    getSupportActionBar().setTitle("Login");
                    //sharedPref = getPreferences(MODE_PRIVATE);
                    //SharedPreferences.Editor editor = sharedPref.edit();
                    //editor.putString("firebasekey","" );
                    //editor.apply();
                    isServicesOk();
                    if (pwd1.isEmpty() && email1.isEmpty()) {
                        Toast.makeText(login.this, "Fields are empty!", Toast.LENGTH_SHORT).show();
                    }
                    else if (pwd1.isEmpty()) {
                        password.setError("Enter your password");
                        password.requestFocus();
                    }
                    else if (email1.isEmpty()) {
                        emailid.setError("Provide an valid email address");
                        emailid.requestFocus();
                    }
                    else if (!(pwd1.isEmpty() && email1.isEmpty())) {
                        HideSoftKeyboard();
                        progressDialog.setTitle("Loading");
                        progressDialog.setMessage("Loading please wait!");
                        progressDialog.show();

                        btn1.setVisibility(View.GONE);
                        verify.setVisibility(View.GONE);
                        mFirebaseAuth.signInWithEmailAndPassword(email1, pwd1).addOnCompleteListener(login.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (!task.isSuccessful()) {
                                    Toast.makeText(login.this, "Login error, please try again!", Toast.LENGTH_SHORT).show();
                                    btn1.setVisibility(View.VISIBLE);
                                    progressDialog.dismiss();
                                } else {
                                    if (mFirebaseAuth.getCurrentUser().isEmailVerified()) {
                                        //String userId = mFirebaseAuth.getCurrentUser().getUid();
                                        //sharedPref = getPreferences(MODE_PRIVATE);
                                        //SharedPreferences.Editor editor = sharedPref.edit();
                                        //editor.putString("firebasekey", userId);
                                        //editor.apply();
                                        Save();
                                        btn1.setVisibility(View.VISIBLE);
                                        Toast.makeText(login.this, "You Are Logged in", Toast.LENGTH_SHORT).show();
                                        Intent intToHome = new Intent(login.this, main.class);
                                        startActivity(intToHome);
                                        progressDialog.dismiss();
                                    } else {
                                        Toast.makeText(login.this, "Please Verify Your Email!", Toast.LENGTH_SHORT).show();
                                        btn1.setVisibility(View.VISIBLE);
                                        verify.setVisibility(View.VISIBLE);
                                        progressDialog.dismiss();
                                    }
                                }
                            }
                        });
                    } else {
                        Toast.makeText(login.this, "Error Occurred!", Toast.LENGTH_SHORT).show();
                        btn1.setVisibility(View.VISIBLE);
                        progressDialog.dismiss();
                    }
                    btn1.setVisibility(View.VISIBLE);
                }
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                {
                    HideSoftKeyboard();
                    Intent xvf = new Intent(login.this, MainActivity.class);
                    startActivity(xvf);
                }
            }
        });
        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                {
                    HideSoftKeyboard();
                    mFirebaseAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(login.this, "Verification Mail Sent", Toast.LENGTH_SHORT).show();
                                verify.setVisibility(View.GONE);
                            } else if (!(task.isSuccessful())) {
                                Toast.makeText(login.this, "Verification Mail Failed", Toast.LENGTH_SHORT).show();
                                verify.setVisibility(View.VISIBLE);
                            }
                        }
                    });
                }
            }
        });
    }

    private void Save() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(TEXT, FirebaseAuth.getInstance().getCurrentUser().getUid());
        editor.apply();

    }

    private void HideSoftKeyboard(){
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public boolean isServicesOk() {
        Log.d(TAG, "isServicesOk: checking play services version");

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(login.this);

        if (available == ConnectionResult.SUCCESS) {
            //everything is fine and user can make map request
            Log.d(TAG, "isServicesOk: Google play services is working");
            return true;
        } else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)) {
            //an error occured but it is resolvable

            Log.d(TAG, "isServicesOk: an error occured b ut we can fix it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(login.this, available, ERROR_DIALOG_REQUEST);
            dialog.show();
        } else {
            Toast.makeText(this, "We can't connect with google play services!", Toast.LENGTH_SHORT).show();
        }
        return false;
    }
}
