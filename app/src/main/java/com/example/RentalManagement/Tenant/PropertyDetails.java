/*
 * Name : PropertyDetails.java
 * Purpose : This file is used to get searched results by tenant
 * Created By : Mahesh
 * Developers Involved : Mahesh
 */
package com.example.RentalManagement.Tenant;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.example.RentalManagement.R;
import com.example.RentalManagement.Services.NetworkConnection;
import com.example.RentalManagement.Tenant.Adapters.PropertyImagePagerAdapter;


public class PropertyDetails extends AppCompatActivity {
    Toolbar toolbar;
    TextView contactOwner;
    ViewPager viewPager;
    PropertyImagePagerAdapter adapter;
    int[] images = new int[]{R.drawable.inside,
            R.drawable.specialarea, R.drawable.outside};
    NetworkConnection networkConnection;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_details);
        networkConnection = new NetworkConnection(this);
        statusBarColor().setStatusBarColor(this.getResources().getColor(R.color.design_default_color_primary));
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Property Details");
        toolbar.setNavigationIcon(R.drawable.arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        viewPager = findViewById(R.id.viewPager);
        adapter = new PropertyImagePagerAdapter(this, images);
        viewPager.setAdapter(adapter);
        contactOwner = findViewById(R.id.contactOwner);
        contactOwner.setBackgroundColor(getResources().getColor(R.color.design_default_color_primary));
    }
    /*change the statusbar color starts here*/
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public Window statusBarColor() {
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        return window;
    }
    /*change the statusbar color ends here */
}