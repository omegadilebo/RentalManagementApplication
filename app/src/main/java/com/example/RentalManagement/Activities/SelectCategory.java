/*
 * Name : SelectCategory.java
 * Purpose : This file is used select category ex:rental, hostels, propertymanagement
 * Created By : Mahesh
 * Developers Involved : Mahesh
 */
package com.example.RentalManagement.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.RentalManagement.Dialogs.LogOut;
import com.example.RentalManagement.Owner.PropertyManagement.PropertyManagementType;
import com.example.RentalManagement.R;

public class SelectCategory extends AppCompatActivity implements View.OnClickListener {
    /*declarations*/
    Toolbar toolbar;
    TextView rental, property, nearShops, hostel, next;
    TextView packers,movers;
    LinearLayout packersMovers;
    String category = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_category);
        statusBarColor().setStatusBarColor(this.getResources().getColor(R.color.design_default_color_primary));
        /*initializations*/
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Select Category");
        rental = findViewById(R.id.rental);
        property = findViewById(R.id.property);
        packersMovers = findViewById(R.id.packersMovers);
        //inside of packers&movers layout
        packers = findViewById(R.id.packers);
        movers = findViewById(R.id.movers);
        nearShops = findViewById(R.id.nearShops);
        hostel = findViewById(R.id.hostel);
        next = findViewById(R.id.next);
        next.setBackgroundColor(getResources().getColor(R.color.design_default_color_primary));
        rental.setOnClickListener(this);
        property.setOnClickListener(this);
        packersMovers.setOnClickListener(this);
        nearShops.setOnClickListener(this);
        hostel.setOnClickListener(this);
        next.setOnClickListener(this);
    }

    /*changing status bar color*/
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public Window statusBarColor() {
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        return window;
    }

    /*toolbar icons initialization and onclick */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_icons,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.profile:
                Toast.makeText(this, "profile", Toast.LENGTH_SHORT).show();
                break;
            case R.id.logOut:
                new LogOut().show(getFragmentManager(),"LogOut");
                break;
        }
        return true;
    }

    /*onclick widgets*/
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rental:
                onClickRental();
                break;
            case R.id.property:
                onClickPropertyManagement();
                break;
            case R.id.packersMovers:
                onClickPackersMovers();
                break;
            case R.id.nearShops:
                onClicknearShops();
                break;
            case R.id.hostel:
                onClickHostels();
                break;
            case R.id.next:
                if (category == "") {
                    Toast.makeText(this, "Select Any Category", Toast.LENGTH_SHORT).show();
                } else {
                    switch (category){
                        case "Rental":
                            Intent i = new Intent(this, SelectRole.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(i);
                            break;
                        case "Property Management":
                            i = new Intent(this, PropertyManagementType.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(i);
                            break;
                    }
                }
                break;
        }
    }
    /*when click on rental*/
    private void onClickRental(){
        category = rental.getText().toString();
        rental.setBackgroundColor(getResources().getColor(R.color.design_default_color_primary));
        rental.setTextColor(Color.WHITE);
        property.setBackground(getDrawable(R.drawable.rectangular_shape));
        property.setTextColor(Color.BLACK);
        packersMovers.setBackground(getDrawable(R.drawable.rectangular_shape));
        packers.setTextColor(Color.BLACK);
        movers.setTextColor(Color.BLACK);
        nearShops.setBackground(getDrawable(R.drawable.rectangular_shape));
        nearShops.setTextColor(Color.BLACK);
        hostel.setBackground(getDrawable(R.drawable.rectangular_shape));
        hostel.setTextColor(Color.BLACK);
    }
    /*when click on PropertyManagement*/
    private void onClickPropertyManagement(){
        category = property.getText().toString();
        property.setBackgroundColor(getResources().getColor(R.color.design_default_color_primary));
        property.setTextColor(Color.WHITE);
        rental.setBackground(getDrawable(R.drawable.rectangular_shape));
        rental.setTextColor(Color.BLACK);
        packersMovers.setBackground(getDrawable(R.drawable.rectangular_shape));
        packers.setTextColor(Color.BLACK);
        movers.setTextColor(Color.BLACK);
        nearShops.setBackground(getDrawable(R.drawable.rectangular_shape));
        nearShops.setTextColor(Color.BLACK);
        hostel.setBackground(getDrawable(R.drawable.rectangular_shape));
        hostel.setTextColor(Color.BLACK);
    }
    /*when click on PackersMovers*/
    private void onClickPackersMovers(){
        category = packers.getText().toString()+movers.getText().toString();
        packersMovers.setBackgroundColor(getResources().getColor(R.color.design_default_color_primary));
        packers.setTextColor(Color.WHITE);
        movers.setTextColor(Color.WHITE);
        property.setBackground(getDrawable(R.drawable.rectangular_shape));
        property.setTextColor(Color.BLACK);
        rental.setBackground(getDrawable(R.drawable.rectangular_shape));
        rental.setTextColor(Color.BLACK);
        nearShops.setBackground(getDrawable(R.drawable.rectangular_shape));
        nearShops.setTextColor(Color.BLACK);
        hostel.setBackground(getDrawable(R.drawable.rectangular_shape));
        hostel.setTextColor(Color.BLACK);
    }
    /*when click on nearShops*/
    private void onClicknearShops(){
        category = nearShops.getText().toString();
        nearShops.setBackgroundColor(getResources().getColor(R.color.design_default_color_primary));
        nearShops.setTextColor(Color.WHITE);
        property.setBackground(getDrawable(R.drawable.rectangular_shape));
        property.setTextColor(Color.BLACK);
        packersMovers.setBackground(getDrawable(R.drawable.rectangular_shape));
        packers.setTextColor(Color.BLACK);
        movers.setTextColor(Color.BLACK);
        rental.setBackground(getDrawable(R.drawable.rectangular_shape));
        rental.setTextColor(Color.BLACK);
        hostel.setBackground(getDrawable(R.drawable.rectangular_shape));
        hostel.setTextColor(Color.BLACK);
    }
    /*when click on Hostels*/
    private void onClickHostels(){
        category = hostel.getText().toString();
        hostel.setBackgroundColor(getResources().getColor(R.color.design_default_color_primary));
        hostel.setTextColor(Color.WHITE);
        property.setBackground(getDrawable(R.drawable.rectangular_shape));
        property.setTextColor(Color.BLACK);
        packersMovers.setBackground(getDrawable(R.drawable.rectangular_shape));
        packers.setTextColor(Color.BLACK);
        movers.setTextColor(Color.BLACK);
        rental.setBackground(getDrawable(R.drawable.rectangular_shape));
        rental.setTextColor(Color.BLACK);
        nearShops.setBackground(getDrawable(R.drawable.rectangular_shape));
        nearShops.setTextColor(Color.BLACK);
    }
    /*onback pressed*/
    private Boolean exit = false;
    @Override
    public void onBackPressed() {
        if (exit) {
            Intent a = new Intent(Intent.ACTION_MAIN);
            a.addCategory(Intent.CATEGORY_HOME);
            a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(a);
        } else {
            Toast.makeText(this, "Press Back again to Exit from App.",
                    Toast.LENGTH_SHORT).show();
            exit = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            }, (3 * 1000));
        }
    }
}