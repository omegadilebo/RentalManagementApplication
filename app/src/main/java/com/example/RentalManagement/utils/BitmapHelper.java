package com.example.RentalManagement.utils;

import android.graphics.Bitmap;

public class BitmapHelper {
    public Bitmap bitmap1 = null;

    public Bitmap getBitmap2() {
        return bitmap2;
    }

    public void setBitmap2(Bitmap bitmap2) {
        this.bitmap2 = bitmap2;
    }

    public Bitmap getBitmap3() {
        return bitmap3;
    }

    public void setBitmap3(Bitmap bitmap3) {
        this.bitmap3 = bitmap3;
    }

    public Bitmap bitmap2 = null;
    public Bitmap bitmap3 = null;
    public static final BitmapHelper instance = new BitmapHelper();

    public BitmapHelper() {
    }

    public Bitmap getBitmap1() {
        return bitmap1;
    }

    public void setBitmap1(Bitmap bitmap1) {
        this.bitmap1 = bitmap1;
    }


    public static BitmapHelper getInstance() {
        return instance;
    }


}
