package com.uqac.stegano;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Debug;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

public class Stegano extends AppCompatActivity {

    public Bitmap loadImage(String path){
        if (path != null && !path.isEmpty()) {
            File imgFile = new File(path);
            if (imgFile.exists()) {
                return BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            }
        }
        return null;
    }

    public String saveToInternalStorage(Bitmap bitmapImage, String filename){
        File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

        // Create imageDir
        File mypath=new File(directory,filename);

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return mypath.getAbsolutePath();
    }
}
