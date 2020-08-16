package com.example.RentalManagement.Owner.Residential.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.RentalManagement.Owner.Residential.Activities.AddProperty;
import com.example.RentalManagement.Owner.Residential.Models.HistoryResponse;
import com.example.RentalManagement.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.MyViewHolder> {

    List<HistoryResponse.data> data;
    Context context;
    List<Integer> images;
    Dialog dialog;
    OnRemovePropertyListener listener;

    public HistoryAdapter(List<HistoryResponse.data> data, Context context, OnRemovePropertyListener listener) {
        this.data = data;
        this.context = context;
        this.listener = listener;

    }

  /*  public HistoryAdapter(List<Integer> images, Context context, OnRemovePropertyListener listener) {
        this.images = images;
        this.context = context;
        this.listener = listener;
    }*/
 /*   public HistoryAdapter(List<Integer> images, Context context) {
        this.images = images;
        this.context = context;
    }*/

    public void removeItem(int adapterPosition) {
        data.remove(adapterPosition);
        notifyItemRemoved(adapterPosition);
        notifyItemRangeChanged(adapterPosition, data.size());
    }

    public interface OnRemovePropertyListener {
        void onRemoveProperty(View v, int adapterPosition, int propertyId);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.adapter_owner_history, parent, false);
        return new MyViewHolder(view, context, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String image_id = data.get(position).getImage2();
        Picasso.with(context).load(image_id).into(holder.propertyImage);
        String apartmentName = data.get(position).getApartmentName();
        String roomNo = data.get(position).getRoomNo();
        String bhk = data.get(position).getBHK();
        String sqft = data.get(position).getSft();
        String rent = data.get(position).getRent();
        String add = data.get(position).getAddress();
        holder.address.setText(apartmentName+"\n"+
                "Room No:"+roomNo+"Rent:"+rent+"BHK:"+bhk+"Sq.ft:"+sqft+"\n"+
                add
        );

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView propertyImage;
        TextView address, edit, remove;
        Context context;
        OnRemovePropertyListener listener;


        public MyViewHolder(@NonNull View view, Context context, OnRemovePropertyListener listener) {
            super(view);
            this.context = context;
            this.listener = listener;
            propertyImage = view.findViewById(R.id.propertyImage);
            address = view.findViewById(R.id.address);
            edit = view.findViewById(R.id.edit);
            remove = view.findViewById(R.id.remove);

            edit.setOnClickListener(this);
            remove.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.edit:
                    Intent i = new Intent(context, AddProperty.class);
                    data.get(getAdapterPosition()).getPropertyId();
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    i.putExtra("buttonName", "edit");
                    i.putExtra("id", data.get(getAdapterPosition()).getPropertyId());
                    context.startActivity(i);
                    break;
                case R.id.remove:
                    try {
                        listener.onRemoveProperty(view, getAdapterPosition(), data.get(getAdapterPosition()).getPropertyId());
                    } catch (Exception e) {
                        Log.d("TAG", "onClick: " + e);
                    }

                    break;
            }
        }
    }
}
