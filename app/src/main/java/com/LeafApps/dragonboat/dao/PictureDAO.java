package com.LeafApps.dragonboat.dao;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Nox on 13.02.2015.
 */
public class PictureDAO {
    String mCurrentPhotoPath;
    Activity source;
    ImageView mImageView;
    BoatDAO boatDAO;
    long boatid;

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_TAKE_PHOTO = 1;
    static final int REQUEST_LOAD_IMAGE = 99;

    public PictureDAO(Activity source, ImageView mImageView,BoatDAO boatDAO, long boatid) {
        //wieso wird Boat DAO übergeben? Image View raus?
        this.source=source;
        this.mImageView=mImageView;
        this.boatDAO=boatDAO;
        this.boatid=boatid;
    }
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "dragonboat");

        if (!storageDir.exists()) {
            if (!storageDir.mkdirs()) {
                Log.d("dragonboat", "failed to create directory");
                return null;
            }


        }
        //File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        File image = new File(storageDir.getPath() + File.separator +
                "IMG_" + timeStamp + ".jpg");

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();

        return image;

    }

    public void makepictureorshow(){
        boolean checkFile = false;
        boolean checkCamera = false;
        //if (checkCameraHardware(this.getApplicationContext())) {
        PackageManager pm = source.getPackageManager();
        if ( pm.hasSystemFeature(PackageManager.FEATURE_CAMERA) |
                pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT)){
            checkCamera = true;
        }

                if ( boatDAO.getPath(boatid) != null) {
            File f = new File(boatDAO.getPath(boatid));
            if (f.exists()) {
                checkFile = true;
            }
        }
       // From API level 9, you may want to check for FEATURE_CAMERA_FRONT besides FEATURE_CAMERA.
        //        For example, Nexus 7 (which has only one frontal camera) returns false to FEATURE_CAMERA. –

        //funzt nicht -Y nachschauen wo der update vom Path gemacht wird.
        //boatDAO.getPath(boatid)== null |
        if (checkCamera & !checkFile) {

                dispatchTakePictureIntent();

        }
        else{
            showPicture();
        }

    }
    public void getpicturefromgallerybyuser(){
    Intent i = new Intent(
            Intent.ACTION_PICK,
            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

    source.startActivityForResult(i, REQUEST_LOAD_IMAGE);

    }
    public void dispatchTakePictureIntent() {

            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            // Ensure that there's a camera activity to handle the intent
            if (takePictureIntent.resolveActivity(source.getPackageManager()) != null) {
                // Create the File where the photo should go
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                } catch (IOException ex) {
                    // Error occurred while creating the File

                }
                // Continue only if the File was successfully created
                if (photoFile != null) {
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                            Uri.fromFile(photoFile));
                    source.startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
                    //startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
            }

    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == source.RESULT_OK) {
           // mImageView.setImageURI(Uri.parse(mCurrentPhotoPath));
          // showPicture();

            boatDAO.setPath(boatid,mCurrentPhotoPath);
            galleryAddPic();
        }

        if (requestCode == REQUEST_LOAD_IMAGE && resultCode == source.RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = source.getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            boatDAO.setPath(boatid,picturePath);


        }



    }
    public void showPicture (){
        mCurrentPhotoPath=boatDAO.getPath(boatid);
        //String path=mCurrentPhotoPath;
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse("file://" + mCurrentPhotoPath), "image/jpg"); //angepasst path -> mCurrentPhotoPath
        source.startActivity(Intent.createChooser(intent, "Select Picture"));
    }
    public void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        source.sendBroadcast(mediaScanIntent);


    }



}
