/*
 * Name : ForgotPasswordActivity.java
 * Purpose : This file is used for validating mobile number aand otp to change the password
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
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import com.example.RentalManagement.Model.ForgotPasswordResponse;
import com.example.RentalManagement.R;
import com.example.RentalManagement.Services.ApiClient;
import com.example.RentalManagement.Services.ApiInterface;
import com.example.RentalManagement.Services.NetworkConnection;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPasswordActivity extends AppCompatActivity implements View.OnClickListener {
    /*declarations*/
    Toolbar toolbar;
    LinearLayout sendOtpLayout, submitOtpLayout;
    EditText mobileNumber, otp;
    Button sendOtp, submitOtp;
    ApiInterface apiInterface;
    ForgotPasswordResponse forgotPasswordResponse;
    NetworkConnection networkConnection;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        statusBarColor().setStatusBarColor(this.getResources().getColor(R.color.design_default_color_primary));

        /*initializations*/
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Forgot Password");
        toolbar.setNavigationIcon(R.drawable.arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });
        sendOtpLayout = findViewById(R.id.sendOtpLayout);
        mobileNumber = findViewById(R.id.mobileNumber);
        sendOtp = findViewById(R.id.sendOtp);
        submitOtpLayout = findViewById(R.id.submitOtpLayout);
        otp = findViewById(R.id.otp);
        submitOtp = findViewById(R.id.submitOtp);

        sendOtp.setOnClickListener(this);
        submitOtp.setOnClickListener(this);
        networkConnection = new NetworkConnection(this);
        progressDialog = new ProgressDialog(this, R.style.progressDialogStyle);
    }

    /*to change the statusbar color*/
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public Window statusBarColor() {
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        return window;
    }

    /*onclick for widgets*/
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sendOtp:
                if (mobileNumber.getText().length() == 0) {
                    mobileNumber.setError("Enter your number");
                } else if (mobileNumber.getText().length() < 10) {
                    mobileNumber.setError("Enter 10 digit Mobile Number");
                } else if (!mobileNumber.getText().toString().matches("[0-9.?]*")) {
                    mobileNumber.setError("Only numbers are allowed");
                } else if (mobileNumber.getText().toString().startsWith("0") ||
                        mobileNumber.getText().toString().startsWith("1") ||
                        mobileNumber.getText().toString().startsWith("2") ||
                        mobileNumber.getText().toString().startsWith("3") ||
                        mobileNumber.getText().toString().startsWith("4") ||
                        mobileNumber.getText().toString().startsWith("5")) {
                    mobileNumber.setError("Enter a valid number");
                } else {
                    if (mobileNumber.getText().toString().length() == 10) {
                        if (networkConnection.isConnectingToInternet()) {
                            try {
                                sendOtp(mobileNumber.getText().toString());
                            } catch (Exception e) {
                                if (progressDialog != null) {
                                    progressDialog.cancel();
                                }
                            }
                        } else {
                            Toast.makeText(this, "Please check your internet connection", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                break;
            case R.id.submitOtp:
                String getOtp = otp.getText().toString();
                if (getOtp.length() == 0) {
                    otp.setError("Enter OTP");
                } else if (getOtp.length() < 6) {
                    otp.setError("Enter 4 digit OTP");
                } else {
                    if (getOtp.length() == 6) {
                        if (networkConnection.isConnectingToInternet()) {
                            try {
                                submitOtp(mobileNumber.getText().toString(), getOtp);
                            } catch (Exception e) {
                                if (progressDialog != null) {
                                    progressDialog.cancel();
                                }
                            }
                        } else {
                            Toast.makeText(this, "Please check your internet connection", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                break;
        }
    }

    /*sending otp to the registered mobile number*/
    private void sendOtp(String mobileNumber) {
       /* for (int i = 0; i < sendOtpLayout.getChildCount(); i++) {
            View v = sendOtpLayout.getChildAt(i);
            v.setEnabled(false);
        }
        submitOtpLayout.setVisibility(View.VISIBLE);*/
       progressDialog.setTitle("Send OTP");
        progressDialog.setMessage("Please wait We are sending OTP...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);
        Call<ForgotPasswordResponse> call = apiInterface.getPasswordSendOtpStatus(new ForgotPasswordResponse(mobileNumber));
        call.enqueue(new Callback<ForgotPasswordResponse>() {
            @Override
            public void onResponse(Call<ForgotPasswordResponse> call, Response<ForgotPasswordResponse> response) {
                forgotPasswordResponse = response.body();
                try {
                    if (forgotPasswordResponse.getOTPNumber() != null) {
                        progressDialog.cancel();
                        for (int i = 0; i < sendOtpLayout.getChildCount(); i++) {
                            View v = sendOtpLayout.getChildAt(i);
                            v.setEnabled(false);
                        }
                        submitOtpLayout.setVisibility(View.VISIBLE);
                    }
                    progressDialog.cancel();
                } catch (Exception e) {
                    progressDialog.cancel();
                }
            }

            @Override
            public void onFailure(Call<ForgotPasswordResponse> call, Throwable t) {
                progressDialog.cancel();
            }
        });
    }

    /*validating otp*/
    private void submitOtp(final String mobileNumber, String otp) {
        /*Intent i = new Intent(getApplicationContext(), PasswordChange.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i.putExtra("mobileNumber",mobileNumber);
        startActivity(i);*/
        progressDialog.setMessage("Please wait ...");
        progressDialog.show();
        apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);
        Call<ForgotPasswordResponse> call = apiInterface.getPasswordSubmitOtpStatus(new ForgotPasswordResponse(mobileNumber, otp));
        call.enqueue(new Callback<ForgotPasswordResponse>() {
            @Override
            public void onResponse(Call<ForgotPasswordResponse> call, Response<ForgotPasswordResponse> response) {
                forgotPasswordResponse = response.body();
                try {
                    if (forgotPasswordResponse.getStatus().equals("OK")) {
                        progressDialog.cancel();
                        Intent i = new Intent(getApplicationContext(), PasswordChange.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        i.putExtra("mobilrNumber", mobileNumber);
                        startActivity(i);
                    }
                } catch (Exception e) {
                    progressDialog.cancel();
                }
            }

            @Override
            public void onFailure(Call<ForgotPasswordResponse> call, Throwable t) {
                progressDialog.cancel();
            }
        });
    }
}