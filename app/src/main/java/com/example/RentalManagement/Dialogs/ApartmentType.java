package com.example.RentalManagement.Dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import com.example.RentalManagement.R;

public class ApartmentType extends DialogFragment {
    ApartmentListener apartmentListener;

    public interface ApartmentListener {
        void getApartmentType(String apartment);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        final String[] apartment = this.getResources().getStringArray(R.array.apartmentType);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.MyAlertDialogTheme);
        builder.setTitle("Select Apartment Type");
        builder.setCancelable(false);
        builder.setItems(apartment, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                apartmentListener.getApartmentType(apartment[i]);
            }
        });
        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity activity = (Activity) context;
        try {
            apartmentListener = (ApartmentListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + "must override ApartmentListener..");
        }
    }
}
