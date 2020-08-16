/*
 * Name : History.java
 * Purpose : This file used to get property history and owner can add new property
 * Created By : Mahesh
 * Developers Involved : Mahesh
 */
package com.example.RentalManagement.Owner.Residential.Activities;

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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.RentalManagement.Activities.SelectRole;
import com.example.RentalManagement.Dialogs.LogOut;
import com.example.RentalManagement.Dialogs.RemoveProperty;
import com.example.RentalManagement.Owner.Residential.Adapters.HistoryAdapter;
import com.example.RentalManagement.Owner.Residential.Models.HistoryResponse;
import com.example.RentalManagement.R;
import com.example.RentalManagement.Services.ApiClient;
import com.example.RentalManagement.Services.ApiInterface;
import com.example.RentalManagement.Services.NetworkConnection;
import com.example.RentalManagement.utils.Config;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class History extends AppCompatActivity implements RemoveProperty.RemovePropertyListener {
    /*declarations*/
    Toolbar toolbar;
    TextView addProperty;
    RecyclerView recyclerView;
    HistoryAdapter adapter;
    HistoryResponse historyResponse;
    List<HistoryResponse.data> data;
    NetworkConnection networkConnection;
    ProgressDialog progressDialog;
    ApiInterface apiInterface;
    /*dummy images*/
    List<Integer> images;
    int position, propertyId;
    SwipeRefreshLayout swipeRefreshLayout;

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
        images = new ArrayList<>();
        images.add(R.drawable.inside);
        images.add(R.drawable.specialarea);
        images.add(R.drawable.outside);
        data = new ArrayList<>();
        addProperty = findViewById(R.id.addProperty);
        addProperty.setBackgroundColor(getResources().getColor(R.color.design_default_color_primary));
        swipeRefreshLayout = findViewById(R.id.swipeRefresh);
        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        /*adapter = new HistoryAdapter(images, getApplicationContext(), new HistoryAdapter.OnRemovePropertyListener() {
            @Override
            public void onRemoveProperty(View v, int adapterPosition, int propertyId) {
                position = adapterPosition;
                RemoveProperty removeProperty= new RemoveProperty();
                removeProperty.show(getFragmentManager(), "RemoveProperty");
            }
        });*/

        // recyclerView.setAdapter(adapter);
        networkConnection = new NetworkConnection(this);
        progressDialog = new ProgressDialog(this, R.style.progressDialogStyle);
        //  getHistory();
        addProperty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), AddProperty.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra("buttonName", "addProperty");
                startActivity(i);
            }
        });
        swipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        getHistory();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }
        );
    }

    /*
        to get history of the owner properties
    */
    private void getHistory() {
        if (networkConnection.isConnectingToInternet()) {
            try {
                progressDialog.setMessage("Fetching Previous Adds.....");
                progressDialog.setCancelable(false);
                progressDialog.show();
                apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);
                Call<HistoryResponse> call = apiInterface.getHistory(Integer.parseInt(Config.getUserId(getApplicationContext())));
                call.enqueue(new Callback<HistoryResponse>() {
                    @Override
                    public void onResponse(Call<HistoryResponse> call, Response<HistoryResponse> response) {
                        historyResponse = response.body();
                        Log.d("TAG", "getHistory1: " + data);
                        try {
                            if (historyResponse.getStatus().equalsIgnoreCase("1")) {
                                progressDialog.dismiss();
                                if (historyResponse.getStatus().equalsIgnoreCase("true")) {
                                    Log.d("TAG", "onResponse: status1" + historyResponse.getData().length);
                                    for (int i = 0; i < historyResponse.getData().length; i++) {
                                        data.add(historyResponse.getData()[i]);
                                    }
                                }
                                adapter = new HistoryAdapter(data, getApplicationContext(), new HistoryAdapter.OnRemovePropertyListener() {
                                    @Override
                                    public void onRemoveProperty(View v, int adapterPosition, int id) {
                                        position = adapterPosition;
                                        propertyId = id;
                                        RemoveProperty removeProperty = new RemoveProperty();
                                        removeProperty.show(getFragmentManager(), "RemoveProperty");
                                    }
                                });
                                //adapter = new HistoryAdapter(data, RideHistory.this,getApplicationContext());
                                recyclerView.setAdapter(adapter);
                            } else {
                                progressDialog.dismiss();

                            }
                        } catch (Exception e) {
                            Log.d("TAG", "getHistory3: " + e);
                            if (progressDialog != null) {
                                progressDialog.cancel();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<HistoryResponse> call, Throwable t) {
                        Log.d("TAG", "getHistory4: " + t);
                        progressDialog.cancel();
                    }
                });
            } catch (Exception e) {
                Log.d("TAG", "getHistory5: " + e);
                if (progressDialog != null) {
                    progressDialog.cancel();
                }
            }
        } else {
            Toast.makeText(this, "Please check your internet connection", Toast.LENGTH_SHORT).show();
        }
    }

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


    @Override
    public void getRemovePropertyDialogItem(String value) {
        try {
            adapter.removeItem(position);
            //removeData(propertyId);
        } catch (Exception e) {
            Log.d("TAG", "getRemovePropertyDialogItem: " + e);
        }
    }

  /*  private void removeData(int propertyId) {
        if (networkConnection.isConnectingToInternet()) {
            progressDialog.setTitle("Please Wait Removing Add..");
            progressDialog.setCancelable(false);
            progressDialog.show();
            apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);
            Call<HistoryResponse> call = apiInterface.removeDataItem(propertyId);
            call.enqueue(new Callback<HistoryResponse>() {
                @Override
                public void onResponse(Call<HistoryResponse> call, Response<HistoryResponse> response) {
                    try {
                        if (progressDialog != null) {
                            progressDialog.cancel();
                        }
                        adapter.removeItem(position);
                    } catch (Exception e) {
                        if (progressDialog != null) {
                            progressDialog.cancel();
                        }
                    }
                }

                @Override
                public void onFailure(Call<HistoryResponse> call, Throwable t) {
                    if (progressDialog != null) {
                        progressDialog.cancel();
                    }
                }
            });
        } else {
            Toast.makeText(this, R.string.no_net, Toast.LENGTH_LONG).show();
        }
    }*/

}