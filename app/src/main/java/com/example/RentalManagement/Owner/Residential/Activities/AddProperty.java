/*
 * Name : AddProperty.java
 * Purpose : This file used to add new property
 * Created By : Mahesh
 * Developers Involved : Mahesh
 */
package com.example.RentalManagement.Owner.Residential.Activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.util.Base64;
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
import com.example.RentalManagement.Dialogs.ApartmentType;
import com.example.RentalManagement.Dialogs.BhkType;
import com.example.RentalManagement.Dialogs.Features;
import com.example.RentalManagement.Dialogs.LogOut;
import com.example.RentalManagement.Dialogs.Parking;
import com.example.RentalManagement.Dialogs.TenantType;
import com.example.RentalManagement.Owner.Residential.Models.AddPropertyResponse;
import com.example.RentalManagement.R;
import com.example.RentalManagement.Services.ApiClient;
import com.example.RentalManagement.Services.ApiInterface;
import com.example.RentalManagement.Services.NetworkConnection;
import com.example.RentalManagement.utils.BitmapHelper;
import com.google.android.datatransport.runtime.BuildConfig;
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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
    EditText enterApartmentName, enterSize, enterRent, enterFloor, enterRoomNo, enterAddress;
    RelativeLayout recylerLayout;
    TextView apartmentType, selectBHK, tenantType, features, parkingFacility, save;
    ImageView currentLocation, clear, powered;
    LocationManager locationManager;
    RadioGroup foodRadioGroup, waterRadioGroup, liftRadioGroup, contactRadioGroup;
    RadioButton foodRadioButton, waterRadioButton, liftRadioButton, contactRadioButton;
    RadioButton vegRadio, nonVegRadio, foodAnyRadio,
            corpRadio, boarRadio, bothRadio, liftAvailRadio, notAvailRadio, timeRadio, anyTimeRadio;
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
    String enteredLocation, apartment, apartmentName, bhk, extent, rent, floorNo, roomNo,
            tenants, food, specialFeatures, water, parking, lift,
            contactTime, address;
    String[] seperated;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1;
    double latitude, longitude;
    LatLng latLng;
    String locality, subLocality;
    List<Address> addresses;
    private boolean isReached = false;
    int j = 20;
    String buttonName;
    Integer id;
    Bitmap bitmap1, bitmap2, bitmap3;

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
        enterApartmentName = findViewById(R.id.apartmentName);
        selectBHK = findViewById(R.id.selectBHK);
        enterSize = findViewById(R.id.size);
        enterRent = findViewById(R.id.rent);
        enterFloor = findViewById(R.id.floor);
        enterRoomNo = findViewById(R.id.roomNo);
        tenantType = findViewById(R.id.tenantType);
        foodRadioGroup = findViewById(R.id.foodRadioGroup);
        foodRadioGroup.clearCheck();
        foodRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                foodRadioButton = (RadioButton) radioGroup.findViewById(checkedId);
            }
        });
        vegRadio = findViewById(R.id.vegRadioButton);
        nonVegRadio = findViewById(R.id.nonVegRadioButton);
        foodAnyRadio = findViewById(R.id.anyRadioButton);
        features = findViewById(R.id.features);
        waterRadioGroup = findViewById(R.id.wateGroup);
        waterRadioGroup.clearCheck();
        waterRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                waterRadioButton = (RadioButton) radioGroup.findViewById(checkedId);
            }
        });
        corpRadio = findViewById(R.id.corporationRadioButton);
        boarRadio = findViewById(R.id.boarRadioButton);
        bothRadio = findViewById(R.id.bothRadioButton);
        liftRadioGroup = findViewById(R.id.liftGroup);
        liftRadioGroup.clearCheck();
        liftRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                liftRadioButton = (RadioButton) radioGroup.findViewById(checkedId);
            }
        });
        liftAvailRadio = findViewById(R.id.avaliableRadioButton);
        notAvailRadio = findViewById(R.id.notAvaliableRadioButton);
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
        timeRadio = findViewById(R.id.timeRadioButton);
        anyTimeRadio = findViewById(R.id.anyTimeRadioButton);
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

        /*getting data from previous activity to know which button has clicked*/
        Intent i = getIntent();
        buttonName = i.getStringExtra("buttonName");
        if(buttonName.equalsIgnoreCase("addProperty")){

        }else if(buttonName.equalsIgnoreCase("edit")) {
            id = i.getIntExtra("id",0);
            getImages(id);
        }
       // getImages();
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
       /* enterAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                Log.d("TAG", "afterTextChanged: " + enterAddress.getText().length());
                if (enterAddress.getLayout().getLineCount() > 5) {
                    enterAddress.getText()
                            .delete(enterAddress.getText().length() - 1, enterAddress.getText().length());
                } else {
                    if (enterAddress.getText().length() == j) {
                        j += 21;
                        enterAddress.append("\n");
                    }
                }
            }
        });*/
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
                checkingValidations();
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
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
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
                    checkLocationPermission();
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
                AddProperty.this, getApplicationContext(), "addProperty");
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
            getLocationDetails(latLng, "suggestionClick");
        } catch (Exception e) {
        }
    }

    /*converting latlng to location name*/
    private void getLocationDetails(LatLng latLng, String s) {
        Geocoder geocoder = new Geocoder(AddProperty.this, Locale.getDefault());
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

    /*checking validations*/
    private void checkingValidations() {
        enteredLocation = enterLocation.getText().toString();
        apartment = apartmentType.getText().toString();
        apartmentName = enterApartmentName.getText().toString();
        bhk = selectBHK.getText().toString();
        extent = enterSize.getText().toString();
        rent = enterRent.getText().toString();
        floorNo = enterFloor.getText().toString();
        roomNo = enterRoomNo.getText().toString();
        tenants = tenantType.getText().toString();
        specialFeatures = features.getText().toString();
        parking = parkingFacility.getText().toString();
        address = enterAddress.getText().toString();

        if (enteredLocation.length() == 0) {
            enterLocation.setError("Please Enter Location");
        } else if (apartment.equalsIgnoreCase("")) {
            Toast.makeText(this, "Select ApartmentType", Toast.LENGTH_SHORT).show();
        } else if (apartmentName.length() == 0) {
            enterApartmentName.setError("Please Enter Apartment Name");
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
               /* submitDetails(enteredLocation, latitude, longitude, apartment, apartmentName, bhk, extent, rent, floorNo, floors,
                        tenants, food, seperated, water, parking, lift,
                        contactTime, address);*/
                Intent i = new Intent(getApplicationContext(), UploadPropertyImages.class);
                Bundle bundle = new Bundle();
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                bundle.putDouble("latitude", latitude);
                bundle.putDouble("longitude", longitude);
                bundle.putString("locality", locality);
                bundle.putString("subLocality", subLocality);
                bundle.putString("apartmentType", apartment);
                bundle.putString("apartmentName", apartmentName);
                bundle.putString("bhk", bhk);
                bundle.putString("extent", extent);
                bundle.putString("rent", rent);
                bundle.putString("roomNo", roomNo);
                bundle.putString("floorNo", floorNo);
                bundle.putString("tenantType", tenants);
                bundle.putString("foodType", food);
                bundle.putStringArray("specialFeatures", seperated);
                bundle.putString("water", water);
                bundle.putString("parking", parking);
                bundle.putString("lift", lift);
                bundle.putString("contactTime", contactTime);
                bundle.putString("address", address);
                bundle.putString("buttonName",buttonName);
                bundle.putInt("id",id);
                i.putExtra("bundle", bundle);
                startActivity(i);

        }
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {

    }

    private void getImages(int id) {
        if (networkConnection.isConnectingToInternet()) {
            try {
                progressDialog.setMessage("Please wait We are Fetching Details...");
                progressDialog.setCancelable(true);
                progressDialog.show();
                apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);
                Call<AddPropertyResponse> call = apiInterface.getPropertyIdData(id);
                call.enqueue(new Callback<AddPropertyResponse>() {
                    @Override
                    public void onResponse(Call<AddPropertyResponse> call, Response<AddPropertyResponse> response) {
                        addPropertyResponse = response.body();
                        Log.d("TAG", "getimages: " + "\n" +
                                addPropertyResponse.getWaterSource() + addPropertyResponse.getContactTime() + addPropertyResponse.getAddress());
                        try {
                            if (progressDialog != null) {
                                progressDialog.cancel();
                            }
                            byte[] decodedString1 = Base64.decode(addPropertyResponse.getImage1(), Base64.DEFAULT);
                            bitmap1 = BitmapFactory.decodeByteArray(decodedString1, 0, decodedString1.length);
                            BitmapHelper.getInstance().setBitmap1(bitmap1);
                            byte[] decodedString2 = Base64.decode(addPropertyResponse.getImage2(), Base64.DEFAULT);
                            bitmap2 = BitmapFactory.decodeByteArray(decodedString2, 0, decodedString2.length);
                            BitmapHelper.getInstance().setBitmap2(bitmap2);
                            byte[] decodedString3 = Base64.decode(addPropertyResponse.getImage3(), Base64.DEFAULT);
                            bitmap3 = BitmapFactory.decodeByteArray(decodedString3, 0, decodedString3.length);
                            BitmapHelper.getInstance().setBitmap3(bitmap3);

                            LatLng latLng = new LatLng(Double.parseDouble(addPropertyResponse.getLatitude()), Double.parseDouble(addPropertyResponse.getLongitude()));
                            getLocationDetails(latLng, "locationClick");
                            apartmentType.setText(addPropertyResponse.getApartmentType());
                            enterApartmentName.setText(addPropertyResponse.getApartmentName());
                            enterAddress.setText(addPropertyResponse.getAddress());
                            selectBHK.setText(addPropertyResponse.getBHK());
                            enterSize.setText(addPropertyResponse.getSft());
                            enterRent.setText(addPropertyResponse.getRent());
                            enterRoomNo.setText(addPropertyResponse.getRoomNo());
                            enterFloor.setText(addPropertyResponse.getFloorNo());
                            tenantType.setText(addPropertyResponse.getTenantType());
                            switch (addPropertyResponse.getFoodType()) {
                                case "Veg":
                                    vegRadio.setChecked(true);
                                    break;
                                case "Non-Veg":
                                    nonVegRadio.setChecked(true);
                                    break;
                                case "Any":
                                    foodAnyRadio.setChecked(true);
                                    break;
                            }
                            features.setText(addPropertyResponse.getSplFeatures().substring(1, addPropertyResponse.getSplFeatures().length() - 1));
                            String s = addPropertyResponse.getSplFeatures().substring(1, addPropertyResponse.getSplFeatures().length() - 1);
                            seperated = s.split(",");
                            switch (addPropertyResponse.getWaterSource()) {
                                case "Corporatio":
                                    corpRadio.setChecked(true);
                                    break;
                                case "Boar":
                                    boarRadio.setChecked(true);
                                    break;
                                case "Both":
                                    bothRadio.setChecked(true);
                                    break;
                            }
                            switch (addPropertyResponse.getLiftAvailable()) {
                                case "Avaliable":
                                    liftAvailRadio.setChecked(true);
                                    break;
                                case "Not Avaliable":
                                    notAvailRadio.setChecked(true);
                                    break;

                            }
                            parkingFacility.setText(addPropertyResponse.getParkingType());
                            switch (addPropertyResponse.getContactTime()) {
                                case "8AM-6PM":
                                    timeRadio.setChecked(true);
                                    break;
                                case "AnyTime":
                                    anyTimeRadio.setChecked(true);
                                    break;

                            }
                            // parkingFacility
                        } catch (Exception e) {
                            Log.d("TAG", "getimages:2 "+e);
                            if (progressDialog != null) {
                                progressDialog.cancel();
                            }
                            Toast.makeText(getApplicationContext(), R.string.try_again, Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<AddPropertyResponse> call, Throwable t) {
                        if (progressDialog != null) {
                            progressDialog.cancel();
                        }
                        Toast.makeText(getApplicationContext(), R.string.try_again, Toast.LENGTH_LONG).show();
                    }
                });
            } catch (Exception e) {
                if (progressDialog != null) {
                    progressDialog.cancel();
                }
                Toast.makeText(this, R.string.try_again, Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, R.string.no_net, Toast.LENGTH_LONG).show();
        }

    }
}