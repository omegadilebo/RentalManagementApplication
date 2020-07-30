package com.example.RentalManagement.Dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import com.example.RentalManagement.R;

public class Parking extends DialogFragment {
    ParkingListener parkingListener;

    public interface ParkingListener {
        void getParkingDetails(String slot);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        final String[] parking = this.getResources().getStringArray(R.array.parking);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Parking Type");
        builder.setCancelable(false);
        builder.setItems(parking, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                parkingListener.getParkingDetails(parking[i]);
            }
        });
        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity activity = (Activity) context;
        try {
            parkingListener = (ParkingListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + "must override ParkingListener..");
        }
    }
}
