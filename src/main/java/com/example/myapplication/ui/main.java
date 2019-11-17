package com.example.myapplication.ui;

import android.Manifest;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.myapplication.R;
import com.example.myapplication.Services.LocationService;
import com.example.myapplication.Clients.UserClient;
import com.example.myapplication.models.Users;
import com.example.myapplication.models.UsersLocation;
import com.example.myapplication.models.place_data_model_class;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static com.example.myapplication.Clients.Constants.AUTOCOMPLETE_REQUEST_CODE;
import static com.example.myapplication.Clients.Constants.DEFAULT_ZOOM;
import static com.example.myapplication.Clients.Constants.LOCATION_PERMISSION_REQUEST_CODE;
import static com.example.myapplication.Clients.Constants.PERMISSIONS_REQUEST_ENABLE_GPS;
import static com.example.myapplication.Clients.Constants.SHARED_PREFS;
import static com.example.myapplication.Clients.Constants.TEXT;
import static com.example.myapplication.Clients.Constants.api_key;
import static com.example.myapplication.Clients.Constants.location_coarse;
import static com.example.myapplication.Clients.Constants.location_fine;
import static com.example.myapplication.Services.LocationService.Userid2;

public class main extends AppCompatActivity implements OnMapReadyCallback {

    private static final String TAG = "main";
    String dev = "Devloper Toast: ";
    View mapView;

    //objects
    Location currentLocation;
    GoogleMap mMap;
    Button driving, carpooling;
    TextView drive_editText, carpool_editText;
    RelativeLayout bottom_rel_lay_2, relativeLayout;
    UsersLocation mUserLocation;
    String Userid = "";
    private place_data_model_class mPlace;
    private FirebaseFirestore mDb = FirebaseFirestore.getInstance();

    //vars
    private Boolean mLocationPermissionGranted = false;
    private Boolean Goggle_service = false;
    private Boolean Google_service = false;
    private FusedLocationProviderClient mFusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        getLocationPermission();
        loadData();
        if (checkMapServices()) {
            Log.d(TAG, "onCreate: Map sercies is already enabled");
        } else {
            Log.d(TAG, "onCreate: gps is not enabled should prompt an error ");
        }
    }

    /* ----------------------------------------------MAP------------------------------------------------------------------------------------------------------------------------------------------------------ */

    //initializing map
    private void initMap() {
        final SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapView = mapFragment.getView();
        Objects.requireNonNull(mapFragment).getMapAsync(main.this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Toast.makeText(this,
                dev + "map is ready", Toast.LENGTH_SHORT).show();
        if (mLocationPermissionGranted) {
            getDeviceLocation();
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                mMap.setMyLocationEnabled(true);
            }
            init();
            getUserDetails();
        } else {
            Toast.makeText(this, "app failed" + "login again", Toast.LENGTH_SHORT).show();
        }
    }

    //main thing
    private void init() {
        Log.d(TAG, "init: initializing");
        HideSoftKeyboard();

        //relative layouts
        bottom_rel_lay_2 = findViewById(R.id.bottom_rel_layout_2);
        relativeLayout = findViewById(R.id.relLayout1);

        //buttons
        driving = findViewById(R.id.driving);
        carpooling = findViewById(R.id.carpooling);

        //edit text layouts
        drive_editText = findViewById(R.id.drive_editText);
        carpool_editText = findViewById(R.id.carpool_editText);

        if (!Places.isInitialized()) {
            Places.initialize(this, api_key);
        }

        mPlace = new place_data_model_class();

        driving.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Userid2 = null;
                //UserId = null;

                //getUserDetails();
                drive_editText.setText(R.string.w);
                relativeLayout.setVisibility(View.VISIBLE);
                drive_editText.setVisibility(View.VISIBLE);
                carpool_editText.setText(R.string.w);
                carpool_editText.setVisibility(View.GONE);
                bottom_rel_lay_2.setVisibility(View.GONE);

                if (getIntentToCall()) {

                }
                if (getDialogIntentToCall()){

                }
            }
        });


        carpooling.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Userid2 = null;
                //UserId = null;
                //getUserDetails();
                carpool_editText.setText(R.string.w);
                relativeLayout.setVisibility(View.VISIBLE);
                carpool_editText.setVisibility(View.VISIBLE);
                drive_editText.setText(R.string.w);
                drive_editText.setVisibility(View.GONE);

                if (getCarpoolDialogIntentToCall()) {

                }

            }
        });

    }


    private boolean getDialogIntentToCall() {
        new AlertDialog.Builder(main.this)
                .setTitle("Do you want to drive?")
                .setMessage("Do you want to use 10 drive coupons?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {
                        loadData();
                        final DocumentReference reference = mDb.collection("Users").document(Userid);
                        reference.get()
                                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        if (documentSnapshot != null) {
                                            int search = (documentSnapshot.getLong("search_coupons").intValue());
                                            if (search > 10) {
                                                int guide_cop = search - 10;
                                                reference.update("guide_coupons", guide_cop).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            Toast.makeText(main.this, "Enough coupons were available! 10 were taken for driving", Toast.LENGTH_SHORT).show();
                                                            Log.d(TAG, "onComplete: everything seems fine");
                                                            startLocationServiceDriving();
                                                        } else {
                                                            Toast.makeText(main.this, "Error Occurred", Toast.LENGTH_SHORT).show();
                                                            Log.d(TAG, "onComplete: nothing is working");

                                                        }
                                                    }
                                                });
                                            } else {
                                                Toast.makeText(main.this, "You do not have enough guide coupons", Toast.LENGTH_SHORT).show();
                                            }
                                        }else {
                                            Toast.makeText(main.this, dev + "please retry", Toast.LENGTH_SHORT).show();
                                        }
                                        dialog.dismiss();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });

                    }
                }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                reset();
                dialog.dismiss();
            }
        }).show();
        return true;
        }

    private boolean getCarpoolDialogIntentToCall() {
        new AlertDialog.Builder(main.this)
                .setTitle("Carpool Too This Place")
                .setMessage("Do you want to Carpool To This Location ?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {
                        loadData();
                        final DocumentReference reference = mDb.collection("Users").document(Userid);
                        reference.get()
                                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        if (documentSnapshot != null) {
                                            int search = (documentSnapshot.getLong("search_coupons").intValue());
                                            if (search > 10) {
                                                int guide_cop = search - 10;
                                                reference.update("search_coupons", guide_cop).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            Toast.makeText(main.this, "Enough coupons were available! 10 were taken for carpooling", Toast.LENGTH_SHORT).show();
                                                            Log.d(TAG, "onComplete: everything seems fine");
                                                            startLocationServiceDriving();
                                                        } else {
                                                            Toast.makeText(main.this, "Error Occurred", Toast.LENGTH_SHORT).show();
                                                            Log.d(TAG, "onComplete: nothing is working");

                                                        }
                                                    }
                                                });
                                            } else {
                                                Toast.makeText(main.this, "You do not have enough search coupons", Toast.LENGTH_SHORT).show();
                                            }
                                        }else {
                                            Toast.makeText(main.this, dev + "failed", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });

                    }
                }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                reset();
                dialog.dismiss();

            }
        }).show();

        return true;
    }

    //get google intent to display
    private Boolean getIntentToCall() {
        // Set the fields to specify which types of place data to
        // return after the user has made a selection
        List<Place.Field> fields = Arrays.asList(
                Place.Field.NAME, Place.Field.LAT_LNG,
                Place.Field.PHONE_NUMBER, Place.Field.ADDRESS,
                Place.Field.OPENING_HOURS, Place.Field.ID,
                Place.Field.WEBSITE_URI);

        // Start the autocomplete intent.
        Intent intent = new Autocomplete.IntentBuilder(
                AutocompleteActivityMode.FULLSCREEN, fields)
                .build(this);
        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
        return true;
    }

    //get data from the intent
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            final Status status = Autocomplete.getStatusFromIntent(data);

            if (resultCode == RESULT_OK) {

                Place place = Autocomplete.getPlaceFromIntent(data);

                try {
                    Log.d(TAG, "onActivityResult: " + place.toString());
                    mPlace.setName(place.getName());
                    mPlace.setId(place.getId());
                    mPlace.setAddress(place.getAddress());
                    mPlace.setLatLng(place.getLatLng());
                    mPlace.setPhoneNumber(place.getPhoneNumber());
                    mPlace.setOpening_hours(place.getOpeningHours());
                    mPlace.setWebsiteUri(place.getWebsiteUri());

                    drive_editText.setText(place.getAddress());
                    carpool_editText.setText(place.getAddress());

                    if (place.getName() != null) {
                        getDialogIntentToCall();
                    }

                    moveCamera(place.getLatLng(), DEFAULT_ZOOM, place.getName());
                    Log.i(TAG, "Place: " + mPlace.toString());
                } catch (NullPointerException e) {
                    Toast.makeText(this, "error" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                super.onActivityResult(requestCode, resultCode, data);
                Log.i(TAG, status.getStatusMessage());
                Toast.makeText(this, "developer toast: " + "QUERY_OVER_LIMIT", Toast.LENGTH_SHORT).show();
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }

    //move camera method
    private void moveCamera(LatLng latLng, float zoom, String title) {
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
        HideSoftKeyboard();

        if (!(title.equals("My location"))) {

            MarkerOptions options = new MarkerOptions()
                    .position(latLng)
                    .title(title);
            mMap.addMarker(options);
        }
    }

    //get your location method
    private void getDeviceLocation() {
        Log.d(TAG, "getDeviceLocation: getting devices current location ");
        FusedLocationProviderClient mfusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        try {
            if (mLocationPermissionGranted) {
                Task location = mfusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "onComplete: found location!");
                            currentLocation = (Location) task.getResult();
                            Toast.makeText(main.this, dev + currentLocation.toString(), Toast.LENGTH_LONG).show();
                            moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), DEFAULT_ZOOM, "My location");
                        } else {
                            Log.d(TAG, "onComplete: current location is null");
                        }
                    }
                });
            }

        } catch (SecurityException e) {
            Log.d(TAG, "getDeviceLocation: SecurityException: " + e.getMessage());
        }

    }

    //get last known location
    private void getLastKnownLocation() {
        Log.d(TAG, "getLastKnownLocation: called.");

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mFusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<android.location.Location>() {
            @Override
            public void onComplete(@NonNull Task<android.location.Location> task) {
                if (task.isSuccessful()) {
                    Location location = task.getResult();
                    Log.d(TAG, "onComplete: Loacation: " + location);
                    GeoPoint geoPoint = new GeoPoint(location.getLatitude(), location.getLongitude());
                    mUserLocation.setGeo_point(geoPoint);
                    mUserLocation.setTimestamp(null);
                    saveUserLocation();
                }
            }
        });

    }

/* --------------------------------------------------MENU-BAR--------------------------------------------------------------------------------------------------------------------------------------------- */

    //an menu item
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            case R.id.user_1:
                startActivity(new Intent(main.this, user_info.class));
                return true;
            case R.id.item2:
                startActivity(new Intent(main.this, available_coupons.class));
                return true;
            case R.id.item4:
                startActivity(new Intent(main.this, buy_premium_membership.class));
                return true;
            case R.id.item5:
                startActivity(new Intent(main.this, user_info.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //adding menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.example_menu, menu);
        return true;
    }

/* --------------------------------------------------PERMISSIONS--------------------------------------------------------------------------------------------------------------------------------------------- */

    //requesting phone the permission of location
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;

        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) ;
                    }
                }
                mLocationPermissionGranted = true;
                initMap();
                getUserDetails();
            }
        }
    }

    private void getLocationPermission() {
        String[] permissions = {Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION};

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                location_coarse) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    location_fine) == PackageManager.PERMISSION_GRANTED) {

                //set a boolean
                mLocationPermissionGranted = true;
                initMap();
                getUserDetails();
            } else {
                ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE);
            }
        } else {
            ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE);
        }

    }

    private boolean checkMapServices() {
        return isMapsEnabled();
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("This application requires GPS to work properly, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        Intent enableGpsIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivityForResult(enableGpsIntent, PERMISSIONS_REQUEST_ENABLE_GPS);
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    public boolean isMapsEnabled() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
            return false;
        }
        return true;
    }

/* ----------------------------------------------------FALTU----------------------------------------------------------------------------------------------------------------------------------------------- */

    //get text from edit text method
    private void geoLocate1() {

        Log.d(TAG, "geoLocate: geolocating");

        String searchString = carpool_editText.getText().toString();

        Geocoder geocoder1 = new Geocoder(main.this);
        List<Address> list1 = new ArrayList<>();

        try {

            list1 = geocoder1.getFromLocationName(searchString, 1);

        } catch (IOException e) {
            Log.e(TAG, "geoLocate: IOException: " + e.getMessage());
        }

        if (list1.size() > 0) {

            Address address1 = list1.get(0);

            Log.d(TAG, "geoLocate: found a location" + address1.toString());
            Toast.makeText(this, address1.toString(), Toast.LENGTH_SHORT).show();
            moveCamera(new LatLng(address1.getLatitude(), address1.getLongitude()), DEFAULT_ZOOM, address1.getAddressLine(0));
        }

    }

    private void geoLocate() {
        Log.d(TAG, "geoLocate: geolocating");

        String searchString = drive_editText.getText().toString();

        Geocoder geocoder = new Geocoder(main.this);
        List<Address> list = new ArrayList<>();

        try {

            list = geocoder.getFromLocationName(searchString, 1);

        } catch (IOException e) {
            Log.e(TAG, "geoLocate: IOException: " + e.getMessage());
        }

        if (list.size() > 0) {

            Address address = list.get(0);

            Log.d(TAG, "geoLocate: found a location" + address.toString());
            Toast.makeText(this, address.toString(), Toast.LENGTH_LONG).show();

            moveCamera(new LatLng(address.getLatitude(), address.getLongitude()), DEFAULT_ZOOM, address.getAddressLine(0));
        }

    }

/* ----------------------------------------------------EXTRAS----------------------------------------------------------------------------------------------------------------------------------------------- */

    //hide the users keyboard
    private void HideSoftKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        Userid = sharedPreferences.getString(TEXT, "");
    }

    private void startLocationServiceDriving() {
        if (!isLocationServiceRunningDriving()) {
            Intent serviceIntent = new Intent(this, LocationService.class);
//        this.startService(serviceIntent);

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

                main.this.startForegroundService(serviceIntent);
            } else {
                startService(serviceIntent);
            }
        }
    }

    private boolean isLocationServiceRunningDriving() {
        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if ("com.example.myapplication.Services".equals(service.service.getClassName())) {
                Log.d(TAG, "isLocationServiceRunning: location service is already running.");
                return true;
            }
        }
        Log.d(TAG, "isLocationServiceRunning: location service is not running.");
        return false;
    }

    private void reset(){

        bottom_rel_lay_2.setVisibility(View.VISIBLE);
        driving.setVisibility(View.VISIBLE);
        carpooling.setVisibility(View.VISIBLE);

        relativeLayout.setVisibility(View.GONE);
        drive_editText.setVisibility(View.GONE);
        carpool_editText.setVisibility(View.GONE);

        Userid2 = null;
        //UserId = null;
    }


/*----------------------------------------------------FireStore------------------------------------------------------------------------------------------------------------------------------------------*/


    private void saveUserLocation() {

        if (mUserLocation != null) {
            DocumentReference locationRef = mDb
                    .collection("Users Location")
                    .document(Userid);

            if (Google_service == false) {
                Google_service = true;
                locationRef.set(mUserLocation).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "saveUserLocation: \n" +
                                    "inserted user location into database." +
                                    "\n latitude: " + mUserLocation.getGeo_point().getLatitude() +
                                    "\n longitude: " + mUserLocation.getGeo_point().getLongitude());
                        }
                    }
                });
            } else {
                locationRef.update("geo_point", mUserLocation.getGeo_point());
            }
        }
    }

    private void getUserDetails() {
        loadData();
        final DocumentReference userRef = mDb.collection("Users")
                .document(Userid);

        if (Goggle_service == false) {
            Log.d(TAG, "getUserDetails: Reading initiated");
            mUserLocation = new UsersLocation();
            Goggle_service = true;
            userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "onComplete: successfully set the user client.");
                        Users user = task.getResult().toObject(Users.class);
                        mUserLocation.setUser(user);
                        ((UserClient) (getApplicationContext())).setUser(user);
                        getLastKnownLocation();
                    }
                }
            });
        } else {
            getLastKnownLocation();
        }
    }
}
