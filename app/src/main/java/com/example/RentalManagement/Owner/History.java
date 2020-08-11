/*
 * Name : History.java
 * Purpose : This file used to get property history and owner can add new property
 * Created By : Mahesh
 * Developers Involved : Mahesh
 */
package com.example.RentalManagement.Owner;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.RentalManagement.Activities.SelectRole;
import com.example.RentalManagement.Dialogs.LogOut;
import com.example.RentalManagement.Owner.Adapters.HistoryAdapter;
import com.example.RentalManagement.Owner.Model.HistoryResponse;
import com.example.RentalManagement.R;
import com.example.RentalManagement.Services.ApiClient;
import com.example.RentalManagement.Services.ApiInterface;
import com.example.RentalManagement.Services.NetworkConnection;
import com.example.RentalManagement.utils.Config;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class History extends AppCompatActivity {
    /*declarations*/
    Toolbar toolbar;
    TextView addProperty;
    RecyclerView recyclerView;
    HistoryAdapter adapter;
    HistoryResponse historyResponse;
    List<HistoryResponse> data;
    NetworkConnection networkConnection;
    ProgressDialog progressDialog;
    ApiInterface apiInterface;
    /*dummy images*/
    int[] images = new int[]{R.drawable.inside,
            R.drawable.specialarea, R.drawable.outside
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        statusBarColor().setStatusBarColor(this.getResources().getColor(R.color.design_default_color_primary));
        /*initilizations*/
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("History");
        toolbar.setNavigationIcon(R.drawable.arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });
        addProperty = findViewById(R.id.addProperty);
        addProperty.setBackgroundColor(getResources().getColor(R.color.design_default_color_primary));
        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        adapter = new HistoryAdapter(images,getApplicationContext());
        recyclerView.setAdapter(adapter);
        networkConnection = new NetworkConnection(this);
        progressDialog = new ProgressDialog(this, R.style.progressDialogStyle);
        //getHistory();
        addProperty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), AddProperty.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra("buttonName","addProperty");
                startActivity(i);
            }
        });
    }

    /*
        to get history of the owner properties
    */
    /*private void getHistory() {
        if (networkConnection.isConnectingToInternet()) {
            try {
                progressDialog.setMessage("Fetching Previous Adds.....");
                progressDialog.setCancelable(false);
                progressDialog.show();
                apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);
                Call<List<HistoryResponse>> call = apiInterface.getHistory(9);
                call.enqueue(new Callback<List<HistoryResponse>>() {
                    @Override
                    public void onResponse(Call<List<HistoryResponse>> call, Response<List<HistoryResponse>> response) {
                        data = response.body();
                        Log.d("TAG", "getHistory1: "+data);
                        try {
                            Log.d("TAG", "getHistory2: ");
                            progressDialog.cancel();
                            adapter = new HistoryAdapter(data, getApplicationContext());
                            recyclerView.setAdapter(adapter);
                        } catch (Exception e) {
                            Log.d("TAG", "getHistory3: "+e);
                            progressDialog.cancel();
                        }
                    }
                    @Override
                    public void onFailure(Call<List<HistoryResponse>> call, Throwable t) {
                        Log.d("TAG", "getHistory4: "+t);
                        progressDialog.cancel();
                    }
                });
            } catch (Exception e) {
                Log.d("TAG", "getHistory5: "+e);
                if (progressDialog != null) {
                    progressDialog.cancel();
                }
            }
        } else {
            Toast.makeText(this, "Please check your internet connection", Toast.LENGTH_SHORT).show();
        }
    }*/

    /*changing statusbar color*/
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public Window statusBarColor() {
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        return window;
    }

    /*toobar icons initilization and onclick*/
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

    /*onback pressed*/
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(getApplicationContext(), SelectRole.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }
}