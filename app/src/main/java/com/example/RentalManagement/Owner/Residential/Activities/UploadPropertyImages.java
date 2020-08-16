/*
 * Name : UploadPropertyImages.java
 * Purpose : This file used to upload property images
 * Created By : Mahesh
 * Developers Involved : Mahesh
 */
package com.example.RentalManagement.Owner.Residential.Activities;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import com.example.RentalManagement.Dialogs.LogOut;
import com.example.RentalManagement.Owner.Residential.Models.AddPropertyResponse;
import com.example.RentalManagement.R;
import com.example.RentalManagement.Services.ApiClient;
import com.example.RentalManagement.Services.ApiInterface;
import com.example.RentalManagement.Services.NetworkConnection;
import com.example.RentalManagement.utils.BitmapHelper;
import com.example.RentalManagement.utils.Config;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UploadPropertyImages extends AppCompatActivity implements View.OnClickListener {
    /*declarations*/
    Toolbar toolbar;
    TextView capture1, capture2, capture3;
    Bitmap bitmap1, bitmap2, bitmap3;
    ImageView image1, clear1, image2, clear2, image3, clear3;
    RelativeLayout imagerelativeLayout1, imagerelativeLayout2, imagerelativeLayout3;
    static final int REQUEST_IMAGE_CAPTURE1 = 101, REQUEST_IMAGE_CAPTURE2 = 102, REQUEST_IMAGE_CAPTURE3 = 103;
    static final int GALLERY1 = 1, GALLERY2 = 2, GALLERY3 = 3;
    Button upload;
    ApiInterface apiInterface;
    AddPropertyResponse addPropertyResponse;
    private static final int PERMISSION_REQUEST_CODE = 104;
    String[] appPermissions = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };
    Uri imageUri1, imageUri2, imageUri3;
    String filePath, buttonName;

    double latitude, longitude;
    String propertyType, apartmentType, apartmentName, bhk, extent, rent, roomNo, floorNo,
            tenantType, foodType, water, parking, lift,
            contactTime, address, floors, deposit, lease;
    String[] specialFeatures;
    String locality, subLocality;
    Integer propertyId;
    ProgressDialog progressDialog;
    NetworkConnection networkConnection;
    Bundle bundle;
    //Bitmap bitmap1, bitmap2, bitmap3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_property_images);
        statusBarColor().setStatusBarColor(this.getResources().getColor(R.color.design_default_color_primary));
        /*initializations*/
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Upload Images");
        toolbar.setNavigationIcon(R.drawable.arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        /*getting property details*/
        Intent i = getIntent();
        bundle = i.getBundleExtra("bundle");
        buttonName = bundle.getString("buttonName");
        latitude = bundle.getDouble("latitude");
        longitude = bundle.getDouble("longitude");
        locality = bundle.getString("locality");
        subLocality = bundle.getString("subLocality");
        extent = bundle.getString("extent");
        floorNo = bundle.getString("floorNo");
        specialFeatures = bundle.getStringArray("specialFeatures");
        rent = bundle.getString("rent");
        contactTime = bundle.getString("contactTime");
        address = bundle.getString("address");
        if (buttonName.equalsIgnoreCase("addProperty") |
                buttonName.equalsIgnoreCase("edit")) {

            apartmentType = bundle.getString("apartmentType");
            apartmentName = bundle.getString("apartmentName");
            bhk = bundle.getString("bhk");
            roomNo = bundle.getString("roomNo");
            tenantType = bundle.getString("tenantType");
            foodType = bundle.getString("foodType");
            water = bundle.getString("water");
            parking = bundle.getString("parking");
            lift = bundle.getString("lift");

        } else if (buttonName.equalsIgnoreCase("commercialAddProperty")) {
            propertyType = bundle.getString("property");
            floors = bundle.getString("noOfFloors");
            deposit = bundle.getString("deposit");
            lease = bundle.getString("lease");
        }

        Log.d("TAG", "uploadPropertyDetailsStatus: " + apartmentType + "\n" + apartmentName + "\n" + bhk + "\n" + extent + "\n" + rent + "\n" + roomNo + "\n" + floorNo + "\n" +
                tenantType + "\n" + foodType + "\n" + water + "\n" + parking + "\n" + lift + "\n" +
                contactTime + "\n" + address + "\n" + locality + "\n" + subLocality + "\n" + Arrays.toString(specialFeatures) + "\n" + latitude + "\n" + longitude);
        //CheckPermissions();
        imagerelativeLayout1 = findViewById(R.id.imagerelativeLayout1);
        image1 = findViewById(R.id.image1);
        clear1 = findViewById(R.id.clear1);
        capture1 = findViewById(R.id.capture1);
        imagerelativeLayout2 = findViewById(R.id.imagerelativeLayout2);
        image2 = findViewById(R.id.image2);
        clear2 = findViewById(R.id.clear2);
        capture2 = findViewById(R.id.capture2);
        imagerelativeLayout3 = findViewById(R.id.imagerelativeLayout3);
        image3 = findViewById(R.id.image3);
        clear3 = findViewById(R.id.clear3);
        capture3 = findViewById(R.id.capture3);
        upload = findViewById(R.id.upload);

        networkConnection = new NetworkConnection(this);
        progressDialog = new ProgressDialog(this, R.style.progressDialogStyle);
        clear1.setOnClickListener(this);
        capture1.setOnClickListener(this);
        clear2.setOnClickListener(this);
        capture2.setOnClickListener(this);
        clear3.setOnClickListener(this);
        capture3.setOnClickListener(this);
        upload.setOnClickListener(this);

        if (BitmapHelper.getInstance().getBitmap1() != null) {
            bitmap1 = BitmapHelper.getInstance().getBitmap1();
            imagerelativeLayout1.setVisibility(View.VISIBLE);
            capture1.setVisibility(View.GONE);
            image1.setImageBitmap(bitmap1);
        }
        Log.d("TAG", "onCreate: " + BitmapHelper.getInstance().getBitmap2());
        if (BitmapHelper.getInstance().getBitmap2() != null) {
            bitmap2 = BitmapHelper.getInstance().getBitmap2();
            imagerelativeLayout2.setVisibility(View.VISIBLE);
            capture2.setVisibility(View.GONE);
            image2.setImageBitmap(bitmap2);
        }
        if (BitmapHelper.getInstance().getBitmap3() != null) {
            bitmap3 = BitmapHelper.getInstance().getBitmap3();
            imagerelativeLayout3.setVisibility(View.VISIBLE);
            capture3.setVisibility(View.GONE);
            image3.setImageBitmap(bitmap3);
        }
    }

    /*toolbar icons initializations and onclick*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_icons, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.profile:
                Toast.makeText(this, "profile", Toast.LENGTH_SHORT).show();
                break;
            case R.id.logOut:
                new LogOut().show(getFragmentManager(), "LogOut");
                break;
        }
        return true;
    }

    /*onclick for widgets*/
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.capture1:
                showPictureDialog(1);
                break;
            case R.id.clear1:
                imagerelativeLayout1.setVisibility(View.GONE);
                capture1.setVisibility(View.VISIBLE);
                bitmap1 = null;
                break;
            case R.id.capture2:
                showPictureDialog(2);
                break;
            case R.id.clear2:
                imagerelativeLayout2.setVisibility(View.GONE);
                capture2.setVisibility(View.VISIBLE);
                bitmap2 = null;
                break;
            case R.id.capture3:
                showPictureDialog(3);
                break;
            case R.id.clear3:
                imagerelativeLayout3.setVisibility(View.GONE);
                capture3.setVisibility(View.VISIBLE);
                bitmap3 = null;
                break;
            case R.id.upload:
                Log.d("TAG", "onClicksd: " + locality.substring(0, 3).toUpperCase() + "\n" +
                        generatePropertyId(locality.substring(0, 3).toUpperCase()));
                try {
                    if (bitmap1 == null) {
                        Toast.makeText(this, "please Select " + capture1.getText().toString(), Toast.LENGTH_LONG).show();
                    } else if (bitmap2 == null) {
                        Toast.makeText(this, "please Select " + capture2.getText().toString(), Toast.LENGTH_LONG).show();
                    } else if (bitmap3 == null) {
                        Toast.makeText(this, "please Select " + capture3.getText().toString(), Toast.LENGTH_LONG).show();
                    } else {
                        uploadPropertyDetailsStatus(
                                Config.getUserId(getApplicationContext()), latitude, longitude, locality, subLocality,
                                apartmentType, apartmentName, bhk, extent, rent, roomNo, floorNo, tenantType, foodType,
                                Arrays.toString(specialFeatures), water, parking, lift, contactTime, address);

                    }
                    Log.d("TAG", "uploadonClick: " + bitmap1 + "\n" +
                            bitmap2 + "\n" +
                            bitmap3);
                } catch (Exception e) {
                    Log.d("TAG", "uploadonClicke: " + e);
                }
                break;
        }
    }

    private void uploadPropertyDetailsStatus(String userId, double latitude, double longitude, String locality, String subLocality,
                                             String apartmentType, String apartmentName, String bhk, String extent, String rent,
                                             String roomNo, String floorNo, String tenantType, String foodType, String specialFeatures,
                                             String water, String parking, String lift, String contactTime, String address
    ) {
        if (networkConnection.isConnectingToInternet()) {
            try {
                progressDialog.setTitle("Uploading Details");
                progressDialog.setMessage("Please wait We are Uploading Details...");
                progressDialog.setCancelable(true);
                progressDialog.show();
                String inside = convertToString1();
                String imageName1 = capture1.getText().toString();
                String outside = convertToString2();
                String imageName2 = capture2.getText().toString();
                String specialArea = convertToString3();
                String imageName3 = capture3.getText().toString();
                apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);
                Call<AddPropertyResponse> call = null;
                /*inserting property details*/
                if (buttonName.equalsIgnoreCase("addProperty")) {
                    call = apiInterface.uploadPropertyDetails(new AddPropertyResponse(userId, 0, latitude, longitude, locality, subLocality,
                            apartmentType, apartmentName, bhk, extent, rent, roomNo, floorNo, tenantType, foodType, specialFeatures,
                            water, parking, lift, contactTime, address, inside, outside, specialArea));
                }/*updating property details*/ else if (buttonName.equalsIgnoreCase("edit")) {
                    propertyId = bundle.getInt("id");
                    call = apiInterface.updatePropertyDetails(new AddPropertyResponse(userId, propertyId, latitude, longitude, locality, subLocality,
                            apartmentType, apartmentName, bhk, extent, rent, roomNo, floorNo, tenantType, foodType, specialFeatures,
                            water, parking, lift, contactTime, address, inside, outside, specialArea));
                } else if (buttonName.equalsIgnoreCase("commercialAddProperty")) { /*adding new commercial property*/
                    call = apiInterface.uploadCommercialPropertyDetails(new AddPropertyResponse(userId, 0, "commercial", latitude, longitude, locality, subLocality,
                            propertyType, extent, floors, floorNo, specialFeatures, rent, deposit, lease, contactTime, address, inside, outside, specialArea));
                }
                Log.d("TAG", "uploadPropertyDetailsStatus:7 " + "\n" + userId + "\n" + latitude + "\n" + longitude + "\n" + locality + "\n" + subLocality + "\n" +
                        apartmentType + "\n" + apartmentName + "\n" + bhk + "\n" + extent + "\n" + rent + "\n" + roomNo + "\n" + floorNo + "\n" +
                        tenantType + "\n" + foodType + "\n" + water + "\n" + parking + "\n" + lift + "\n" + specialFeatures + "\n" +
                        contactTime + "\n" + address /*+ "\n" + inside + "\n" + outside +"\n"+specialArea*/);

                call.enqueue(new Callback<AddPropertyResponse>() {
                    @Override
                    public void onResponse(Call<AddPropertyResponse> call, Response<AddPropertyResponse> response) {
                        addPropertyResponse = response.body();
                        try {
                            Log.d("TAG", "uploadPropertyDetailsStatus:0" + addPropertyResponse.getStatus() + "\n" +
                                    addPropertyResponse.getUserID());
                            if (addPropertyResponse.getStatus().equalsIgnoreCase("SUCCESS")) {
                                progressDialog.cancel();
                                Toast.makeText(UploadPropertyImages.this, "Successfully Submitted Your Property Details", Toast.LENGTH_LONG).show();
                                Intent i = new Intent(getApplicationContext(), History.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(i);
                            } else {
                                progressDialog.cancel();
                                Toast.makeText(UploadPropertyImages.this, "Please Try Again Later", Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            progressDialog.cancel();
                            Toast.makeText(UploadPropertyImages.this, "Please Try Again Later", Toast.LENGTH_LONG).show();
                            Log.d("TAG", "uploadPropertyDetailsStatus:1 " + e);
                        }
                    }

                    @Override
                    public void onFailure(Call<AddPropertyResponse> call, Throwable t) {
                        progressDialog.cancel();
                        Toast.makeText(UploadPropertyImages.this, "Please Try Again Later", Toast.LENGTH_LONG).show();
                        Log.d("TAG", "uploadPropertyDetailsStatus:2 " + t);
                    }
                });

            } catch (Exception e) {
                if (progressDialog != null) {
                    progressDialog.cancel();
                }
                Toast.makeText(UploadPropertyImages.this, "Please Try Again Later", Toast.LENGTH_LONG).show();
                Log.d("TAG", "uploadPropertyDetailsStatus:3 " + e);
            }
        } else {
            Toast.makeText(this, R.string.no_net, Toast.LENGTH_LONG).show();
        }
    }

    /*generating property Id for property*/
    private String generatePropertyId(String locality) {
        String chars = "1234567890";
        StringBuilder builder = new StringBuilder();
        Random rd = new Random();
        while (builder.length() < 7) {
            int index = (int) (rd.nextFloat() * chars.length());
            builder.append(index);
        }
        return locality + builder.toString();
    }

    /*showing dialog to select image from camera or gallery*/
    private void showPictureDialog(final int id) {
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("Select Action");
        pictureDialog.setCancelable(true);
        String[] pictureDialogItems = {
                "Select photo from gallery",
                "Capture photo from camera"};
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                choosePhotoFromGallary(id);
                                break;
                            case 1:
                                TakePicture(id);
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }

    /*selecting image from gallery*/
    private void choosePhotoFromGallary(int id) {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryIntent.setType("image/*");
        switch (id) {
            case 1:
                startActivityForResult(galleryIntent, GALLERY1);
                break;
            case 2:
                startActivityForResult(galleryIntent, GALLERY2);
                break;
            case 3:
                startActivityForResult(galleryIntent, GALLERY3);
                break;
        }
    }

    /*capturing image from camera*/
    private void TakePicture(int id) {        //to capture image
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            takePictureIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION
                    | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            switch (id) {
                case 1:
                    File imageFile1 = null;
                    Log.d("TAG", "TakePicture: " + imageFile1);
                    try {
                        imageFile1 = getImageFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (imageFile1 != null) {
                        imageUri1 = FileProvider.getUriForFile(this,
                                "com.example.RentalManagement.fileprovider", imageFile1);
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri1);
                        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE1);
                    }
                    break;
                case 2:
                    File imageFile2 = null;
                    try {
                        imageFile2 = getImageFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (imageFile2 != null) {
                        imageUri2 = FileProvider.getUriForFile(this,
                                "com.example.RentalManagement.fileprovider", imageFile2);
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri2);
                        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE2);
                    }
                    break;
                case 3:
                    File imageFile3 = null;
                    try {
                        imageFile3 = getImageFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (imageFile3 != null) {
                        imageUri3 = FileProvider.getUriForFile(this,
                                "com.example.RentalManagement.fileprovider", imageFile3);
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri3);
                        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE3);
                    }
                    break;
            }

        }
    }

    /*storing image to local after capture to save highquality images*/
    private File getImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "jpg_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(imageFileName);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        // Save a file: path for use with ACTION_VIEW intents
        filePath = image.getAbsolutePath();
        return image;
    }

    /*on Activity result for images starts here*/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("TAG", "onActivityResult: " + requestCode + "\n" + resultCode + "\n" + data);
        switch (requestCode) {
            case REQUEST_IMAGE_CAPTURE1:
                try {
                    Log.d("TAG", "onActivityResult: " + resultCode);
                    if (resultCode == -1) {
                        bitmap1 = BitmapFactory.decodeFile(filePath);
                        imagerelativeLayout1.setVisibility(View.VISIBLE);
                        capture1.setVisibility(View.GONE);
                        image1.setImageBitmap(bitmap1);
                    } else {
                        imagerelativeLayout1.setVisibility(View.GONE);
                        capture1.setVisibility(View.VISIBLE);
                    }
                    break;
                } catch (Exception e) {

                }
            case GALLERY1:
                try {
                    Uri uri1 = data.getData();
                    imagerelativeLayout1.setVisibility(View.VISIBLE);
                    capture1.setVisibility(View.GONE);
                    bitmap1 = MediaStore.Images.Media.getBitmap(getContentResolver(), uri1);
                    image1.setImageBitmap(bitmap1);
                } catch (Exception e) {

                }
                break;
            case REQUEST_IMAGE_CAPTURE2:
                try {
                    if (resultCode == -1) {
                        bitmap2 = BitmapFactory.decodeFile(filePath);
                        imagerelativeLayout2.setVisibility(View.VISIBLE);
                        capture2.setVisibility(View.GONE);
                        image2.setImageBitmap(bitmap2);
                    } else {
                        imagerelativeLayout2.setVisibility(View.GONE);
                        capture2.setVisibility(View.VISIBLE);
                    }
                    break;
                } catch (Exception e) {

                }
            case GALLERY2:
                try {
                    Uri uri2 = data.getData();
                    imagerelativeLayout2.setVisibility(View.VISIBLE);
                    capture2.setVisibility(View.GONE);
                    bitmap2 = MediaStore.Images.Media.getBitmap(getContentResolver(), uri2);
                    image2.setImageBitmap(bitmap2);
                } catch (Exception e) {

                }
                break;
            case REQUEST_IMAGE_CAPTURE3:
                try {
                    if (resultCode == -1) {
                        bitmap3 = BitmapFactory.decodeFile(filePath);
                        imagerelativeLayout3.setVisibility(View.VISIBLE);
                        capture3.setVisibility(View.GONE);
                        image3.setImageBitmap(bitmap3);
                    } else {
                        imagerelativeLayout3.setVisibility(View.GONE);
                        capture3.setVisibility(View.VISIBLE);
                    }
                    break;
                } catch (Exception e) {

                }
            case GALLERY3:
                try {
                    Uri uri3 = data.getData();
                    imagerelativeLayout3.setVisibility(View.VISIBLE);
                    capture3.setVisibility(View.GONE);
                    bitmap3 = MediaStore.Images.Media.getBitmap(getContentResolver(), uri3);
                    image3.setImageBitmap(bitmap3);
                } catch (Exception e) {

                }
                break;
        }
    }

    /*converting images to base64 encoded string*/
    private String convertToString1() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap1.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    private String convertToString2() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap2.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    private String convertToString3() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap3.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    /*checking camera permissions*/
    public boolean CheckPermissions() {
        List<String> permissionsNeeded = new ArrayList<>();
        for (String permission : appPermissions) {
            if (ActivityCompat.checkSelfPermission(UploadPropertyImages.this, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                permissionsNeeded.add(permission);
            }
        }
        if (!permissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this,
                    permissionsNeeded.toArray(new String[permissionsNeeded.size()]),
                    PERMISSION_REQUEST_CODE);
            return false;
        }
        return true;
    }

    /*permission result for camera starts here*/
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            HashMap<String, Integer> permissionResults = new HashMap<>();
            int deniedCount = 0;
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    permissionResults.put(permissions[i], grantResults[i]);
                    deniedCount++;
                }
            }
            if (deniedCount > 0) {
                for (Map.Entry<String, Integer> entry : permissionResults.entrySet()) {
                    String permissionName = entry.getKey();
                    int permissionResult = entry.getValue();
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this, permissionName)) {
                        CheckPermissions();
                    } else {
                        Intent i = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                Uri.fromParts("package:", getPackageName(), null));
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                    }
                }
            }
        }
    }

    /*change statusbar color*/
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public Window statusBarColor() {
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        return window;
    }
}