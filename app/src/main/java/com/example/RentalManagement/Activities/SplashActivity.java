/*
 * Name : SplashActivity.java
 * Purpose : This file for splash
 * Created By : Mahesh
 * Developers Involved : Mahesh
 */
package com.example.RentalManagement.Activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.RentalManagement.R;
import com.example.RentalManagement.utils.Config;


public class SplashActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        statusBarColor().setStatusBarColor(this.getResources().getColor(R.color.design_default_color_primary));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d("TAG", "run: "+Config.getUserId(getApplicationContext()));
                if (Config.getLoginStatus(getApplicationContext()).equals("1")) {
                   Intent i = new Intent(getApplicationContext(), SelectCategory.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                } else if(Config.getLoginStatus(getApplicationContext()).equals("0")) {
                   Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                }else {
                    Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                }
            }
        }, 2000);
    }
/*changing status bar color*/
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public Window statusBarColor() {
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        return window;

    }
}