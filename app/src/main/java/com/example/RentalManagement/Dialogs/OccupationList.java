package com.example.RentalManagement.Dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import com.example.RentalManagement.R;

public class OccupationList extends DialogFragment {
    OccupationListener occupationListener;

    public interface OccupationListener {
        void getOccupationList(String occupation);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        final String[] occupationList = this.getResources().getStringArray(R.array.occupation);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.MyAlertDialogTheme);
        builder.setTitle("Select Occupation");
        builder.setCancelable(false);
        builder.setItems(occupationList, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                occupationListener.getOccupationList(occupationList[i]);
            }
        });
        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity activity = (Activity) context;
        try {
            occupationListener = (OccupationListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + "must override Occupation Listener..");
        }
    }

}
