package com.example.RentalManagement.Dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;

import com.example.RentalManagement.R;

import java.util.ArrayList;
import java.util.List;

public class Features extends DialogFragment {
    private List<String> selectedItems;
    FeaturesListener featuresListener;

    public interface FeaturesListener {
        void getFeatures(String features);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        selectedItems = new ArrayList<>();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),R.style.MyAlertDialogTheme);
        builder.setTitle("Select Features");
        builder.setMultiChoiceItems(R.array.features, null,
                new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i, boolean isChecked) {
                String[] features = getActivity().getResources().getStringArray(R.array.features);
                if(isChecked){
                    selectedItems.add(features[i]);
                }else if(selectedItems.contains(features[i])){
                    selectedItems.remove(features[i]);
                }
            }
        });
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String final_selection = "";
                for (String Item : selectedItems){
                    final_selection =final_selection+","+Item;
                }
                featuresListener.getFeatures(final_selection);
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
            featuresListener = (FeaturesListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + "must override FeaturesListener..");
        }
    }
}
