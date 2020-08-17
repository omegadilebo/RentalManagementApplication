/*
 * Name : CommercialAddProperty.java
 * Purpose : This file used to add new commercial property
 * Created By : Mahesh
 * Developers Involved : Mahesh
 */
package com.example.RentalManagement.Owner.Commercial.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.RentalManagement.Adapters.LocationSuggestionsAdapter;
import com.example.RentalManagement.Dialogs.CommercialPropertyFeatures;
import com.example.RentalManagement.Dialogs.CommercialPropertyType;
import com.example.RentalManagement.Owner.UploadPropertyImages;
import com.example.RentalManagement.R;
import com.google.android.datatransport.runtime.BuildConfig;
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
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class CommercialAddProperty extends AppCompatActivity implements
        View.OnClickListener,
        LocationListener,
        CommercialPropertyType.PropertyTypeListener,
        CommercialPropertyFeatures.CommercialPropertyFeaturesListener {
    /*declarations*/
    Toolbar toolbar;
    RelativeLayout recylerLayout;
    RecyclerView recyclerView;
    AutoCompleteTextView enterLocation;
    ImageView currentLocation, clear, powered;
    private com.google.android.gms.location.FusedLocationProviderClient FusedLocationProviderClient;
    LocationSuggestionsAdapter locationSuggestionsAdapter;   //Adapter to display location suggestions
    ArrayList<String> data;           //To store area name
    ArrayList<String> data1;         //To store location name
    int cursorPosition;
    LocationManager locationManager;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1;
    double latitude, longitude;
    LatLng latLng;
    String locality, subLocality;
    List<Address> addresses;
    TextView propertyType, features, save;
    EditText size, floors, floor, rent, deposit, lease, address;
    String enteredLocation, property, extent, noOfFloors, floorNo,
            selectFeatues, enterRent, enterDeposit, enterLease, contactTime, enterAddress;
    String[] seperated;
    RadioGroup contactRadioGroup;
    RadioButton contactRadioButton, timeRadio, anyTimeRadio;
    String buttonName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commercial_add_property);

        /*initilizations*/
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Add Property");
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
        propertyType = findViewById(R.id.propertyType);
        size = findViewById(R.id.size);
        floors = findViewById(R.id.floors);
        floor = findViewById(R.id.floorNo);
        features = findViewById(R.id.features);
        rent = findViewById(R.id.rent);
        deposit = findViewById(R.id.deposit);
        lease = findViewById(R.id.lease);
        contactRadioGroup = findViewById(R.id.timeGroup);
        contactRadioGroup.clearCheck();
        contactRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                contactRadioButton = (RadioButton) radioGroup.findViewById(checkedId);
            }
        });
        timeRadio = findViewById(R.id.timeRadioButton);
        anyTimeRadio = findViewById(R.id.anyTimeRadioButton);
        address = findViewById(R.id.address);
        save = findViewById(R.id.save);

        /*getting data from previous activity to know which button has clicked*/
        Intent i = getIntent();
        buttonName = i.getStringExtra("buttonName");
        if(buttonName.equalsIgnoreCase("addProperty")){

        }else if(buttonName.equalsIgnoreCase("edit")) {

        }
        currentLocation.setOnClickListener(this);
        clear.setOnClickListener(this);
        propertyType.setOnClickListener(this);
        features.setOnClickListener(this);
        save.setOnClickListener(this);

        /*auto location suggestions for edit text*/
        String apiKey = getString(R.string.apiKey);
        data = new ArrayList<String>();
        data1 = new ArrayList<String>();
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), apiKey);
        }
        final PlacesClient placesClient = Places.createClient(this);
        //text watcher for source
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
                        //  scrollView.setVisibility(View.VISIBLE);
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
                                        setlistView();
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
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.currentLocation:
                checkLocationPermission();
                break;
            case R.id.clear:
                enterLocation.setText("");
                break;
            case R.id.propertyType:
                new CommercialPropertyType().show(getSupportFragmentManager(), "CommercialPropertyType");
                break;
            case R.id.features:
                new CommercialPropertyFeatures().show(getSupportFragmentManager(), "CommercialPropertyFeatures");
                break;
            case R.id.save:
                /*validations*/
                checkingValidations();
                break;
        }
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {

    }

    /*setting auto location suggestions to recyclerview*/
    private void setlistView() {            //to send data to adapter
        Log.d("TAG", "setlistView: " + data.size() + "  " + data1.size());
        locationSuggestionsAdapter = new LocationSuggestionsAdapter(data, data1,
                CommercialAddProperty.this, getApplicationContext(), "CommercialAddProperty");
        recyclerView.setAdapter(locationSuggestionsAdapter);
    }

    /*checking location permission*/
    public void checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(CommercialAddProperty.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(CommercialAddProperty.this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(CommercialAddProperty.this, new String[]
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

    /*onclick of location suggestions*/
    public void getLocation(String selectedLocation) {
        //scrollView.setVisibility(View.VISIBLE);
        if (cursorPosition > 0) {
            recylerLayout.setVisibility(View.GONE);
        }
        enterLocation.setText(selectedLocation);
        hideKeybaord();
    }

    /*onclick of location icon to get current location details*/
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
                    getLocationDetails(new LatLng(location.getLatitude(), location.getLongitude()), "locationClick");
                } else {
                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    Location location1 = locationManager
                            .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    if (location1 != null) {
                        getLocationDetails(new LatLng(location1.getLatitude(), location1.getLongitude()), "locationClick");
                    } else {
                        Toast.makeText(CommercialAddProperty.this, "unable to get current location", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        } //try
        catch (SecurityException e) {
            e.printStackTrace();
        }//catch
    }

    /*converting location name to latlng*/
    private void locationLatLng(String locationDetails) {
        Geocoder geocoder = new Geocoder(getApplicationContext());
        try {
            addresses = geocoder.getFromLocationName(locationDetails, 1);
            Address location = addresses.get(0);
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            latLng = new LatLng(latitude, longitude);
            getLocationDetails(latLng, "suggestionClick");
        } catch (Exception e) {
        }
    }

    /*converting latlng to location name*/
    private void getLocationDetails(LatLng latLng, String s) {
        Geocoder geocoder = new Geocoder(CommercialAddProperty.this, Locale.getDefault());
        try {
            Log.d("TAG", "latlng" + latLng + "  lat" + latLng.latitude + "long" + latLng);
            // List<Address> addresses = geocoder.getFromLocation(18.277481,   83.900032, 1);
            latitude = latLng.latitude;
            longitude = latLng.longitude;
            addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            if (s.equalsIgnoreCase("locationClick")) {
                enterLocation.setText(addresses.get(0).getAddressLine(0));
            }
            currentLocation.setVisibility(View.GONE);
            clear.setVisibility(View.VISIBLE);
            locality = addresses.get(0).getLocality();
            subLocality = addresses.get(0).getSubLocality();
            Log.d("TAG", "CityName: latlngaddress" + "\n" +
                    addresses.get(0).getLocality() + "\n" +
                    addresses.get(0).getSubLocality() + "\n" +
                    addresses.get(0).getAdminArea() + "\n" +
                    addresses.get(0).getSubAdminArea() + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*hidekeyboard adfter onclick of location suggestions*/
    public void hideKeybaord() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /*checking gps turned on or not*/
    public boolean GpsStatus() {
        locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    /*permissions result*/
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            } else if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(CommercialAddProperty.this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                    //when click on deny
                    checkLocationPermission();
                } else {
                    //when click on don't show again
                    Intent i = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + BuildConfig.APPLICATION_ID));
                    startActivity(i);
                }
            }
        }
    }

    @Override
    public void getPropertyType(String property) {
        propertyType.setText(property);
    }

    @Override
    public void getFeatures(String feature) {
        features.setText(feature.substring(1));
        String s = feature.substring(1);
        seperated = s.split(",");
        Log.d("TAG", "getFeatures: " + seperated.length + "\n" + seperated + "\n" + Arrays.toString(seperated));
    }

    /*checking validations*/
    private void checkingValidations() {
        enteredLocation = enterLocation.getText().toString();
        property = propertyType.getText().toString();
        extent = size.getText().toString().trim();
        noOfFloors = floors.getText().toString().trim();
        floorNo = floor.getText().toString().trim();
        selectFeatues = features.getText().toString();
        enterRent = rent.getText().toString().trim();
        enterDeposit = deposit.getText().toString().trim();
        enterLease = lease.getText().toString().trim();
        enterAddress = address.getText().toString().trim();

        if (enteredLocation.length() == 0) {
            enterLocation.setError("Please Enter Location");
        } else if (property.equalsIgnoreCase("")) {
            Toast.makeText(this, "Select Property Type", Toast.LENGTH_LONG).show();
        } else if (extent.length() < 2) {
            size.setError("Enter Property Size");
        } else if (noOfFloors.length() == 0) {
            floors.setError("Enter Floor Number");
        } else if (floorNo.length() == 0) {
            floor.setError("Enter Floor Number");
        } else if (Integer.parseInt(floorNo) > Integer.parseInt(noOfFloors)) {
            Toast.makeText(this, "floor No. should be lessthan no. of floors", Toast.LENGTH_LONG).show();
        } else if (selectFeatues.equalsIgnoreCase("")) {
            Toast.makeText(this, "Select Features", Toast.LENGTH_LONG).show();
        } else if (enterRent.length() == 0) {
            rent.setError("Enter Rent");
        } else if (enterDeposit.length() == 0) {
            deposit.setError("Enter Deposit Amount");
        } else if (enterLease.length() == 0) {
            lease.setError("Enter Lease");
        } else if (contactRadioGroup.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this, "Please Select Prefer Contact Time", Toast.LENGTH_LONG).show();
        } else if (address.length() == 0) {
            address.setError("Please Enter Address");
        } else {
            locationLatLng(enteredLocation);
            contactTime = contactRadioButton.getText().toString();
            Intent i = new Intent(getApplicationContext(), UploadPropertyImages.class);
            Bundle bundle = new Bundle();
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            bundle.putDouble("latitude", latitude);
            bundle.putDouble("longitude", longitude);
            bundle.putString("locality", locality);
            bundle.putString("subLocality", subLocality);
            bundle.putString("propertyType", property);
            bundle.putString("extent", extent);
            bundle.putString("noOfFloors", noOfFloors);
            bundle.putString("floorNo", floorNo);
            bundle.putStringArray("specialFeatures", seperated);
            bundle.putString("rent", enterRent);
            bundle.putString("deposit", enterDeposit);
            bundle.putString("lease", enterLease);
            bundle.putString("contactTime", contactTime);
            bundle.putString("address", enterAddress);
            bundle.putString("buttonName", "commercialAddProperty");
            i.putExtra("bundle", bundle);
            try {
                startActivity(i);
            } catch (Exception e) {
                Log.d("TAG", "checkingValidations: " + e);
            }

        }
    }
}

