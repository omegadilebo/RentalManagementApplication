package com.example.RentalManagement.Dialogs;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.RentalManagement.R;

public class RemoveProperty extends DialogFragment {
    RemovePropertyListener removePropertyListener;

    public interface RemovePropertyListener {
        void getRemovePropertyDialogItem(String value);
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.activity_remove_property, new LinearLayout(getActivity()), false);
        TextView yes = view.findViewById(R.id.yes);
        TextView no = view.findViewById(R.id.no);
        final AlertDialog builder = new AlertDialog.Builder(getActivity()).create();
        builder.setView(view);
        Window window = builder.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removePropertyListener.getRemovePropertyDialogItem("yes");
                builder.cancel();
            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builder.cancel();
            }
        });

        return builder;
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity activity = (Activity) context;
        try {
            removePropertyListener = (RemovePropertyListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + "must override removePropertyListener..");
        }
    }
}
