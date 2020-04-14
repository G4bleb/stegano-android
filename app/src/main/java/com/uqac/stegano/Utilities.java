package com.uqac.stegano;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utilities {
    /**
     * Loads a Bitmap from the given path
     * @param path
     * @return the Bitmap if succeeded, null otherwise
     */
    public static Bitmap loadImage(String path){
        if (path != null && !path.isEmpty()) {
            File imgFile = new File(path);
            if (imgFile.exists()) {
                return BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            }
        }
        return null;
    }

    /**
     * Saves a Bitmap as a PNG to pictures folder
     * @param bitmapImage
     * @param filename
     * @return the path to the image
     */
    public static String saveToPictures(Bitmap bitmapImage, String filename){
        File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

        directory = new File(directory, "Stegano");
        if(!directory.exists()) {
            directory.mkdir();
        }
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

    /**
     * Saves a Bitmap as a PNG to pictures folder, generating the file name
     * @param bitmapImage
     * @return the path to the image
     */
    public static String saveToPictures(Bitmap bitmapImage) {
        return saveToPictures(bitmapImage, new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss'.png'").format(new Date()));
    }

    public static String overwriteImage(Bitmap bitmapImage, String path) {
        new File(path).delete();

        String newpath=path.substring(0, path.lastIndexOf("."))+".png";
        File mypath = new File(newpath);

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

    /**
     * Refreshes the media scanner on given path
     * @param context the application context
     * @param path the path where the change has been made
     */
    public static void refreshGallery(Context context, String path){
        MediaScannerConnection
                .scanFile(
                        context,
                        new String[]{path},
                        null,
                        new MediaScannerConnection.OnScanCompletedListener() {
                            public void onScanCompleted(
                                    String path, Uri uri) {
                                Log.i("ExternalStorage", "Scanned "
                                        + path + ":");
                                Log.i("ExternalStorage", "-> uri="
                                        + uri);
                            }
                        });
    }

    /**
     * This method converts the byte array to an integer array.
     *
     * @return : Integer Array
     * @parameter : b {the byte array}
     */

    public static int[] byteArrayToIntArray(byte[] b) {

        Log.v("Size byte array", b.length + "");

        int size = b.length / 3;

        Log.v("Size Int array", size + "");

        System.runFinalization();
        //Garbage collection
        System.gc();

        Log.v("FreeMemory", Runtime.getRuntime().freeMemory() + "");
        int[] result = new int[size];
        int offset = 0;
        int index = 0;

        while (offset < b.length) {
            result[index++] = byteArrayToInt(b, offset);
            offset = offset + 3;
        }

        return result;
    }

    /**
     * Convert the byte array to an int.
     *
     * @return : Integer
     * @parameter :  b {the byte array}
     */
    public static int byteArrayToInt(byte[] b) {

        return byteArrayToInt(b, 0);

    }


    /**
     * Convert the byte array to an int starting from the given offset.
     *
     * @return :  Integer
     * @parameter :  b {the byte array}, offset {integer}
     */
    private static int byteArrayToInt(byte[] b, int offset) {

        int value = 0x00000000;

        for (int i = 0; i < 3; i++) {
            int shift = (3 - 1 - i) * 8;
            value |= (b[i + offset] & 0x000000FF) << shift;
        }

        value = value & 0x00FFFFFF;

        return value;
    }

    /**
     * Convert integer array representing [argb] values to byte array
     * representing [rgb] values
     *
     * @return : byte Array representing [rgb] values.
     * @parameter : Integer array representing [argb] values.
     */
    public static byte[] convertArray(int[] array) {

        byte[] newarray = new byte[array.length * 3];

        for (int i = 0; i < array.length; i++) {

            newarray[i * 3] = (byte) ((array[i] >> 16) & 0xFF);
            newarray[i * 3 + 1] = (byte) ((array[i] >> 8) & 0xFF);
            newarray[i * 3 + 2] = (byte) ((array[i]) & 0xFF);

        }

        return newarray;
    }

    /**
     * This method is used to check whether the string is empty of not
     *
     * @return : true or false {boolean}
     * @parameter : String
     */
    public static boolean isStringEmpty(String str) {
        boolean result = true;

        if (str == null) ;
        else {
            str = str.trim();
            if (str.length() > 0 && !str.equals("undefined"))
                result = false;
        }

        return result;
    }


}
