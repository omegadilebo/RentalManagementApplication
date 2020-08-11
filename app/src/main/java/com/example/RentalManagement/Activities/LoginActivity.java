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
import android.util.Log;
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
import com.example.RentalManagement.Owner.AddProperty;
import com.example.RentalManagement.R;
import com.example.RentalManagement.Services.ApiClient;
import com.example.RentalManagement.Services.ApiInterface;
import com.example.RentalManagement.Services.NetworkConnection;
import com.example.RentalManagement.utils.Config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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
    String[] appPermissions = new String[]{
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
                String getPassword = password.getText().toString().trim();
                if (mobileNumber.getText().length() == 0) {
                    mobileNumber.setError("Enter your number");
                } else if (getMobileNumber.length() < 10) {
                    mobileNumber.setError("Enter 10 digit Mobile Number");
                } else if (getPassword.length() == 0) {
                    password.setError("Enter password");
                } else if (getPassword.length() < 8) {
                    password.setError("minimum 8 charcters");
                } else if (getMobileNumber.length() == 10 && getMobileNumber.matches("^[6-9]\\d{9}$")) {
                    if (networkConnection.isConnectingToInternet()) {
                        login(getMobileNumber, getPassword);
                    } else {
                        Toast.makeText(this, "Please check your internet connection", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    mobileNumber.setError("Enter Correct Mobilenumber");
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
        try {
            progressDialog.setMessage("Please wait ...");
            progressDialog.setCancelable(false);
            progressDialog.show();

            apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);
            Log.d("TAG", "login: " + getMobileNumber + "\n" + getPassword);
            Call<LoginResponse> call = apiInterface.getLoginStatus(new LoginResponse(getMobileNumber, getPassword));
            call.enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    loginResponse = response.body();
                    Log.d("TAG", "login: " + loginResponse.getStatus()+"\n"+loginResponse.getUserID());
                    try {
                        if (loginResponse.getStatus().equals("1")) {
                            progressDialog.cancel();
                            Config.saveLoginStatus(getApplicationContext(), "1");
                            Config.saveUserId(getApplicationContext(), loginResponse.getUserID());
                            Intent i = new Intent(getApplicationContext(), SelectCategory.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(i);
                        } else if (loginResponse.getStatus().equals("0")) {
                            Toast.makeText(LoginActivity.this, "Please Enter Correct mobile number and password", Toast.LENGTH_SHORT).show();
                            progressDialog.cancel();
                        } else {
                            progressDialog.cancel();
                            Log.d("TAG", "login:0 " + loginResponse.getStatus());
                        }
                    } catch (Exception e) {
                        Log.d("TAG", "login:1 " + e);
                        progressDialog.cancel();
                    }
                }

                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {
                    progressDialog.cancel();
                    Log.d("TAG", "login:2 " + t);
                }
            });
        } catch (Exception e) {
            Log.d("TAG", "login:3 " + e);
            if (progressDialog != null) {
                progressDialog.cancel();
            }
        }
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
                    Log.d("TAG", "onRequestPermissionsResult: " + permissionName + permissionResult);
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this, permissionName)) {
                        CheckPermissions();
                    } else {
                        Intent i = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + BuildConfig.APPLICATION_ID));
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                    }
                }
            }
        }
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