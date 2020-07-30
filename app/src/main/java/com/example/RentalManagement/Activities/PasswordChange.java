/*
 * Name : PasswordChange.java
 * Purpose : This file is used for updating oldpassword with new password
 * Created By : Mahesh
 * Developers Involved : Mahesh
 */
package com.example.RentalManagement.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import com.example.RentalManagement.Model.ChangePasswordResponse;
import com.example.RentalManagement.R;
import com.example.RentalManagement.Services.ApiClient;
import com.example.RentalManagement.Services.ApiInterface;
import com.example.RentalManagement.Services.NetworkConnection;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PasswordChange extends AppCompatActivity {
    /*declarations*/
    Toolbar toolbar;
    EditText password, confirmPassword;
    Button updatePassword;
    ApiInterface apiInterface;
    ChangePasswordResponse changePasswordResponse;
    NetworkConnection networkConnection;
    ProgressDialog progressDialog;
    String mobileNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_change);
        statusBarColor().setStatusBarColor(this.getResources().getColor(R.color.design_default_color_primary));
        /*initilizations*/
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Change Password");
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirmPassword);
        updatePassword = findViewById(R.id.updatePassword);
        Intent i = getIntent();
        mobileNumber = i.getStringExtra("mobileNumber");
        networkConnection = new NetworkConnection(this);
        progressDialog = new ProgressDialog(this, R.style.progressDialogStyle);

        /*onclick for update password*/
        updatePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chekingValidatiions();
            }
        });
    }

    /*checking validations*/
    private void chekingValidatiions() {
        String getPassword = password.getText().toString().trim();
        String getConfirmPassword = confirmPassword.getText().toString().trim();
        if (getPassword.length() == 0) {
            password.setError("Enter Password");
        } else if (getPassword.length() < 8) {
            password.setError("Password Should be 8 charcters");
        } else if (getConfirmPassword.length() == 0) {
            confirmPassword.setError("Enter Password");
        } else if (getConfirmPassword.length() < 8) {
            confirmPassword.setError("Password Should be 8 charcters");
        } else {
            if (getPassword.equals(getConfirmPassword)) {
                if (networkConnection.isConnectingToInternet()) {
                    try {
                        changePassword(mobileNumber, getPassword);
                    } catch (Exception e) {
                        if (progressDialog != null) {
                            progressDialog.cancel();
                        }
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Please check your internet connection", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getApplicationContext(), "Both password & confirm password should be same", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /*updating new password*/
    private void changePassword(String mobileNumber, String newPassword) {
        /*Intent i = new Intent(getApplicationContext(), LoginActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);*/
        progressDialog.setMessage("Please wait ...");
        progressDialog.show();
        apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);
        Call<ChangePasswordResponse> call = apiInterface.getChangePasswordStatus(new ChangePasswordResponse(mobileNumber, newPassword));
        call.enqueue(new Callback<ChangePasswordResponse>() {
            @Override
            public void onResponse(Call<ChangePasswordResponse> call, Response<ChangePasswordResponse> response) {
                changePasswordResponse = response.body();
                try {
                    if (changePasswordResponse.getStatus().equalsIgnoreCase("success")) {
                        progressDialog.cancel();
                        Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                        Toast.makeText(PasswordChange.this, "Password Changed Successfully", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    progressDialog.cancel();
                }
            }

            @Override
            public void onFailure(Call<ChangePasswordResponse> call, Throwable t) {
                progressDialog.cancel();
            }
        });
    }

    /*to change statusbar color*/
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public Window statusBarColor() {
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        return window;
    }

    /*onback presses*/
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(getApplicationContext(), LoginActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }
}