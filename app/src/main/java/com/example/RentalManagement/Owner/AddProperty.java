/*
 * Name : AddProperty.java
 * Purpose : This file used to add new property
 * Created By : Mahesh
 * Developers Involved : Mahesh
 */
package com.example.RentalManagement.Owner;

import android.Manifest;
import android.app.ProgressDialog;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
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
import com.example.RentalManagement.Dialogs.Features;
import com.example.RentalManagement.Dialogs.LogOut;
import com.example.RentalManagement.Dialogs.Parking;
import com.example.RentalManagement.Dialogs.TenantType;
import com.example.RentalManagement.Owner.Model.AddPropertyResponse;
import com.example.RentalManagement.R;
import com.example.RentalManagement.Services.ApiInterface;
import com.example.RentalManagement.Services.NetworkConnection;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
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

public class AddProperty extends AppCompatActivity implements View.OnClickListener,
        ApartmentType.ApartmentListener,
        BhkType.BhkListener,
        TenantType.TenantTypeListener,
        Features.FeaturesListener,
        Parking.ParkingListener,
        LocationListener {
    /*declarations*/
    Toolbar toolbar;
    AutoCompleteTextView enterLocation;
    EditText enterSize, enterRent, enterFloor, enterFloors, enterAddress;
    RelativeLayout recylerLayout;
    TextView apartmentType, selectBHK, tenantType, features, parkingFacility, save;
    ImageView currentLocation, clear, powered;
    LocationManager locationManager;
    RadioGroup foodRadioGroup, waterRadioGroup, liftRadioGroup, contactRadioGroup;
    RadioButton foodRadioButton, waterRadioButton, liftRadioButton, contactRadioButton;
    RecyclerView recyclerView;
    private FusedLocationProviderClient FusedLocationProviderClient;
    LocationSuggestionsAdapter locationSuggestionsAdapter;   //Adapter to display location suggestions
    ArrayList<String> data;           //To store area name
    ArrayList<String> data1;         //To store location name
    int cursorPosition;
    ScrollView scrollView;
    ApiInterface apiInterface;
    ProgressDialog progressDialog;
    AddPropertyResponse addPropertyResponse;
    NetworkConnection networkConnection;
    String enteredLocation, apartment, bhk, extent, rent, floorNo, floors,
            tenants, food, specialFeatures, water, parking, lift,
            contactTime, address;
    String[] seperated;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1;
    double latitude, longitude;
    LatLng latLng;
    String locality;
    List<Address> addresses;
    private boolean isReached = false;
    int j = 20;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_property);
        statusBarColor().setStatusBarColor(this.getResources().getColor(R.color.design_default_color_primary));
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
        scrollView = findViewById(R.id.scrollView);
        apartmentType = findViewById(R.id.apartmentType);
        selectBHK = findViewById(R.id.selectBHK);
        enterSize = findViewById(R.id.size);
        enterRent = findViewById(R.id.rent);
        enterFloor = findViewById(R.id.floor);
        enterFloors = findViewById(R.id.floors);
        tenantType = findViewById(R.id.tenantType);
        foodRadioGroup = findViewById(R.id.foodRadioGroup);
        foodRadioGroup.clearCheck();
        foodRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                foodRadioButton = (RadioButton) radioGroup.findViewById(checkedId);
            }
        });
        features = findViewById(R.id.features);
        waterRadioGroup = findViewById(R.id.wateGroup);
        waterRadioGroup.clearCheck();
        waterRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                waterRadioButton = (RadioButton) radioGroup.findViewById(checkedId);
            }
        });
        liftRadioGroup = findViewById(R.id.liftGroup);
        liftRadioGroup.clearCheck();
        liftRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                liftRadioButton = (RadioButton) radioGroup.findViewById(checkedId);
            }
        });
        parkingFacility = findViewById(R.id.parking);
        enterAddress = findViewById(R.id.address);
        contactRadioGroup = findViewById(R.id.timeGroup);
        contactRadioGroup.clearCheck();
        contactRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                contactRadioButton = (RadioButton) radioGroup.findViewById(checkedId);
            }
        });
        save = findViewById(R.id.save);
        save.setBackgroundColor(getResources().getColor(R.color.design_default_color_primary));
        networkConnection = new NetworkConnection(this);
        progressDialog = new ProgressDialog(this, R.style.progressDialogStyle);

        currentLocation.setOnClickListener(this);
        clear.setOnClickListener(this);
        apartmentType.setOnClickListener(this);
        selectBHK.setOnClickListener(this);
        tenantType.setOnClickListener(this);
        features.setOnClickListener(this);
        parkingFacility.setOnClickListener(this);
        save.setOnClickListener(this);
        Intent i = getIntent();
        Bundle bundle = i.getBundleExtra("bundle");
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
                        scrollView.setVisibility(View.VISIBLE);
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

        /*enter property address field*/
        enterAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                Log.d("TAG", "afterTextChanged: "+enterAddress.getText().length());
                if (enterAddress.getLayout().getLineCount() > 5) {
                    enterAddress.getText()
                            .delete(enterAddress.getText().length() - 1, enterAddress.getText().length());
                }else{
                    if(enterAddress.getText().length() == j){
                        j += 21;
                        enterAddress.append("\n");
                    }
                }
            }
        });
    }

/*changing statusbar color*/
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public Window statusBarColor() {
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        return window;
    }

/*toolbar icons initilizations and onclick*/
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

/*onclick of widgets*/
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
            case R.id.tenantType:
                new TenantType().show(getFragmentManager(), "tenantType");
                break;
            case R.id.features:
                new Features().show(getFragmentManager(), "features");
                break;
            case R.id.parking:
                new Parking().show(getFragmentManager(), "parking");
                break;
            case R.id.save:
                /*validations*/
                // checkingValidations();
                enteredLocation = enterLocation.getText().toString();
                locationLatLng(enteredLocation);
                Intent i = new Intent(this, UploadPropertyImages.class);
                i.putExtra("locality",locality);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                break;

        }
    }

    /*checking location permission*/
    public void checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(AddProperty.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(AddProperty.this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(AddProperty.this, new String[]
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

/*checking gps turned on or not*/
    public boolean GpsStatus() {
        locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ;
    }

/*permissions result*/
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

                if (ActivityCompat.shouldShowRequestPermissionRationale(AddProperty.this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                    //when click on deny
                } else {
                    //when click on don't show again
                    Intent i = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + BuildConfig.APPLICATION_ID));
                    startActivity(i);
                }
            }
        }
    }

    /*setting auto location suggestions to recyclerview*/
    private void setlistView() {            //to send data to adapter
        Log.d("TAG", "setlistView: " + data.size() + "  " + data1.size());
        locationSuggestionsAdapter = new LocationSuggestionsAdapter(data, data1,
                AddProperty.this, getApplicationContext(),"addProperty");
        recyclerView.setAdapter(locationSuggestionsAdapter);
    }

  /*getting apartmenttype, bhk, tenant type, features,parking details from Dialogs */
    @Override
    public void getApartmentType(String apartment) {
        apartmentType.setText(apartment);
    }
    @Override
    public void getBhkType(String BHK) {
        selectBHK.setText(BHK);
    }
    @Override
    public void getTenantType(String tenat) {
        tenantType.setText(tenat);
    }
    @Override
    public void getFeatures(String feature) {
        features.setText(feature.substring(1));
        String s = feature.substring(1);
        seperated = s.split(",");
        Log.d("TAG", "getFeatures: " + seperated.length + "\n" + Arrays.toString(seperated));
    }
    @Override
    public void getParkingDetails(String slot) {
        parkingFacility.setText(slot);
    }

/*onclick of location suggestions*/
    public void getLocation(String selectedLocation) {
        scrollView.setVisibility(View.VISIBLE);
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
                    getLocationDetails(new LatLng(location.getLatitude(), location.getLongitude()));
                }else{
                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    Location location1 = locationManager
                            .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    if (location1 != null) {
                        getLocationDetails(new LatLng(location1.getLatitude(), location1.getLongitude()));
                    }else {
                        Toast.makeText(AddProperty.this, "unable to get current location", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        } //try
        catch (SecurityException e) {
            e.printStackTrace();
        }//catch
    }

/*convertinf location name to latlng*/
    private void locationLatLng(String locationDetails) {
        Geocoder geocoder = new Geocoder(getApplicationContext());
        try {
            addresses = geocoder.getFromLocationName(locationDetails, 1);
            Address location = addresses.get(0);
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            latLng = new LatLng(latitude, longitude);
            getLocationDetails(latLng);
        } catch (Exception e) {
        }
    }

/*converting latlng to location name*/
    private void getLocationDetails(LatLng latLng) {
        Geocoder geocoder = new Geocoder(AddProperty.this, Locale.getDefault());
        try {
            Log.d("TAG", "latlng" + latLng + "  lat" + latLng.latitude + "long" + latLng);
            // List<Address> addresses = geocoder.getFromLocation(18.277481,   83.900032, 1);
            latitude = latLng.latitude;
            longitude = latLng.longitude;
            addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            enterLocation.setText(addresses.get(0).getAddressLine(0));
            currentLocation.setVisibility(View.GONE);
            clear.setVisibility(View.VISIBLE);
            locality = addresses.get(0).getLocality();
            Log.d("TAG", "CityName: latlngaddress" + addresses + "\n" +
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

    /*checking validations*/
    private void checkingValidations() {
        enteredLocation = enterLocation.getText().toString();
        apartment = apartmentType.getText().toString();
        bhk = selectBHK.getText().toString();
        extent = enterSize.getText().toString();
        rent = enterRent.getText().toString();
        floorNo = enterFloor.getText().toString();
        floors = enterFloors.getText().toString();
        tenants = tenantType.getText().toString();
        specialFeatures = features.getText().toString();
        parking = parkingFacility.getText().toString();
        address = enterAddress.getText().toString();

        if (enteredLocation.length() == 0) {
            enterLocation.setError("Please Enter Location");
        } else if (apartment.equalsIgnoreCase("")) {
            Toast.makeText(this, "Select ApartmentType", Toast.LENGTH_SHORT).show();
        } else if (bhk.equalsIgnoreCase("")) {
            Toast.makeText(this, "Select BHK Type", Toast.LENGTH_SHORT).show();
        } else if (extent.length() < 2) {
            enterSize.setError("Enter Property Size");
        } else if (rent.length() == 0) {
            enterRent.setText("Enter Rent");
        } else if (floorNo.length() == 0) {
            enterFloor.setError("Enter Floor Number");
        } else if (tenants.equalsIgnoreCase("")) {
            Toast.makeText(this, "Select Tenant Type", Toast.LENGTH_SHORT).show();
        } else if (foodRadioGroup.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this, "Please Select Type Of Food", Toast.LENGTH_LONG).show();
        } else if (specialFeatures.equalsIgnoreCase("")) {
            Toast.makeText(this, "Select Features", Toast.LENGTH_SHORT).show();
        } else if (waterRadioGroup.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this, "Please Select Type Of Water", Toast.LENGTH_LONG).show();
        } else if (liftRadioGroup.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this, "Please Select Lift Avaliable or Not", Toast.LENGTH_LONG).show();
        } else if (parking.equalsIgnoreCase("")) {
            Toast.makeText(this, "Select Parking Type", Toast.LENGTH_SHORT).show();
        } else if (contactRadioGroup.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this, "Please Select Prefer Contact Time", Toast.LENGTH_LONG).show();
        } else if (address.length() == 0) {
            enterAddress.setError("Please Enter Address");
        } else {
            food = foodRadioButton.getText().toString();
            water = waterRadioButton.getText().toString();
            lift = liftRadioButton.getText().toString();
            contactTime = contactRadioButton.getText().toString();
            locationLatLng(enteredLocation);
            if (Integer.parseInt(floors) < Integer.parseInt(floorNo)) {
                Toast.makeText(this, "floor Number should be lessthan or equal to number of floors", Toast.LENGTH_LONG).show();
            } else {
                if (networkConnection.isConnectingToInternet()) {
                    try {
                        submitDetails(enteredLocation, latitude, longitude, apartment, bhk, extent, rent, floorNo, floors,
                                tenants, food, Arrays.toString(seperated), water, parking, lift,
                                contactTime, address);
                    } catch (Exception e) {
                        if (progressDialog != null) {
                            progressDialog.cancel();
                        }
                    }
                } else {
                    Toast.makeText(this, "Please check your internet connection", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    /*submitting property details to server*/
    private void submitDetails(String enteredLocation, double latitude, double longitude,String apartment, String bhk, String extent,
                               String rent, String floors, String floorNo, String tenants, String food, String specialFeatures,
                               String water, String parking, String lift, String contactTime, String address) {
        Intent i = new Intent(this, UploadPropertyImages.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);

        /*progressDialog.setMessage("Please wait ...");
        progressDialog.show();
        apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);
        Call<AddPropertyResponse> call = apiInterface.getSubmitDetailsStatus(
                new AddPropertyResponse(enteredLocation, String.valueOf(latitude), String.valueOf(longitude),
                        apartment, bhk,extent, rent,floors, floorNo, tenants, food, specialFeatures,water,parking,
                        lift, contactTime, address));
        call.enqueue(new Callback<AddPropertyResponse>() {
            @Override
            public void onResponse(Call<AddPropertyResponse> call, Response<AddPropertyResponse> response) {
                addPropertyResponse = response.body();
                try {
                    if(addPropertyResponse.getStatus().equalsIgnoreCase("SUCCESS")) {
                        progressDialog.cancel();
                        Intent i = new Intent(getApplicationContext(), UploadPropertyImages.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                    }else{
                        Toast.makeText(AddProperty.this, "Try Again Later", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    progressDialog.cancel();
                }
            }
            @Override
            public void onFailure(Call<AddPropertyResponse> call, Throwable t) {
                progressDialog.cancel();
            }
        });;*/
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {

    }
}