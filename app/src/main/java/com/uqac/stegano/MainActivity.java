package com.uqac.stegano;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_PERMISSIONS = 1234;
    private static final String[] PERMISSIONS = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private static final int PERMISSIONS_COUNT = 2;
    private boolean isGalleryInitialized;
    private SwipeRefreshLayout refreshLayout;

    public Toast mToast;

    @SuppressLint("NewApi")
    private boolean arePermissionsDenied() {
        for(int i=0 ; i < PERMISSIONS_COUNT ; i++) {
            if(checkSelfPermission(PERMISSIONS[i]) != PackageManager.PERMISSION_GRANTED) {
                return true;
            }
         }

        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQUEST_PERMISSIONS && grantResults.length>0) {
            if(arePermissionsDenied()) {
                ((ActivityManager) Objects.requireNonNull(this.getSystemService(ACTIVITY_SERVICE)))
                        .clearApplicationUserData();
                recreate();
            } else {
                onResume();
            }
        }
    }

    private void loadImages() {
        final GalleryAdapter galleryAdapter = new GalleryAdapter();
        final GridView gridView = findViewById(R.id.gridView);
        final List<String> filesList = new ArrayList<>();
        final String[] columns = { MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID };
        final String orderBy = MediaStore.Images.Media._ID;

        //Stores all the images from the gallery in Cursor
        Cursor cursor = getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null,
                null, orderBy);
        //Total number of images
        int count = cursor.getCount();

        //Create an array to store path to all the images
        String[] arrPath = new String[count];

        for (int i = 0; i < count; i++) {
            cursor.moveToPosition(i);
            int dataColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
            //Store the path of the image
            arrPath[i]= cursor.getString(dataColumnIndex);
            //Log.i("PATH", arrPath[i]);
        }
        // The cursor should be freed up after use with close()
        cursor.close();

        for(int i=count-1 ; i >= 0 ; i--) { // reverse for latest image on top
            if (arrPath[i].endsWith(".jpg") || arrPath[i].endsWith(".png") || arrPath[i].endsWith(".jpeg")) {
                filesList.add(arrPath[i]);
            }
        }

        galleryAdapter.setData(filesList);
        gridView.setAdapter(galleryAdapter);
        galleryAdapter.notifyDataSetChanged();

        // Make grid clickable
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                String stringVal = filesList.get(pos);
                Intent intent = new Intent(MainActivity.this, DisplayActivity.class);
                intent.putExtra("path", stringVal);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && arePermissionsDenied()){
            requestPermissions(PERMISSIONS, REQUEST_PERMISSIONS);
            return;
        }

        if(!isGalleryInitialized) {
            loadImages();
            // isGalleryInitialized = true;
        }
    }

    final class GalleryAdapter extends BaseAdapter {

        private List<String> data = new ArrayList<>();

        void setData(List<String> data) {
            if(this.data.size() > 0) {
                data.clear();
            }
            this.data.addAll(data);
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView;

            if(convertView == null) {
                imageView = (ImageView) LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
            } else {
                imageView = (ImageView) convertView;
            }

            Glide.with(MainActivity.this).load(data.get(position)).centerCrop().into(imageView);
            return imageView;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        isGalleryInitialized = false;
        mToast = Toast.makeText(getBaseContext(), "", Toast.LENGTH_SHORT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        refreshLayout = findViewById(R.id.swipeRefresh);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshImages();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.menu_refresh) {
            refreshImages();
        }

        return super.onOptionsItemSelected(item);
    }

    private void refreshImages() {
        mToast.setText("Refreshing");
        mToast.show();
        loadImages();
        refreshLayout.setRefreshing(false);
        mToast.setText("Refreshed");
        mToast.show();
    }
}
