package com.example.RentalManagement.Dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

import com.example.RentalManagement.R;

public class PropertyLocated extends DialogFragment {
    PropertyLocatedListener propertyLocatedListener;

    public interface PropertyLocatedListener {
        void getPropertyLocated(String PropertyLocated);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        final String[] propertylocated = this.getResources().getStringArray(R.array.propertyLocated);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.MyAlertDialogTheme);
        builder.setTitle("Property Located:");
        builder.setCancelable(false);
        builder.setItems(propertylocated, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                propertyLocatedListener.getPropertyLocated(propertylocated[i]);
            }
        });
        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity activity = (Activity) context;
        try {
            propertyLocatedListener = (PropertyLocatedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + "must override PropertyLocatedListener..");
        }
    }
}
