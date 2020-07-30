package com.example.RentalManagement.Dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import com.example.RentalManagement.R;

public class TenantType extends DialogFragment {
    TenantTypeListener tenantTypeListener;

    public interface TenantTypeListener {
        void getTenantType(String tenatType);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        final String[] tenant = this.getResources().getStringArray(R.array.tenantType);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Select Tenant Type");
        builder.setCancelable(false);
        builder.setItems(tenant, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                tenantTypeListener.getTenantType(tenant[i]);

            }
        });
        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity activity = (Activity) context;
        try {
            tenantTypeListener = (TenantTypeListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + "must override TenantTypeListener..");
        }
    }
}
