/*
 * Name : LoginActivity.java
 * Purpose : This file is used to validate login credentials
 * Created By : Mahesh
 * Developers Involved : Mahesh
 */
package com.example.RentalManagement.Activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;


import com.example.RentalManagement.BuildConfig;
import com.example.RentalManagement.Model.LoginResponse;
import com.example.RentalManagement.R;
import com.example.RentalManagement.Services.ApiClient;
import com.example.RentalManagement.Services.ApiInterface;
import com.example.RentalManagement.Services.NetworkConnection;
import com.example.RentalManagement.utils.Config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    /*declarations*/
    EditText mobileNumber, password;
    Button login, register;
    TextView forgotPassword;
    ApiInterface apiInterface;
    LoginResponse loginResponse;
    NetworkConnection networkConnection;
    ProgressDialog progressDialog;
    private static final int PERMISSION_REQUEST_CODE = 104;
    /*app permissions*/
    String[] appPermissions = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.READ_PHONE_STATE
    };

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        statusBarColor().setStatusBarColor(this.getResources().getColor(R.color.white));
        /*initilizations*/
        mobileNumber = findViewById(R.id.mobileNumber);
        mobileNumber.setInputType(InputType.TYPE_CLASS_NUMBER);
        password = findViewById(R.id.password);
        password.setTransformationMethod(new PasswordTransformationMethod());
        login = findViewById(R.id.login);
        register = findViewById(R.id.register);
        forgotPassword = findViewById(R.id.forgot_password);
        CheckPermissions();
        login.setOnClickListener(this);
        register.setOnClickListener(this);
        forgotPassword.setOnClickListener(this);
        networkConnection = new NetworkConnection(this);
        progressDialog = new ProgressDialog(this, R.style.progressDialogStyle);
    }

    /*onclick for widgets*/
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login:
                String getMobileNumber = mobileNumber.getText().toString();
                String getPassword = password.getText().toString();
                if (mobileNumber.getText().length() == 0) {
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
                    if (getMobileNumber.length() == 10) {
                        if (networkConnection.isConnectingToInternet()) {
                            try {
                                login(getMobileNumber, getPassword);
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
            case R.id.register:
                Intent i = new Intent(this, RegisterActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                break;
            case R.id.forgot_password:
                i = new Intent(this, ForgotPasswordActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                break;
        }
    }

    /*validating mobilenumber and password*/
    private void login(final String getMobileNumber, String getPassword) {
        Config.saveLoginStatus(getApplicationContext(), "1");
        Config.saveLoginNumber(getApplicationContext(), getMobileNumber);
        Intent i = new Intent(getApplicationContext(), SelectCategory.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
        progressDialog.setMessage("Please wait ...");
        progressDialog.show();
        apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);
        Call<LoginResponse> call = apiInterface.getLoginStatus(new LoginResponse(getMobileNumber, getPassword));
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                loginResponse = response.body();
                try {
                    if (loginResponse.getStatus().equals("OK")) {
                        progressDialog.cancel();
                        Config.saveLoginStatus(getApplicationContext(), "1");
                        Config.saveLoginNumber(getApplicationContext(), getMobileNumber);
                        Intent i = new Intent(getApplicationContext(), SelectCategory.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                    }
                } catch (Exception e) {
                    progressDialog.cancel();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                progressDialog.cancel();
            }
        });
    }

    /*checking permissions*/
    public boolean CheckPermissions() {
        List<String> permissionsNeeded = new ArrayList<>();
        for (String permission : appPermissions) {
            if (ActivityCompat.checkSelfPermission(LoginActivity.this, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                permissionsNeeded.add(permission);
            }
        }
        if (!permissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this,
                    permissionsNeeded.toArray(new String[permissionsNeeded.size()]),
                    PERMISSION_REQUEST_CODE);
            return false;
        }
        return true;
        /*if (ActivityCompat.checkSelfPermission(UploadPropertyImages.this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(UploadPropertyImages.this, new String[]
                    {Manifest.permission.CAMERA}, 1);
        } else if (ActivityCompat.checkSelfPermission(UploadPropertyImages.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(UploadPropertyImages.this, new String[]
                    {Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }*/
    }

    /*permission result for camera starts here*/
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            HashMap<String, Integer> permissionResults = new HashMap<>();
            int deniedCount = 0;
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    permissionResults.put(permissions[i], grantResults[i]);
                    deniedCount++;
                }
            }
            if (deniedCount > 0) {
                for (Map.Entry<String, Integer> entry : permissionResults.entrySet()) {
                    String permissionName = entry.getKey();
                    int permissionResult = entry.getValue();
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this, permissionName)) {
                        CheckPermissions();
                    } else {
                        Intent i = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + BuildConfig.APPLICATION_ID));
                        startActivity(i);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
                        startActivity(i);
                    }
                }
            }
        }
       /* if (grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            } else if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(UploadPropertyImages.this,
                        Manifest.permission.CAMERA)) {
                    //when click on deny
                } else {  //when click on don't show again
                    Intent i = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + BuildConfig.APPLICATION_ID));
                    startActivity(i);
                }
            }
        }*/
    }

    /*to change the statubar color*/
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public Window statusBarColor() {
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        return window;
    }
}