package com.example.RentalManagement.Tenant.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.RentalManagement.R;
import com.example.RentalManagement.Tenant.PropertyDetails;
import com.squareup.picasso.Picasso;

public class SearchPropertyResultsAdapter extends RecyclerView.Adapter<SearchPropertyResultsAdapter.MyViewHolder> {
    int[] images;
    Context context;

    public SearchPropertyResultsAdapter(int[] images, Context context) {
        this.images = images;
        this.context  = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.activity_searchpropertyresultsadapter,parent,false);
        return new MyViewHolder(v, context);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
      int image_id = images[position];
        Picasso.with(context).load(image_id).into(holder.propertyImage);
    }

    @Override
    public int getItemCount() {
        return images.length;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView propertyImage, directions;
        TextView address;
        Context context;
        public MyViewHolder(@NonNull View view, Context context) {
            super(view);
            this.context = context;
            propertyImage = view.findViewById(R.id.propertyImage);
            directions =  view.findViewById(R.id.directions);
            address = view.findViewById(R.id.address);
            propertyImage.setOnClickListener(this);
            directions.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
        switch (view.getId()){
            case R.id.propertyImage:
                Log.d("TAG", "onClick: "+"Heleelo");
                Intent i = new Intent(context, PropertyDetails.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
                break;
            case R.id.directions:
                Log.d("TAG", "onClick: "+"Hello");
                String uri = "http://maps.google.com/maps?f=d&hl=en&saddr=" + "currentLocationLatitude" + "," + "currentLocationLongitude" + "&daddr=" + "propertyLatitude" + "," + "propertyLongitude";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                if (intent.resolveActivity(context.getPackageManager()) != null) {
                    context.startActivity(intent);
                }
                break;
        }
        }
    }
}
