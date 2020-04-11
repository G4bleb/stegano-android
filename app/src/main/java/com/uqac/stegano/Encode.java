package com.uqac.stegano;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.ayush.imagesteganographylibrary.Text.AsyncTaskCallback.TextEncodingCallback;
import com.ayush.imagesteganographylibrary.Text.ImageSteganography;
import com.ayush.imagesteganographylibrary.Text.TextEncoding;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class Encode extends Stegano implements TextEncodingCallback {

    private ImageView imageViewOriginal;
    private ImageView imageViewEncoded;
    private Bitmap originalBitmap;
    private Bitmap encodedBitmap;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.encode);
        imageViewOriginal = findViewById(R.id.imageview_original);
        imageViewEncoded = findViewById(R.id.imageview_encoded);
        String imagePath = getIntent().getStringExtra("ClickedImagePath"); //get image path from post activity

        originalBitmap = loadImage(imagePath);
        Bitmap tmp = originalBitmap.copy(originalBitmap.getConfig(), originalBitmap.isMutable());
        imageViewOriginal.setImageBitmap(originalBitmap);
        encodeImage("test", tmp);

    }


    public void encodeImage(String message, Bitmap image){
        ImageSteganography imageSteganography = new ImageSteganography(message,
                "",
                image);
        TextEncoding textEncoding = new TextEncoding(Encode.this,
                Encode.this);
        textEncoding.execute(imageSteganography);
    }

    @Override
    public void onStartTextEncoding() {
        //Whatever you want to do at the start of text encoding
    }

    @Override
    public void onCompleteTextEncoding(ImageSteganography result) {
        encodedBitmap = result.getEncoded_image();
        String savePath = saveToInternalStorage(encodedBitmap, "01.png");
        imageViewEncoded.setImageBitmap(encodedBitmap);

        Intent i = new Intent(getApplicationContext(), Decode.class); //start new Intent to another Activity.
        i.putExtra("EncodedImagePath", savePath ); //put image link in intent.
        startActivity(i);
    }
}
