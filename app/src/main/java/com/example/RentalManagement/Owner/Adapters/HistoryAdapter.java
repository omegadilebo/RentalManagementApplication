package com.example.RentalManagement.Owner.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.RentalManagement.Owner.AddProperty;
import com.example.RentalManagement.Owner.Model.HistoryResponse;
import com.example.RentalManagement.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.MyViewHolder>{
    List<HistoryResponse> data;
    Context context;
    int[] images;
    public HistoryAdapter(List<HistoryResponse> data,Context context) {
     this.data = data;
     this.context = context;
    }
    public HistoryAdapter(int[] images, Context context) {
        this.images = images;
        this.context = context;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.adapter_owner_history,parent,false);
        return new MyViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        int image_id = images[position];
        Picasso.with(context).load(image_id).into(holder.propertyImage);

        /*holder.address.setText(data.get(position).getAddress());
        holder.status.setText(data.get(position).getImagePath());
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, SelectCategory.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                context.startActivity(i);
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return images.length;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
       ImageView propertyImage;
       TextView address,edit,directions;
       Context context;

        public MyViewHolder(@NonNull View view, Context context) {
            super(view);
            this.context = context;
            propertyImage = view.findViewById(R.id.propertyImage);
            address = view.findViewById(R.id.address);
            edit = view.findViewById(R.id.edit);
            edit.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Intent i = new Intent(context, AddProperty.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.putExtra("buttonName","edit");
            i.putExtra("id",9);
            context.startActivity(i);
        }
    }
}
