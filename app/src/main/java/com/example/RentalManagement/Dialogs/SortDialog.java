package com.example.RentalManagement.Dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import com.example.RentalManagement.R;
import java.util.List;

public class SortDialog extends DialogFragment {
    public  String selectedItems;
    SortDialogListener sortDialogListener;

    public interface SortDialogListener {
        void getSortDialog(String s);
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final String[] sorts = getActivity().getResources().getStringArray(R.array.sort);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.MyAlertDialogTheme);
        builder.setTitle("Sort By");
        builder.setSingleChoiceItems(R.array.sort, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        selectedItems = sorts[i];
                    }
                });
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        sortDialogListener.getSortDialog(selectedItems);
                    }
                });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        return builder.create();
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity activity = (Activity) context;
        try {
            sortDialogListener = (SortDialogListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + "must override SortDialogListener..");
        }
    }
}
