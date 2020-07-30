/*
 * Name : SearchPropertyResults.java
 * Purpose : This file is used to fetch the results searched by tenant
 * Created By : Mahesh
 * Developers Involved : Mahesh
 */
package com.example.RentalManagement.Tenant;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.RentalManagement.R;
import com.example.RentalManagement.Services.ApiClient;
import com.example.RentalManagement.Services.ApiInterface;
import com.example.RentalManagement.Services.NetworkConnection;
import com.example.RentalManagement.Tenant.Adapters.SearchPropertyResultsAdapter;
import com.example.RentalManagement.Tenant.Model.SearchPropertyResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchPropertyResults extends AppCompatActivity {
    String category, propertyType, locality, subLocality, apartmentType, bhk, rent, extent;
    Bundle b;
    Toolbar toolbar;
    RecyclerView recyclerView;
    int[] images = new int[]{R.drawable.img3,
            R.drawable.img4, R.drawable.img6,
            R.drawable.img7, R.drawable.img8};
    SearchPropertyResultsAdapter adapter;
    SearchPropertyResponse searchPropertyResponse;
    ApiInterface apiInterface;
    NetworkConnection networkConnection;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_property_results);
        /*getting values from previous activity*/
        Intent i = getIntent();
        b = i.getBundleExtra("b");
        category = b.getString("category");
        propertyType = b.getString("propertyType");
        locality = b.getString("locality");
        subLocality = b.getString("subLocality");
        apartmentType = b.getString("apartmentType");
        bhk = b.getString("bhk");
        rent = b.getString("rent");
        extent = b.getString("extent");
       // getSearchHistory(category, propertyType, locality, subLocality, apartmentType, bhk, rent, extent);
        Log.d("TAG", "onCreate: " + category + "\n" +
                propertyType + "\n" + locality + "\n" + subLocality + "\n" + apartmentType + "\n" + bhk + "\n" + rent + "\n" + extent);
        networkConnection = new NetworkConnection(this);
        progressDialog = new ProgressDialog(this,R.style.progressDialogStyle);
        statusBarColor().setStatusBarColor(this.getResources().getColor(R.color.design_default_color_primary));
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Search Results");
        toolbar.setNavigationIcon(R.drawable.arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        adapter = new SearchPropertyResultsAdapter(images, getApplicationContext());
        recyclerView.setAdapter(adapter);

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

    /*get searched results*/
    private void getSearchHistory(String category, String propertyType,
                                  String locality, String subLocality, String apartmentType,
                                  String bhk, String rent, String extent) {
        if(networkConnection.isConnectingToInternet()) {
            try {
                progressDialog.setMessage("Loading Details...");
                progressDialog.setCancelable(false);
                progressDialog.show();
                apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);
                Call<SearchPropertyResponse> call = apiInterface.getSearchPropertyResults(
                        new SearchPropertyResponse(category, propertyType, locality,
                                subLocality, apartmentType, bhk, rent, extent)
                );
                call.enqueue(new Callback<SearchPropertyResponse>() {
                    @Override
                    public void onResponse(Call<SearchPropertyResponse> call, Response<SearchPropertyResponse> response) {
                        searchPropertyResponse = response.body();
                        progressDialog.cancel();
                    }

                    @Override
                    public void onFailure(Call<SearchPropertyResponse> call, Throwable t) {
                        progressDialog.cancel();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                progressDialog.cancel();
                Toast.makeText(this, "Please Try again Later", Toast.LENGTH_SHORT).show();
            }
        } else {
        Toast.makeText(this, "Please check your internet connection", Toast.LENGTH_SHORT).show();
    }
    }


}