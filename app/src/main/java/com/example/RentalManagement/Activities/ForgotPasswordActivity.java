/*
 * Name : ForgotPasswordActivity.java
 * Purpose : This file is used for validating mobile number aand otp to change the password
 * Created By : Mahesh
 * Developers Involved : Mahesh
 */
package com.example.RentalManagement.Activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;


import com.example.RentalManagement.BuildConfig;
import com.example.RentalManagement.Model.ForgotPasswordResponse;
import com.example.RentalManagement.Model.ForgotPasswordResponse;
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
    NetworkConnection networkConnection;
    ProgressDialog progressDialog;
    String MobileNo,OTPNumber, IMEI;
    TelephonyManager telephonyManager;
    public static int REQUEST_CODE = 200;
    ForgotPasswordResponse forgotPasswordResponse;

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
        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
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
                MobileNo = mobileNumber.getText().toString().trim();
                if (MobileNo.length() == 0) {
                    mobileNumber.setError("Enter your number");
                } else if (MobileNo.length() < 10) {
                    mobileNumber.setError("Enter 10 digit Mobile Number");
                }  else {
                    if (MobileNo.length() == 10 & MobileNo.matches("^[6-9]\\d{9}$")) {
                        if (networkConnection.isConnectingToInternet()) {
                            if (ActivityCompat.checkSelfPermission(ForgotPasswordActivity.this, Manifest.permission.READ_PHONE_STATE)
                                    != PackageManager.PERMISSION_GRANTED) {
                                ActivityCompat.requestPermissions(ForgotPasswordActivity.this, new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_CODE);
                            } else {
                                    IMEI = telephonyManager.getDeviceId();
                                sendOtp(MobileNo, IMEI);
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
                    otp.setError("Enter 6 digit OTP");
                } else {
                    if (getOtp.length() == 6) {
                        if (networkConnection.isConnectingToInternet()) {
                                if(getOtp.equals(OTPNumber)){
                                    Intent i = new Intent(getApplicationContext(), PasswordChange.class);
                                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    i.putExtra("MobileNo",MobileNo);
                                    startActivity(i);
                                }else{
                                    Toast.makeText(this, "Please Enter Correct Otp", Toast.LENGTH_LONG).show();
                                }
                        } else {
                            Toast.makeText(this, "Please check your internet connection", Toast.LENGTH_LONG).show();
                        }
                    }
                }
                break;
        }
    }

    private void sendOtp(String mobileNo, String imei) {
        try{
            /*for (int i = 0; i < sendOtpLayout.getChildCount(); i++) {
                View v = sendOtpLayout.getChildAt(i);
                v.setEnabled(false);
            }
            submitOtpLayout.setVisibility(View.VISIBLE);*/

            progressDialog.setTitle("Send OTP");
            progressDialog.setMessage("Please wait We are sending OTP...");
            progressDialog.setCancelable(false);
            progressDialog.show();
            apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);
            Log.d("TAG", "sendOtp: "+mobileNo+"\n"+imei);
            Call<ForgotPasswordResponse> call = apiInterface.getPasswordSendOtpStatus(new ForgotPasswordResponse(mobileNo,imei));
            call.enqueue(new Callback<ForgotPasswordResponse>() {
                @Override
                public void onResponse(Call<ForgotPasswordResponse> call, Response<ForgotPasswordResponse> response) {
                    forgotPasswordResponse = response.body();
                    Log.d("TAG", "sendOtp1: "+response.body());
                    try {
                        if (forgotPasswordResponse.getStatus().equals("1")) {
                            OTPNumber = forgotPasswordResponse.getOTPNumber();
                            Log.d("TAG", "sendOtp1: "+OTPNumber);
                            Toast.makeText(ForgotPasswordActivity.this, OTPNumber, Toast.LENGTH_LONG).show();
                            progressDialog.cancel();
                            for (int i = 0; i < sendOtpLayout.getChildCount(); i++) {
                                View v = sendOtpLayout.getChildAt(i);
                                v.setEnabled(false);
                            }
                            submitOtpLayout.setVisibility(View.VISIBLE);
                        }else if (forgotPasswordResponse.getStatus().equals("0")){
                            Log.d("TAG", "sendOtp2: "+forgotPasswordResponse.getStatus());
                            Toast.makeText(ForgotPasswordActivity.this, "Please Enter a Registered Number", Toast.LENGTH_LONG).show();
                            progressDialog.cancel();
                        }else {
                            Log.d("TAG", "sendOtp3: "+forgotPasswordResponse.getStatus());
                            progressDialog.cancel();
                            Toast.makeText(ForgotPasswordActivity.this, "Please Try Again Later", Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception e) {
                        progressDialog.cancel();
                        Log.d("TAG", "sendOtp4: "+e);
                        Toast.makeText(ForgotPasswordActivity.this, "Please Try Again Later", Toast.LENGTH_LONG).show();
                    }
                }
                @Override
                public void onFailure(Call<ForgotPasswordResponse> call, Throwable t) {
                    Log.d("TAG", "sendOtp5: "+t);
                    Toast.makeText(ForgotPasswordActivity.this, "Please Try Again Later", Toast.LENGTH_LONG).show();
                    progressDialog.cancel();
                }
            });
        } catch (Exception e) {
            Log.d("TAG", "sendOtp: "+e);
            Toast.makeText(ForgotPasswordActivity.this, "Please Try Again Later", Toast.LENGTH_LONG).show();
            if (progressDialog != null) {
                progressDialog.cancel();
            }
        }
    }

    /*sending otp to the registered mobile number*/
    /*private void sendOtp(String mobileNumber, String IMEI) {
        
    }*/

    /*read phone state permission result*/
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            } else if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(ForgotPasswordActivity.this, Manifest.permission.READ_PHONE_STATE)) {
                    //when click on deny
                } else {
                    //when click on don't show again
                    Intent i = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + BuildConfig.APPLICATION_ID));
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                }
            }
        }
    }
}