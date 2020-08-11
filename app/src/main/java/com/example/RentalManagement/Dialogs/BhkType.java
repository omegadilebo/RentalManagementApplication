package com.example.RentalManagement.Dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import com.example.RentalManagement.R;

public class BhkType extends DialogFragment {
    BhkListener bhkListener;

    public interface BhkListener {
        void getBhkType(String BHK);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        final String[] bhk = this.getResources().getStringArray(R.array.selectBHK);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.MyAlertDialogTheme);
        builder.setTitle("Select BHK");
        builder.setItems(bhk, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                bhkListener.getBhkType(bhk[i]);
            }
        });
        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity activity = (Activity) context;
        try {
            bhkListener = (BhkListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + "must override BhkListener..");
        }
    }
}

