package com.example.RentalManagement.Tenant;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.RentalManagement.R;

public class PropertyImage extends AppCompatActivity {
    Toolbar toolbar;
ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_image);
        Bundle bundle = getIntent().getBundleExtra("bundle");
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.black_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        imageView = findViewById(R.id.image);
        imageView.setImageResource(bundle.getInt("imageData"));
    }
}