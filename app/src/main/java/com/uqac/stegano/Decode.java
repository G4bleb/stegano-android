package com.uqac.stegano;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Decode extends AppCompatActivity {
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
        encodedBitmap = Utilities.loadImage(imagePath);
        String result = EncodeDecode.decodeMessage(encodedBitmap);

        //After the completion of text encoding.

        //result object is instantiated
        if (result != null){

            /* If result.isDecoded() is false, it means no Message was found in the image. */
            if (result.equals(""))
                logTextView.setText("No message found");

            else{
                    //set the message to the UI component.
                    logTextView.setText("Decoded");
                    decodedTextView.setText(result);

            }
        }
        else {
            //If result is null it means that bitmap is null
            logTextView.setText("Select Image First");
        }
    }
}
