package com.uqac.stegano;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


public class DisplayActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Display the layout
        setContentView(R.layout.image_select);

        // Getting the image path
        String photo_path = null;
        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
            photo_path = bundle.getString("path");

        // Display the relative image
        if (photo_path != null) {
            ImageView imageView = (ImageView) findViewById(R.id.singleImage);
            Bitmap myBitmap = Utilities.loadImage(photo_path);
            imageView.setImageBitmap(myBitmap);
        }

        // Update the hidden text in the image
        TextView textView  = (TextView)  findViewById(R.id.imageText);
        String decodeText = "Calling here the function to decode the image, or maybe we do it before and then test if there is anything, idk...";
        textView.setText(decodeText);

        // Create the event for the button
        Button button = (Button) findViewById(R.id.saveButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = "Get the text in the editor";
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