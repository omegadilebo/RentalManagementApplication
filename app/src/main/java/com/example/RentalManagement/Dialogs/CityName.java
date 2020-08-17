package com.example.RentalManagement.Dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

import com.example.RentalManagement.R;

public class CityName extends DialogFragment {
    CityNameListener cityNameListener;

    public interface CityNameListener {
        void getCityName(String cityName);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        final String[] cityname = this.getResources().getStringArray(R.array.cityname);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.MyAlertDialogTheme);
        builder.setTitle("Property Located:");
        builder.setCancelable(false);
        builder.setItems(cityname, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                cityNameListener.getCityName(cityname[i]);
            }
        });
        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity activity = (Activity) context;
        try {
            cityNameListener = (CityNameListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + "must override CityNameListener..");
        }
    }
}
