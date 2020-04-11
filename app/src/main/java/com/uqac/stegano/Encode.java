package com.uqac.stegano;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;


public class Encode extends AppCompatActivity {

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

        originalBitmap = Utilities.loadImage(imagePath);
        Bitmap tmp = originalBitmap.copy(originalBitmap.getConfig(), originalBitmap.isMutable());
        imageViewOriginal.setImageBitmap(originalBitmap);
        encodedBitmap = EncodeDecode.encodeMessage(tmp,"super secret message");

        String savePath = Utilities.saveToInternalStorage(encodedBitmap, "01.png");
        imageViewEncoded.setImageBitmap(encodedBitmap);

        Intent i = new Intent(getApplicationContext(), Decode.class); //start new Intent to another Activity.
        i.putExtra("EncodedImagePath", savePath ); //put image link in intent.
        startActivity(i);
    }

}
