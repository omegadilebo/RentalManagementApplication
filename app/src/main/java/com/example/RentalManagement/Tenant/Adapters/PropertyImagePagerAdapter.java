package com.example.RentalManagement.Tenant.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.RentalManagement.R;
import com.example.RentalManagement.Tenant.PropertyImage;


public class PropertyImagePagerAdapter extends PagerAdapter {
    Context context;
    int images[];
    LayoutInflater layoutInflater;
    public PropertyImagePagerAdapter(Context context, int images[]) {
        this.context = context;
        this.images = images;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (LinearLayout)object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        View itemView = layoutInflater.inflate(R.layout.imges_items, container, false);

        ImageView imageView = (ImageView) itemView.findViewById(R.id.Imageview);
        imageView.setImageResource(images[position]);

        container.addView(itemView);

        //listening to image click
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, PropertyImage.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Bundle bundle = new Bundle();
                bundle.putInt("imageData",images[position]);
                i.putExtra("bundle", bundle);
                context.startActivity(i);
               // Toast.makeText(context, "you clicked image " + (position + 1), Toast.LENGTH_LONG).show();
            }
        });

        return itemView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        //super.destroyItem(container, position, object);
        container.removeView((LinearLayout) object);
    }
}
