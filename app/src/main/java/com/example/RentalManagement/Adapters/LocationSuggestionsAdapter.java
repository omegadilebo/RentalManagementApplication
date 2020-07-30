package com.example.RentalManagement.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.RentalManagement.Owner.AddProperty;
import com.example.RentalManagement.R;
import com.example.RentalManagement.Tenant.SearchProperty;

import java.util.ArrayList;

public class LocationSuggestionsAdapter extends
        RecyclerView.Adapter<LocationSuggestionsAdapter.EMSHolder> {
    //decalarations
    int FADE_DURATION = 100;
    ArrayList<String> data;
    ArrayList<String> data1;
    Context context;
    AddProperty addProperty;
    SearchProperty searchProperty;
    String classname;
    public LocationSuggestionsAdapter(ArrayList<String> data, ArrayList<String> data1, AddProperty addProperty,
                                      Context applicationContext, String classname) {
        this.context=applicationContext;        //initializing variables
        this.data = data;
        this.data1 = data1;
        this.addProperty=addProperty;
        this.classname = classname;
    }
    public LocationSuggestionsAdapter(ArrayList<String> data, ArrayList<String> data1,
                                      SearchProperty searchProperty, Context applicationContext, String classname) {
        this.context=applicationContext;        //initializing variables
        this.data = data;
        this.data1 = data1;
        this.searchProperty=searchProperty;
        this.classname = classname;
    }
    @Override
    public void onBindViewHolder(@NonNull final LocationSuggestionsAdapter.EMSHolder holder, final int position) {
        setScaleAnimation(holder.itemView);
        //set the data to views
        holder.first.setText(data.get(position));
        holder.second.setText(data1.get(position));
        //onclick for layout
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context = holder.layout1.getContext();
                String selectedLocation=data.get(position).concat(", "+data1.get(position));
                if(classname.equalsIgnoreCase("searchProperty")){
                    searchProperty.getLocation(selectedLocation);
                }else if(classname.equalsIgnoreCase("addProperty")){
                    addProperty.getLocation(selectedLocation);
                }
            }
        });
    }

    private void setScaleAnimation(View view){      //to set animation
        ScaleAnimation anim = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setDuration(FADE_DURATION);
        view.startAnimation(anim);
    }

    public static class EMSHolder extends RecyclerView.ViewHolder
    {   //decalaraions
        TextView first, second;
        LinearLayout linearLayout, layout1;

        public EMSHolder(View v)
        {
            super(v);
            first = (TextView)v.findViewById(R.id.first);
            second = (TextView)v.findViewById(R.id.second);
            linearLayout = (LinearLayout)v.findViewById(R.id.linearLayout);
            layout1 = (LinearLayout)v.findViewById(R.id.layout1);
        }
    }

    public int getItemCount() {
        return  data.size();
    }

    public Object getItem(int i) {
        return i;
    }

    @NonNull
    @Override   //xml file initialization
    public LocationSuggestionsAdapter.EMSHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_location_suggestions_adapter,
                parent, false);
        return new LocationSuggestionsAdapter.EMSHolder(itemView);
    }

    public long getItemId(int i) {
        return i;
    }
}
