/*
 * Name : SelectRole.java
 * Purpose : This file is used select role owner or tenant
 * Created By : Mahesh
 * Developers Involved : Mahesh
 */
package com.example.RentalManagement.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.RentalManagement.Dialogs.LogOut;
import com.example.RentalManagement.Owner.Commercial.Activities.CommercialAddProperty;
import com.example.RentalManagement.Owner.Residential.Activities.History;
import com.example.RentalManagement.R;
import com.example.RentalManagement.Tenant.SearchProperty;


public class SelectRole extends AppCompatActivity implements View.OnClickListener {
    /*declarations*/
    Toolbar toolbar;
    TextView owner, tenant, residential, commercial, next;
    RelativeLayout relativeLayout;
    String role = "", propertyType = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_role);
        statusBarColor().setStatusBarColor(this.getResources().getColor(R.color.design_default_color_primary));
        /*initializations*/
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Select Role");
        toolbar.setNavigationIcon(R.drawable.arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        owner = findViewById(R.id.owner);
        tenant = findViewById(R.id.tenant);
        relativeLayout = findViewById(R.id.relativeLayout);
        residential = findViewById(R.id.residential);
        commercial = findViewById(R.id.commercial);
        next = findViewById(R.id.next);
        next.setBackgroundColor(getResources().getColor(R.color.design_default_color_primary));
        owner.setOnClickListener(this);
        tenant.setOnClickListener(this);
        residential.setOnClickListener(this);
        commercial.setOnClickListener(this);
        next.setOnClickListener(this);
    }

    /*change status bar color*/
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public Window statusBarColor() {
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        return window;
    }

    /*onclick of widgets*/
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.owner:
                onClickOwner();
                break;
            case R.id.tenant:
                onClickTenant();
                break;
            case R.id.residential:
                onClickResidential();
                break;
            case R.id.commercial:
                onClickCommercial();
                break;
            case R.id.next:
                if (role == "") {
                    Toast.makeText(this, "Select Any Role", Toast.LENGTH_SHORT).show();
                } else {
                    switch (role) {
                        case "Property Owner":
                            if (propertyType == "") {
                                Toast.makeText(this, "Select Property Type", Toast.LENGTH_SHORT).show();
                            } else if (propertyType.equalsIgnoreCase("Residential")) {
                                Intent i = new Intent(this, History.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(i);
                            } else if (propertyType.equalsIgnoreCase("Commercial")) {
                                Intent i = new Intent(this, CommercialAddProperty.class);
                                //  Intent i = new Intent(this, AddProperty.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(i);
                            }
                            break;
                        case "Tenant":
                            Log.d("TAG", "onClick: " + propertyType);
                            if (propertyType == "") {
                                Toast.makeText(this, "Select Property Type", Toast.LENGTH_SHORT).show();
                            } else if (propertyType.equalsIgnoreCase("Residential")) {
                                Intent i = new Intent(this, SearchProperty.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(i);
                            } else if (propertyType.equalsIgnoreCase("Commercial")) {
                                /*if tenant click on commercial*/
                            }
                            break;
                    }
                }
                break;
        }
    }

    /*onclick owner*/
    private void onClickOwner() {
        role = owner.getText().toString();
        owner.setBackground(getDrawable(R.drawable.rectangular_shape));
        owner.setBackgroundColor(getResources().getColor(R.color.design_default_color_primary));
        owner.setTextColor(Color.WHITE);
        relativeLayout.setVisibility(View.VISIBLE);
        tenant.setBackground(getDrawable(R.drawable.rectangular_shape));
        tenant.setTextColor(Color.BLACK);
    }

    /*onclick tenant*/
    private void onClickTenant() {
        role = tenant.getText().toString();
        relativeLayout.setVisibility(View.VISIBLE);
        tenant.setBackground(getDrawable(R.drawable.rectangular_shape));
        tenant.setBackgroundColor(getResources().getColor(R.color.design_default_color_primary));
        tenant.setTextColor(Color.WHITE);
        owner.setBackground(getDrawable(R.drawable.rectangular_shape));
        owner.setTextColor(Color.BLACK);
    }

    /*onclick residential*/
    private void onClickResidential() {
        propertyType = residential.getText().toString();
        residential.setBackgroundColor(getResources().getColor(R.color.design_default_color_primary));
        residential.setTextColor(Color.WHITE);
        commercial.setBackground(getDrawable(R.drawable.rectangular_shape));
        commercial.setTextColor(Color.BLACK);
    }

    /*onclick commercial*/
    private void onClickCommercial() {
        propertyType = commercial.getText().toString();
        commercial.setBackgroundColor(getResources().getColor(R.color.design_default_color_primary));
        commercial.setTextColor(Color.WHITE);
        residential.setBackground(getDrawable(R.drawable.rectangular_shape));
        residential.setTextColor(Color.BLACK);
    }

    /*toobar icons initializations and onclick*/
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
}