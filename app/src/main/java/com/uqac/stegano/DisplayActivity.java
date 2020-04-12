package com.uqac.stegano;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.util.Util;


public class DisplayActivity extends Activity {

    private String photo_path = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Display the layout
        setContentView(R.layout.image_select);

        // Getting the image path
        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
            photo_path = bundle.getString("path");

        // Display the relative image
        if (photo_path != null) {
            ImageView imageView = (ImageView) findViewById(R.id.singleImage);
            Bitmap myBitmap = Utilities.loadImage(photo_path);
            imageView.setImageBitmap(myBitmap);
        } else {
            Toast.makeText(getApplicationContext(), "Error loading image path", Toast.LENGTH_SHORT).show();
            finish();
        }

        // Update the hidden text in the image
        TextView textView  = (TextView)  findViewById(R.id.imageText);
        String decodeText = EncodeDecode.decodeMessage(Utilities.loadImage(photo_path));
        textView.setText(decodeText);

        // Create the event for the button
        Button button = (Button) findViewById(R.id.saveButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText editText = (EditText) findViewById(R.id.userText);
                String msg = editText.getText().toString();
                if (!msg.matches("")) {
                    Toast.makeText(getApplicationContext(), "Encryption started", Toast.LENGTH_SHORT).show();
                    Bitmap bmpIn = Utilities.loadImage(photo_path);
                    Bitmap bmpOut = EncodeDecode.encodeMessage(bmpIn, msg);
                    String savePath = Utilities.saveToInternalStorage(bmpOut, "01.png"); // TODO : find a better file location, allow to save multiple images
                    // Possibly restart this activity with the new image
                    Toast.makeText(getApplicationContext(), "Saved in " + savePath, Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getApplicationContext(), "No message to encrypt", Toast.LENGTH_SHORT).show();
                }
                // Encode the text in image
            }
        });
    }

}

// NOTE to start activity on click image
// String stringVal = <information de la photo>
// Intent i = new Intent(this, DisplayActivity.class);
// i.putString("path", stringVal);
// startActivity(i);