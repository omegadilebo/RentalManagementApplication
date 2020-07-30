/*
 * Name : SearchProperty.java
 * Purpose : This file is used for, tenant can search the property(apartments,villas etc.)
 * Created By : Mahesh
 * Developers Involved : Mahesh
 */

package com.example.RentalManagement.Tenant;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.RentalManagement.Adapters.LocationSuggestionsAdapter;
import com.example.RentalManagement.BuildConfig;
import com.example.RentalManagement.Dialogs.ApartmentType;
import com.example.RentalManagement.Dialogs.BhkType;
import com.example.RentalManagement.Dialogs.LogOut;
import com.example.RentalManagement.R;
import com.example.RentalManagement.Services.NetworkConnection;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SearchProperty extends AppCompatActivity implements
        View.OnClickListener,
        ApartmentType.ApartmentListener,
        BhkType.BhkListener, LocationListener {
    /*declarations*/
    Toolbar toolbar;
    AutoCompleteTextView enterLocation;
    ImageView currentLocation, clear, powered;
    RelativeLayout recylerLayout;
    RecyclerView recyclerView;
    TextView apartmentType, selectBHK, rent, search;
    EditText enterSize;
    String enteredLocation, apartment, bhk, extent, getRent;
    private com.google.android.gms.location.FusedLocationProviderClient FusedLocationProviderClient;
    LocationManager locationManager;
    LocationSuggestionsAdapter locationSuggestionsAdapter;   //Adapter to display location suggestions
    ArrayList<String> data;           //To store area name
    ArrayList<String> data1;
    int cursorPosition;
    double latitude, longitude;
    LatLng latLng;
    String locality, subLocality;
    List<Address> addresses;
    NetworkConnection networkConnection;
    SeekBar seekBar;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_property);
        statusBarColor().setStatusBarColor(this.getResources().getColor(R.color.design_default_color_primary));
        /*initializations*/
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Search Property");
        toolbar.setNavigationIcon(R.drawable.arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        enterLocation = findViewById(R.id.location);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recylerLayout = findViewById(R.id.recylerLayout);
        currentLocation = findViewById(R.id.currentLocation);
        clear = findViewById(R.id.clear);
        powered = findViewById(R.id.powered);
        apartmentType = findViewById(R.id.apartmentType);
        selectBHK = findViewById(R.id.selectBHK);
        enterSize = findViewById(R.id.size);
        rent = findViewById(R.id.rent);
        seekBar = findViewById(R.id.seekbar);
        /*seekbar code starts here*/
        seekBar.setMin(0);
        seekBar.setMax(50000);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                rent.setText(String.valueOf(i));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        /*seekbar code ends here*/
        search = findViewById(R.id.search);
        search.setBackgroundColor(getResources().getColor(R.color.design_default_color_primary));
        currentLocation.setOnClickListener(this);
        clear.setOnClickListener(this);
        apartmentType.setOnClickListener(this);
        selectBHK.setOnClickListener(this);
        search.setOnClickListener(this);

        networkConnection = new NetworkConnection(this);
        String apiKey = getString(R.string.apiKey);
        data = new ArrayList<String>();
        data1 = new ArrayList<String>();
        /*google auto location suggestions starts here*/
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), apiKey);
        }
        final PlacesClient placesClient = Places.createClient(this);
        enterLocation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                cursorPosition = enterLocation.getSelectionStart();
                Log.d("TAG", "onTextChanged: " + cursorPosition);
                if (s.toString().trim().length() == 0) {
                    clear.setVisibility(View.GONE);
                    currentLocation.setVisibility(View.VISIBLE);
                    recylerLayout.setVisibility(View.GONE);
                } else if (s.toString().trim().length() > 2) {
                    if (cursorPosition > 2) {
                        recylerLayout.setVisibility(View.VISIBLE);
                    }
                    AutocompleteSessionToken token = AutocompleteSessionToken.newInstance();
                    RectangularBounds bounds = RectangularBounds.newInstance(
                            new LatLng(23.63936, 68.14712),
                            new LatLng(28.20453, 97.34466));
                    FindAutocompletePredictionsRequest request = FindAutocompletePredictionsRequest.builder()
                            .setLocationBias(bounds)
                            .setCountry("IN")
                            .setSessionToken(token)
                            .setQuery(s.toString())
                            .build();
                    placesClient.findAutocompletePredictions(request).addOnSuccessListener
                            (new OnSuccessListener<FindAutocompletePredictionsResponse>() {
                                @Override
                                public void onSuccess(FindAutocompletePredictionsResponse response) {
                                    data.clear();
                                    data1.clear();

                                    for (AutocompletePrediction prediction : response.getAutocompletePredictions()) {
                                        Log.i("TAG", "places" + prediction.getPrimaryText(null).toString());
                                        Log.i("TAG", "placess" + prediction
                                                .getSecondaryText(null).toString());
                                        data.add(prediction.getPrimaryText(null).toString());
                                        data1.add(prediction.getSecondaryText(null).toString());
                                    }
                                    if (data.size() > 1) {
                                        locationSuggestionsAdapter = new LocationSuggestionsAdapter(data, data1,
                                                SearchProperty.this, getApplicationContext(), "searchProperty");
                                        recyclerView.setAdapter(locationSuggestionsAdapter);
                                    } else {
                                        Log.d("TAG", "onTextChanged: No location found ");
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            if (exception instanceof ApiException) {
                                ApiException apiException = (ApiException) exception;
                                Log.e("TAG", "Place not found: " + apiException.getStatusCode());
                            }
                        }
                    });
                } else {
                    clear.setVisibility(View.VISIBLE);
                    currentLocation.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                data.clear();
                data1.clear();
            }
        });
        /*google auto location suggestions ends here*/
    }//oncreate ends here

    /*change the statusbar color starts here*/
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public Window statusBarColor() {
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        return window;
    }
    /*change the statusbar color ends here */

    /*toolbar menu's code starts here*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_icons, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.profile:
                Toast.makeText(this, "profile", Toast.LENGTH_SHORT).show();
                break;
            case R.id.logOut:
                new LogOut().show(getFragmentManager(), "LogOut");
                break;
        }
        return true;
    }
    /*toolbar menu's code ends here*/

    /*on click listeners for the widgets strats here*/
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.currentLocation:
                checkLocationPermission();
                break;
            case R.id.clear:
                enterLocation.setText("");
                break;
            case R.id.apartmentType:
                new ApartmentType().show(getFragmentManager(), "apartmentType");
                break;
            case R.id.selectBHK:
                new BhkType().show(getFragmentManager(), "selectBHK");
                break;
            case R.id.search:
                /*validations*/
                checkingValidations();
                break;
        }
    }
    /*on click listeners for the widgets ends here*/

    /*ontapping of location names and seeting it to textview*/
    public void getLocation(String selectedLocation) {
        if (cursorPosition > 0) {
            recylerLayout.setVisibility(View.GONE);
        }
        enterLocation.setText(selectedLocation);
        locationLatLng(selectedLocation);
        hideKeybaord();
    }

    /*checking location permission when click on mylocation*/
    public void checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(SearchProperty.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(SearchProperty.this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(SearchProperty.this, new String[]
                    {Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        } else {
            if (GpsStatus()) {
                getCurrentLocation();
            } else {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        }
    }

    /*checking GPS status*/
    public boolean GpsStatus() {
        locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    /*permission result request codes*/
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                /*if(GpsStatus()){

                }else {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                }*/
            } else if (grantResults[0] == PackageManager.PERMISSION_DENIED) {

                if (ActivityCompat.shouldShowRequestPermissionRationale(SearchProperty.this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                    //when click on deny
                } else {
                    //when click on don't show again
                    Intent i = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + BuildConfig.APPLICATION_ID));
                    startActivity(i);
                }
            }
        }
    }

    /*to get current location details*/
    private void getCurrentLocation() {
        FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        try {
            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    MIN_TIME_BW_UPDATES,
                    MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
            if (locationManager != null) {
                Location location = locationManager
                        .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (location != null) {
                    getLocationDetails(new LatLng(location.getLatitude(), location.getLongitude()));
                } else {
                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    Location location1 = locationManager
                            .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    if (location1 != null) {
                        getLocationDetails(new LatLng(location1.getLatitude(), location1.getLongitude()));
                    } else {
                        Toast.makeText(SearchProperty.this, "unable to get current location", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        } //try
        catch (SecurityException e) {
            e.printStackTrace();
        }//catch
    }

    /*getting latlng from location names*/
    private void locationLatLng(String locationDetails) {
        Geocoder geocoder = new Geocoder(getApplicationContext());
        try {
            addresses = geocoder.getFromLocationName(locationDetails, 1);
            Address destinationlocation = addresses.get(0);
            latitude = destinationlocation.getLatitude();
            longitude = destinationlocation.getLongitude();
            latLng = new LatLng(latitude, longitude);
            getLocationDetails(latLng);
        } catch (Exception e) {
        }
    }

    /*converting latlng to get the location name*/
    private void getLocationDetails(LatLng latLng) {
        Geocoder geocoder = new Geocoder(SearchProperty.this, Locale.getDefault());
        try {
            Log.d("TAG", "latlng" + latLng + "  lat" + latLng.latitude + "long" + latLng);
            // List<Address> addresses = geocoder.getFromLocation(18.277481,   83.900032, 1);
            addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            enterLocation.setText(addresses.get(0).getAddressLine(0));
            currentLocation.setVisibility(View.GONE);
            clear.setVisibility(View.VISIBLE);
            locality = addresses.get(0).getLocality();
            subLocality = addresses.get(0).getSubLocality();
            Log.d("TAG", "CityName: latlngaddress" + addresses + "\n" +
                    addresses.get(0).getLocality() + "\n" +
                    addresses.get(0).getSubLocality() + "\n" +
                    addresses.get(0).getAdminArea() + "\n" +
                    addresses.get(0).getSubAdminArea() + "\n");

        } catch (IOException e) {
            Toast.makeText(this, "We are unable to fetch your location", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    /*hiding keyboard when tap on location name*/
    public void hideKeybaord() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /*getting apartment type and bhk type from Dialog classes*/
    @Override
    public void getApartmentType(String apartment) {
        apartmentType.setText(apartment);
    }

    @Override
    public void getBhkType(String BHK) {
        selectBHK.setText(BHK);
    }

    /*checking validations when onclick on search button*/
    private void checkingValidations() {
        enteredLocation = enterLocation.getText().toString();
        apartment = apartmentType.getText().toString();
        bhk = selectBHK.getText().toString();
        extent = enterSize.getText().toString();
        getRent = rent.getText().toString();
        locationLatLng(enteredLocation);
        if (enteredLocation.length() == 0) {
            enterLocation.setError("Please Enter Location");
        } else if (apartment.equalsIgnoreCase("")) {
            Toast.makeText(this, "Select ApartmentType", Toast.LENGTH_SHORT).show();
        } else if (bhk.equalsIgnoreCase("")) {
            Toast.makeText(this, "Select BHK Type", Toast.LENGTH_SHORT).show();
        } else if (extent.length() < 2) {
            enterSize.setError("Enter Property Size");
        } else if (getRent.equalsIgnoreCase("")) {
            Toast.makeText(this, "Select Rent", Toast.LENGTH_SHORT).show();
        } else {
            if (networkConnection.isConnectingToInternet()) {
                try {
                    Intent i = new Intent(this, SearchPropertyResults.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    Bundle b = new Bundle();
                    String category = "Rental";
                    String propertyType = "Residential";
                    b.putString("category", category);
                    b.putString("propertyType", propertyType);
                    b.putString("locality", locality);
                    b.putString("subLocality", subLocality);
                    b.putString("apartmentType", apartment);
                    b.putString("bhk", bhk);
                    b.putString("extent", extent);
                    b.putString("rent", getRent);
                    i.putExtra("b", b);
                    startActivity(i);
                } catch (Exception e) {

                }
            } else {
                Toast.makeText(this, "Please check your internet connection", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {

    }
}