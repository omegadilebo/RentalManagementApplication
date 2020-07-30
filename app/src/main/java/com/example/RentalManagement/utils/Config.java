package com.example.RentalManagement.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Config {
    public static void saveLoginStatus(Context c, String status)
    {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(c);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("loginStatus",status);
        editor.commit();
    }

    public static String getLoginStatus(Context c)
    {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(c);
        return sharedPreferences.getString("loginStatus","");
    }

    public static void saveLoginNumber(Context applicationContext, String getMobileNumber) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("loginNumber",getMobileNumber);
        editor.commit();
    }
    public static String getLoginNumber(Context c)
    {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(c);
        return sharedPreferences.getString("loginNumber","");
    }
}
