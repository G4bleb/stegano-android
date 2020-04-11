package com.uqac.stegano;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.ayush.imagesteganographylibrary.Text.AsyncTaskCallback.TextDecodingCallback;
import com.ayush.imagesteganographylibrary.Text.ImageSteganography;
import com.ayush.imagesteganographylibrary.Text.TextDecoding;

public class Decode extends Stegano implements TextDecodingCallback {
    private TextView logTextView;
    private TextView decodedTextView;
    private Bitmap encodedBitmap;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.decode);
        decodedTextView = findViewById(R.id.textview_decoded);
        logTextView = findViewById(R.id.textview_decoded);

        String imagePath = getIntent().getStringExtra("EncodedImagePath"); //get image path from post activity
        Log.d("DECODE", "Loading "+imagePath);
        encodedBitmap = loadImage(imagePath);
        decodeImage(encodedBitmap);

    }

    public void decodeImage(Bitmap image){
        ImageSteganography imageSteganography = new ImageSteganography(
                "",
                image);
        TextDecoding textDecoding = new TextDecoding(Decode.this,
                Decode.this);
        textDecoding.execute(imageSteganography);

    }

    @Override
    public void onStartTextEncoding() {
        //Whatever you want to do at the start of text encoding
    }

    @Override
    public void onCompleteTextEncoding(ImageSteganography result) {

        //After the completion of text encoding.

        //result object is instantiated
        if (result != null){

            /* If result.isDecoded() is false, it means no Message was found in the image. */
            if (!result.isDecoded())
                logTextView.setText("No message found");

            else{
                /* If result.isSecretKeyWrong() is true, it means that secret key provided is wrong. */
                if (!result.isSecretKeyWrong()){
                    //set the message to the UI component.
                    logTextView.setText("Decoded");
                    decodedTextView.setText("" + result.getMessage());
                }
                else {
                    logTextView.setText("Wrong secret key");
                }
            }
        }
        else {
            //If result is null it means that bitmap is null
            logTextView.setText("Select Image First");
        }
    }
}
