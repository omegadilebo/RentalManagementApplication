/*
 * Name : PropertyManagementDetails.java
 * Purpose : this file used to add property details for property management
 * Created By : Mahesh
 * Developers Involved : Mahesh
 */
package com.example.RentalManagement.Owner.PropertyManagement;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.RentalManagement.Dialogs.CommercialPropertyFeatures;
import com.example.RentalManagement.Owner.UploadPropertyImages;
import com.example.RentalManagement.R;

import java.util.Arrays;
import java.util.Objects;

public class PropertyManagementDetails extends AppCompatActivity implements
        View.OnClickListener,
        CommercialPropertyFeatures.CommercialPropertyFeaturesListener {
    /*declarations*/
    Toolbar toolbar;
    Bundle b;
    EditText floors, flats, blocks, size, rent, deposit, duration,
            Hno, colony, landmark, city, state, pincode;
    String noFloors, noFlats, noBlocks, extent, Rent, leaseAmount, leaseDuration,
            flatNo, Colony, Landmark, City, State, Pincode, splFeatures, contactTime;
    TextView features, save;
    RadioGroup contactRadioGroup;
    RadioButton contactRadioButton, beforeRadioButton, afterRadioButton, anyTimeRadioButton;
    String[] seperated;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_property_management_details);
        /*initializations*/
        b = getIntent().getBundleExtra("bundle");
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Property Details");
        toolbar.setNavigationIcon(R.drawable.arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });
        floors = findViewById(R.id.floors);
        flats = findViewById(R.id.flats);
        blocks = findViewById(R.id.blocks);
        size = findViewById(R.id.size);
        features = findViewById(R.id.features);
        rent = findViewById(R.id.rent);
        deposit = findViewById(R.id.deposit);
        duration = findViewById(R.id.duration);
        Hno = findViewById(R.id.HNo);
        colony = findViewById(R.id.colony);
        landmark = findViewById(R.id.landmark);
        city = findViewById(R.id.city);
        state = findViewById(R.id.state);
        pincode = findViewById(R.id.pincode);
        contactRadioGroup = findViewById(R.id.timeGroup);
        contactRadioGroup.clearCheck();
        contactRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                contactRadioButton = (RadioButton) radioGroup.findViewById(checkedId);
            }
        });
        beforeRadioButton = findViewById(R.id.beforeRadioButton);
        afterRadioButton = findViewById(R.id.afterRadioButton);
        anyTimeRadioButton = findViewById(R.id.anyTimeRadioButton);
        save = findViewById(R.id.save);

        features.setOnClickListener(this);
        save.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.features:
                new CommercialPropertyFeatures().show(getSupportFragmentManager(), "CommercialPropertyFeatures");
                break;
            case R.id.save:
               // checkValidations();
                Intent i = new Intent(getApplicationContext(), UploadPropertyImages.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Bundle bundle = new Bundle();
                bundle.putString("buttonName", "PropertyManagement");
                i.putExtra("bundle", bundle);
                startActivity(i);
                break;
        }
    }

    private void checkValidations() {
        noFloors = floors.getText().toString().trim();
        noFlats = flats.getText().toString().trim();
        extent = size.getText().toString().trim();
        noBlocks = blocks.getText().toString().trim();
        Rent = rent.getText().toString().trim();
        leaseAmount = deposit.getText().toString().trim();
        leaseDuration = duration.getText().toString().trim();
        flatNo = Hno.getText().toString().trim();
        Colony = colony.getText().toString().trim();
        Landmark = landmark.getText().toString().trim();
        City = city.getText().toString().trim();
        State = state.getText().toString().trim();
        Pincode = pincode.getText().toString().trim();
        splFeatures = features.getText().toString().trim();

        if (noFloors.length() == 0) {
            floors.setError("Enter Floors");
        } else if (noFlats.length() == 0) {
            flats.setError("Enter Flats");
        } else if (noBlocks.length() == 0) {
            blocks.setError("Enter Blocks");
        } else if (extent.length() == 0) {
            size.setError("Enter Size");
        } else if (splFeatures.equalsIgnoreCase("")) {
            Toast.makeText(this, "Select Features", Toast.LENGTH_LONG).show();
        } else if (Rent.length() == 0) {
            rent.setError("Enter Rent");
        } else if (leaseAmount.length() == 0) {
            deposit.setError("Enter Deposit Amount");
        } else if (leaseDuration.length() == 0) {
            duration.setError("Enter Lease Duration");
        } else if (flatNo.length() == 0) {
            Hno.setError("Enter House No.");
        } else if (Colony.length() == 0) {
            colony.setError("Enter Colony Name");
        } else if (City.length() == 0) {
            city.setError("Enter Floors");
        } else if (State.length() == 0) {
            state.setError("Enter City Name");
        } else if (Pincode.length() == 0) {
            pincode.setError("Enter Pincode");
        } else if (contactRadioGroup.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this, "Please Select Prefer Contact Time", Toast.LENGTH_LONG).show();
        } else {
            contactTime = contactRadioButton.getText().toString();
            Intent i = new Intent(getApplicationContext(), UploadPropertyImages.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Bundle bundle = new Bundle();
            bundle.putString("buttonName", "PropertyManagement");
            bundle.putBundle("b", b);
            bundle.putString("noFloors", noFloors);
            bundle.putString("noFlats", noFlats);
            bundle.putString("noBlocks", noBlocks);
            bundle.putString("extent", extent);
            bundle.putStringArray("splFeatures", seperated);
            bundle.putString("Rent", Rent);
            bundle.putString("leaseAmount", leaseAmount);
            bundle.putString("leaseDuration", leaseDuration);
            bundle.putString("flatNo", flatNo);
            bundle.putString("Colony", Colony);
            bundle.putString("Landmark", Landmark);
            bundle.putString("City", City);
            bundle.putString("State", State);
            bundle.putString("Pincode", Pincode);
            bundle.putString("contactTime", contactTime);
            i.putExtra("bundle", bundle);
            startActivity(i);
        }
    }

    @Override
    public void getFeatures(String feature) {
        features.setText(feature.substring(1));
        String s = feature.substring(1);
        seperated = s.split(",");
        Log.d("TAG", "getFeatures: " + seperated.length + "\n" + seperated + "\n" + Arrays.toString(seperated));
    }
}