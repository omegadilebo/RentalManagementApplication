/*
 * Name : PropertyManagementType.java
 * Purpose : this file used to add or select property Type for property management
 * Created By : Mahesh
 * Developers Involved : Mahesh
 */
package com.example.RentalManagement.Owner.PropertyManagement;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.RentalManagement.Dialogs.CityName;
import com.example.RentalManagement.Dialogs.PropertyLocated;
import com.example.RentalManagement.Dialogs.PropertyManagementPropertyType;
import com.example.RentalManagement.R;

import java.util.Objects;

public class PropertyManagementType extends AppCompatActivity implements
        View.OnClickListener,
        PropertyLocated.PropertyLocatedListener,
        CityName.CityNameListener,
        PropertyManagementPropertyType.PropertyTypeListener {
    /*declarations*/
    Toolbar toolbar;
    RadioGroup propertyRadioGroup, registerGroup;
    RadioButton propertyRadioButton, firmRadioButton, individualRadioButton,
            registerRadioButton, leaseRadioButton, saleRadioButton;
    LinearLayout dir_lay;
    EditText director1, director2, director3, director4;
    TextView remove, addmore, propertyLocated, cityName, propertyType, save;
    int i = 0;
    String property, registerType, dir1, dir2, dir3, dir4, located, cName, pType;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_property_type);
        /*initializations*/
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Property Type");
        toolbar.setNavigationIcon(R.drawable.arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        propertyRadioGroup = findViewById(R.id.propertyRadioGroup);
        propertyRadioGroup.clearCheck();
        dir_lay = findViewById(R.id.dir_lay);
        addmore = findViewById(R.id.addMore);
        remove = findViewById(R.id.remove);
        director1 = findViewById(R.id.director1);
        director2 = findViewById(R.id.director2);
        director3 = findViewById(R.id.director3);
        director4 = findViewById(R.id.director4);
        propertyRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                propertyRadioButton = (RadioButton) radioGroup.findViewById(checkedId);
                if (propertyRadioButton.getText().toString().equalsIgnoreCase("Firm")) {
                    dir_lay.setVisibility(View.VISIBLE);
                } else if (propertyRadioButton.getText().toString().equalsIgnoreCase("Individual")) {
                    dir_lay.setVisibility(View.GONE);
                }
            }
        });
        firmRadioButton = findViewById(R.id.firmRadioButton);
        individualRadioButton = findViewById(R.id.individualRadioButton);
        registerGroup = findViewById(R.id.registerGroup);
        registerGroup.clearCheck();
        registerGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                registerRadioButton = (RadioButton) radioGroup.findViewById(checkedId);
            }
        });
        leaseRadioButton = findViewById(R.id.leaseRadioButton);
        saleRadioButton = findViewById(R.id.saleRadioButton);
        propertyLocated = findViewById(R.id.propertyLocated);
        cityName = findViewById(R.id.cityName);
        propertyType = findViewById(R.id.propertyType);
        save = findViewById(R.id.save);

        addmore.setOnClickListener(this);
        remove.setOnClickListener(this);
        propertyLocated.setOnClickListener(this);
        cityName.setOnClickListener(this);
        propertyType.setOnClickListener(this);
        save.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.addMore:
                showDirectorsFields();
                break;
            case R.id.remove:
                removeDirectorsFields();
                break;
            case R.id.propertyLocated:
                new PropertyLocated().show(getSupportFragmentManager(), "propertyLocated");
                break;
            case R.id.cityName:
                new CityName().show(getSupportFragmentManager(), "cirtName");
                break;
            case R.id.propertyType:
                new PropertyManagementPropertyType().show(getSupportFragmentManager(), "propertyType");
                break;
            case R.id.save:
                checkValidations();
                break;
        }
    }

    private void removeDirectorsFields() {
        if (i == 0) {
            director3.setVisibility(View.GONE);
            remove.setVisibility(View.GONE);
        } else if (i == 1) {
            remove.setVisibility(View.VISIBLE);
            director4.setVisibility(View.GONE);
            i--;
        }
    }

    private void showDirectorsFields() {
        if (i == 0) {
            director3.setVisibility(View.VISIBLE);
            remove.setVisibility(View.VISIBLE);
            i++;
        } else if (i == 1) {
            remove.setVisibility(View.VISIBLE);
            director4.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void getPropertyType(String property) {
        propertyType.setText(property);
    }

    private void checkValidations() {
        located = propertyLocated.getText().toString();
        cName = cityName.getText().toString();
        pType = propertyType.getText().toString();

        if (propertyRadioGroup.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this, "Select Property Registered", Toast.LENGTH_LONG).show();
        } else if (registerGroup.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this, "Select Register For", Toast.LENGTH_LONG).show();
        } else if (located.equalsIgnoreCase("")) {
            Toast.makeText(this, "Select Property Located", Toast.LENGTH_LONG).show();
        } else if (cName.equalsIgnoreCase("")) {
            Toast.makeText(this, "Select City Name", Toast.LENGTH_LONG).show();
        } else if (pType.equalsIgnoreCase("")) {
            Toast.makeText(this, "Select Property Type", Toast.LENGTH_LONG).show();
        } else {
            property = propertyRadioButton.getText().toString();
            registerType = registerRadioButton.getText().toString();
            switch (pType) {
                case "Apartment":
                    intent = new Intent(getApplicationContext(), PropertyManagementDetails.class);
                    getDetails();
                    break;
                case "Shopping Complex":
                    intent = new Intent(getApplicationContext(), PropertyManagementDetails.class);
                    getDetails();
                    break;
                case "Godown":
                    intent = new Intent(getApplicationContext(), GodownDetails.class);
                    getDetails();
                    break;
                case "Open Place":
                    intent = new Intent(getApplicationContext(), OpenPlaceDetails.class);
                    getDetails();
                    break;
                case "Shops":
                    intent = new Intent(getApplicationContext(), ShopsDetails.class);
                    getDetails();
                    break;
            }
        }
    }

    @Override
    public void getCityName(String cityname) {
        cityName.setText(cityname);
    }

    @Override
    public void getPropertyLocated(String PropertyLocated) {
        propertyLocated.setText(PropertyLocated);
    }
    private void getDetails(){
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Bundle bundle = new Bundle();
        bundle.putString("property", property);
        bundle.putString("registerType", registerType);
        bundle.putString("located", located);
        bundle.putString("cName", cName);
        bundle.putString("pType", pType);
        if (property.equalsIgnoreCase("Firm")) {
            dir1 = director1.getText().toString();
            dir2 = director2.getText().toString();
            dir3 = director1.getText().toString();
            dir4 = director4.getText().toString();
            if (dir1.length() == 0 | dir2.length() == 0) {
                Toast.makeText(this, "Enter Directors Name", Toast.LENGTH_LONG).show();
            } else {
                bundle.putString("dir1", dir1);
                bundle.putString("dir2", dir2);
                bundle.putString("dir3", dir3);
                bundle.putString("dir4", dir4);
                intent.putExtra("bundle", bundle);
                startActivity(intent);
            }
        } else if (propertyRadioButton.getText().toString().equalsIgnoreCase("Individual")) {
            intent.putExtra("bundle", bundle);
            startActivity(intent);
        }
    }
}