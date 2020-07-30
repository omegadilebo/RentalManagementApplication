/*
 * Name : RegisterActivity.java
 * Purpose : This file is used for user registration
 * Created By : Mahesh
 * Developers Involved : Mahesh
 */
package com.example.RentalManagement.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
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

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


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
    String IMEI;

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

        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

        Log.d("TAG", "onCreate: "+telephonyManager.getDeviceId());
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
                } else if (getOtp.length() < 3) {
                    otp.setError("OTP should be 4 digits");
                } else {
                    if (networkConnection.isConnectingToInternet()) {
                        try {
                            submitOtp(getOtp);
                        } catch (Exception e) {
                            if (progressDialog != null) {
                                progressDialog.cancel();
                            }
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
        } else if (!getMobileNumber.matches("[0-9.?]*")) {
            mobileNumber.setError("Only numbers are allowed");
        } else if (getMobileNumber.startsWith("0") ||
                getMobileNumber.startsWith("1") ||
                getMobileNumber.startsWith("2") ||
                getMobileNumber.startsWith("3") ||
                getMobileNumber.startsWith("4") ||
                getMobileNumber.startsWith("5")) {
            mobileNumber.setError("Enter a valid number");
        } else if (getPassword.length() == 0) {
            password.setError("Enter password");
        } else if (getPassword.length() < 8) {
            password.setError("minimum 8 charcters");
        } else {
            getGender = radioButton.getText().toString();
            if (getMobileNumber.length() == 10) {
                if (networkConnection.isConnectingToInternet()) {
                    try {
                        sendOtp(getName, getAge, getOccupation, getGender, getMobileNumber, getPassword);
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
    }

    /*send otp for registration*/
    private void sendOtp(String getName, String getAge, String getOccupation, String getGender, String getMobileNumber, String getPassword) {
       /* for (int i = 0; i < registerLayout.getChildCount(); i++) {
            View v = registerLayout.getChildAt(i);
            v.setEnabled(false);
        }
        sendOtp.setVisibility(View.GONE);
        otpLayout.setVisibility(View.VISIBLE);*/
      progressDialog.setMessage("Please wait ...");
      progressDialog.setCancelable(false);
      progressDialog.show();
      apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);
      Call<RegistrationResponse> call = apiInterface.SendOtpStatus(new RegistrationResponse(getName, getAge, getOccupation,
              getGender, getMobileNumber, getPassword, IMEI));
      call.enqueue(new Callback<RegistrationResponse>() {
          @Override
          public void onResponse(Call<RegistrationResponse> call, Response<RegistrationResponse> response) {
              registrationResponse = response.body();

              try {
                  Log.d("TAG", "onResponse: " + registrationResponse);
                  progressDialog.cancel();
                   /* if (registrationResponse.getStatus().equals("OK")) {
                        progressDialog.cancel();
                        for (int i = 0; i < registerLayout.getChildCount(); i++) {
                            View v = registerLayout.getChildAt(i);
                            v.setEnabled(false);
                        }
                        sendOtp.setVisibility(View.GONE);
                        otpLayout.setVisibility(View.VISIBLE);
                    }*/
              } catch (Exception e) {
                  Log.d("TAG", "onResponse:1 " + e);
                  progressDialog.cancel();
              }
          }

          @Override
          public void onFailure(Call<RegistrationResponse> call, Throwable t) {
              Log.d("TAG", "onResponse:2 " + t);
              progressDialog.cancel();
          }
      });
      ;
    }

    /*submit otp for registration*/
    private void submitOtp(String getOtp) {
        /*Config.saveLoginStatus(getApplicationContext(), "1");
        Intent i = new Intent(this, SelectCategory.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);*/
        progressDialog.setMessage("Please wait ...");
        progressDialog.show();
        apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);
        Call<RegistrationResponse> call = apiInterface.ValidateOtp(new RegistrationResponse(getOtp));
        call.enqueue(new Callback<RegistrationResponse>() {
            @Override
            public void onResponse(Call<RegistrationResponse> call, Response<RegistrationResponse> response) {
                registrationResponse = response.body();
                try {
                    if (registrationResponse.getStatus().equals("OK")) {
                        progressDialog.cancel();
                        Config.saveLoginStatus(getApplicationContext(), "1");
                        Intent i = new Intent(getApplicationContext(), SelectCategory.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                    }
                } catch (Exception e) {
                    progressDialog.cancel();
                }
            }

            @Override
            public void onFailure(Call<RegistrationResponse> call, Throwable t) {
                progressDialog.cancel();
            }
        });
        ;
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