package com.example.RentalManagement.Dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

import com.example.RentalManagement.R;

public class CommercialPropertyType extends DialogFragment {

    PropertyTypeListener propertyTypeListener;

    public interface PropertyTypeListener {
        void getPropertyType(String property);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        final String[] property = this.getResources().getStringArray(R.array.commercialPropertyType);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.MyAlertDialogTheme);
        builder.setTitle("Select Property Type");
        builder.setCancelable(false);
        builder.setItems(property, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                propertyTypeListener.getPropertyType(property[i]);
            }
        });
        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity activity = (Activity) context;
        try {
            propertyTypeListener = (PropertyTypeListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + "must override PropertyTypeListener..");
        }
    }

}
