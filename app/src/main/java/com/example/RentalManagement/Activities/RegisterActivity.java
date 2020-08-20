/*
 * Name : RegisterActivity.java
 * Purpose : This file is used for user registration
 * Created By : Mahesh
 * Developers Involved : Mahesh
 */
package com.example.RentalManagement.Activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;


import com.example.RentalManagement.BuildConfig;
import com.example.RentalManagement.Dialogs.OccupationList;
import com.example.RentalManagement.Model.RegistrationResponse;
import com.example.RentalManagement.R;
import com.example.RentalManagement.Services.ApiClient;
import com.example.RentalManagement.Services.ApiInterface;
import com.example.RentalManagement.Services.NetworkConnection;
import com.example.RentalManagement.utils.Config;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RegisterActivity extends AppCompatActivity implements View.OnClickListener,
        OccupationList.OccupationListener {
    /*declarations*/
    LinearLayout registerLayout, otpLayout;
    Toolbar toolbar;
    EditText fullName, age, mobileNumber, password, otp;
    TextView occupation;
    RadioGroup radioGroup;
    RadioButton radioButton;
    Button sendOtp, submit;
    Typeface typeface;//to set font
    String getName, getAge, getGender, getOccupation, getMobileNumber, getPassword, getOtp;
    ApiInterface apiInterface;
    ProgressDialog progressDialog;
    NetworkConnection networkConnection;
    RegistrationResponse registrationResponse;
    TelephonyManager telephonyManager;
    String OTPNumber, IMEI;
    public static int REQUEST_CODE = 200;
    String UserID;
    int i, j, k, r;

    @SuppressLint("HardwareIds")
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        typeface = Typeface.createFromAsset(getAssets(), "font/roboto_light.ttf");
        statusBarColor().setStatusBarColor(this.getResources().getColor(R.color.design_default_color_primary));
        /*initializations*/
        registerLayout = findViewById(R.id.registerLayout);
        otpLayout = findViewById(R.id.otpLayout);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Registration");
        toolbar.setNavigationIcon(R.drawable.arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });
        fullName = findViewById(R.id.fullName);
        age = findViewById(R.id.age);
        age.setInputType(InputType.TYPE_CLASS_NUMBER);
        radioGroup = findViewById(R.id.radioGroup);
        radioGroup.clearCheck();
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                radioButton = (RadioButton) radioGroup.findViewById(checkedId);
            }
        });
        occupation = findViewById(R.id.occupation);
        mobileNumber = findViewById(R.id.mobileNumber);
        mobileNumber.setTypeface(typeface);
        mobileNumber.setInputType(InputType.TYPE_CLASS_NUMBER);
        password = findViewById(R.id.password);
        password.setTypeface(typeface);
        password.setTransformationMethod(new PasswordTransformationMethod());
        sendOtp = findViewById(R.id.sendOtp);
        otp = findViewById(R.id.otp);
        otp.setTypeface(typeface);
        otp.setInputType(InputType.TYPE_CLASS_NUMBER);
        submit = findViewById(R.id.submit);
        occupation.setOnClickListener(this);
        sendOtp.setOnClickListener(this);
        submit.setOnClickListener(this);

        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        networkConnection = new NetworkConnection(this);
        progressDialog = new ProgressDialog(this, R.style.progressDialogStyle);
    }

    /*on click for widgets*/
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.occupation:
                new OccupationList().show(getFragmentManager(), "occupationlist");
                break;
            case R.id.sendOtp:
                checkValidations();
                break;
            case R.id.submit:
                getOtp = otp.getText().toString().trim();
                if (getOtp.length() == 0) {
                    otp.setError("Please Enter OTP");
                } else if (getOtp.length() < 6) {
                    otp.setError("OTP should be 6 digits");
                } else {
                    if (networkConnection.isConnectingToInternet()) {
                            if (getOtp.equals(OTPNumber)) {
                                submitOtp(getName, getAge, getGender, getOccupation, getMobileNumber, getPassword, "0", IMEI);
                            } else {
                                Toast.makeText(this, "Enter Correct OTP", Toast.LENGTH_SHORT).show();
                            }
                    } else {
                        Toast.makeText(this, "Please check your internet connection", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }

    /*checking validations*/
    private void checkValidations() {
        getName = fullName.getText().toString();
        getAge = age.getText().toString();
        getOccupation = occupation.getText().toString();
        getMobileNumber = mobileNumber.getText().toString().trim();
        getPassword = password.getText().toString().trim();
        Log.d("TAG", "onClick: " + getOccupation);
        if (getName.length() == 0) {
            fullName.setError("Enter Name");
        } else if (getAge.length() == 0) {
            age.setError("Enter age");
        } else if (getAge.length() == 1) {
            age.setError("Age should be 2 digits");
        } else if (radioGroup.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this, "Please Select Gender", Toast.LENGTH_LONG).show();
        } else if (getOccupation.equalsIgnoreCase("")) {
            Toast.makeText(this, "Select Occupation", Toast.LENGTH_SHORT).show();
        } else if (mobileNumber.getText().length() == 0) {
            mobileNumber.setError("Enter your number");
        } else if (getMobileNumber.length() < 10) {
            mobileNumber.setError("Enter 10 digit Mobile Number");
        } else if (getPassword.length() == 0) {
            password.setError("Enter password");
        } else if (getPassword.length() < 8) {
            password.setError("minimum 8 charcters");
        } else {
            getGender = radioButton.getText().toString();
            if (getMobileNumber.length() == 10 & getMobileNumber.matches("^[6-9]\\d{9}$")) {
                if (networkConnection.isConnectingToInternet()) {
                        if (ActivityCompat.checkSelfPermission(RegisterActivity.this, Manifest.permission.READ_PHONE_STATE)
                                != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(RegisterActivity.this, new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_CODE);
                        } else {
                            try {
                                IMEI = telephonyManager.getDeviceId();
                            } catch (Exception e) {
                                Log.d("TAG", "onResponse:5 " + e);
                            }
                            sendOtp(getMobileNumber, IMEI);
                        }
                } else {
                    Toast.makeText(this, "Please check your internet connection", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    /*send otp for registration*/
    private void sendOtp(String getMobileNumber, String IMEI) {
        try {
            progressDialog.setMessage("Please wait ...");
            progressDialog.setCancelable(false);
            progressDialog.show();
            apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);
            Call<RegistrationResponse> call = apiInterface.SendOtpStatus(new RegistrationResponse(getMobileNumber, IMEI));
            call.enqueue(new Callback<RegistrationResponse>() {
                @Override
                public void onResponse(Call<RegistrationResponse> call, Response<RegistrationResponse> response) {
                    registrationResponse = response.body();
                    try {
                        Log.d("TAG", "onResponse2: " + registrationResponse.getStatus()+"\n"+
                                registrationResponse.getOTPNumber());
                        if (registrationResponse.getStatus().equals("0")) {//new user
                            for (int i = 0; i < registerLayout.getChildCount(); i++) {
                                View v = registerLayout.getChildAt(i);
                                v.setEnabled(false);
                            }
                            sendOtp.setVisibility(View.GONE);
                            otpLayout.setVisibility(View.VISIBLE);
                            OTPNumber = registrationResponse.getOTPNumber();
                            Log.d("TAG", "onResponse3: " + OTPNumber);
                            Toast.makeText(RegisterActivity.this, OTPNumber, Toast.LENGTH_SHORT).show();
                            progressDialog.cancel();
                        } else if(registrationResponse.getStatus().equals("1")){//user already existed
                            progressDialog.cancel();
                            Toast.makeText(RegisterActivity.this, "The Mobile Number is Already Registered, Try with new Mobile Number", Toast.LENGTH_LONG).show();
                        } else {
                            Log.d("TAG", "onResponse:1 ");
                            progressDialog.cancel();
                            Toast.makeText(RegisterActivity.this, "Please Try Again Later", Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception e) {
                        Log.d("TAG", "onResponse:2 " + e);
                        Toast.makeText(RegisterActivity.this, "Please Try Again Later", Toast.LENGTH_LONG).show();
                        progressDialog.cancel();
                    }
                }

                @Override
                public void onFailure(Call<RegistrationResponse> call, Throwable t) {
                    Log.d("TAG", "onResponse:3 " + t);
                    Toast.makeText(RegisterActivity.this, "Please Try Again Later", Toast.LENGTH_LONG).show();
                    progressDialog.cancel();
                }
            });
        } catch (Exception e) {
            if(progressDialog!=null){
                progressDialog.cancel();
            }
            Toast.makeText(RegisterActivity.this, "Please Try Again Later", Toast.LENGTH_LONG).show();
            Log.d("TAG", "onResponse:4 : "+e);
        }
    }

    /*submit otp for registration*/
    private void submitOtp(String getName, String getAge, String getGender,
                           String getOccupation, String getMobileNumber, String getPassword,
                           String i, String imei) {
        try {
            progressDialog.setMessage("Please wait ...");
            progressDialog.setCancelable(false);
            progressDialog.show();
            apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);
            Call<RegistrationResponse> call = apiInterface.ValidateOtp(new RegistrationResponse(
                    getName, getAge, getGender, getOccupation, getMobileNumber, getPassword, i, imei));
            call.enqueue(new Callback<RegistrationResponse>() {
                @Override
                public void onResponse(Call<RegistrationResponse> call, Response<RegistrationResponse> response) {
                    registrationResponse = response.body();
                    try {
                        if (registrationResponse.getStatus().equalsIgnoreCase("SUCCESS")) {
                            UserID = registrationResponse.getUserID();
                            Toast.makeText(RegisterActivity.this, UserID, Toast.LENGTH_LONG).show();
                            Config.saveUserId(getApplicationContext(), UserID);
                            Config.saveLoginStatus(getApplicationContext(), "1");
                            Intent i = new Intent(getApplicationContext(), SelectCategory.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(i);
                            progressDialog.cancel();
                        } else {
                            Toast.makeText(RegisterActivity.this, "please try again later", Toast.LENGTH_SHORT).show();
                            progressDialog.cancel();
                        }
                    } catch (Exception e) {
                        Toast.makeText(RegisterActivity.this, "please try again later", Toast.LENGTH_SHORT).show();
                        progressDialog.cancel();
                    }
                }

                @Override
                public void onFailure(Call<RegistrationResponse> call, Throwable t) {
                    Log.d("TAG", "onFailure: "+t);
                    progressDialog.cancel();
                }
            });
        }catch (Exception e){
            Log.d("TAG", "submitOtp: "+e);
        }
    }

    /*read phone state permission result*/
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            } else if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(RegisterActivity.this, Manifest.permission.READ_PHONE_STATE)) {
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

    /*changing status bar color*/
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public Window statusBarColor() {
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        return window;
    }

    /*getting occupation details from Dialogs/OccupationList*/
    @Override
    public void getOccupationList(String data) {
        occupation.setText(data);
    }
}