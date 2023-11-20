package au.edu.sydney.comp5216.homework2;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Optional;

/**
 * Main Activity: Display all users photos in a grid
 * @author Kun Zhang
 */
public class MainActivity extends AppCompatActivity {

    private static final int CAMERA_ACTIVITY_REQUEST_CODE = 1;
    private static final int EDIT_ACTIVITY_REQUEST_CODE = 2;
    private static final String APP_TAG = "Homework2";


    private GridView gridview;
    private ImageAdapter imageAdapter;
    private ArrayList<Uri> photoUriList = new ArrayList<>();     // references to our images
    private MarshmallowPermission marshmallowPermission = new MarshmallowPermission(this);

    @Override
    protected void onStart(){
        super.onStart();
        if(!marshmallowPermission.checkPermissionForReadfiles()) {
            marshmallowPermission.requestPermissionForReadfiles();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getAllPhotoUris();

        imageAdapter = new ImageAdapter(this,photoUriList);
        gridview = (GridView) findViewById(R.id.photoGrid);
        gridview.setAdapter(imageAdapter);

        setupGridViewListener();

    }

    @Override
    @SuppressWarnings("unchecked")
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == CAMERA_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Serializable serializable = data.getExtras().getSerializable("fileUriList");
                ArrayList<Uri> fileUrlList = null;

                if(serializable instanceof ArrayList<?>){
                    fileUrlList = (ArrayList<Uri>) serializable;
                }

                photoUriList.addAll(fileUrlList);
                imageAdapter.notifyDataSetChanged();
            }
        }else if (requestCode == EDIT_ACTIVITY_REQUEST_CODE){
            imageAdapter = new ImageAdapter(this,photoUriList);
            gridview = (GridView) findViewById(R.id.photoGrid);
            gridview.setAdapter(imageAdapter);
        }
    }

    public void setupGridViewListener() {
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Intent intent = new Intent(MainActivity.this, EditActivity.class);
                intent.putExtra("photoUri", photoUriList.get(position));
                // brings up the second activity
                startActivityForResult(intent, EDIT_ACTIVITY_REQUEST_CODE);

//                Toast.makeText(MainActivity.this, "" + position,
//                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    public Uri getFileUri(String fileName) {
        Uri fileUri = null;
        File file;
        try {
            String fileDir = "/images/"; //default to images type
            File mediaStorageDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath(),
                    fileDir+fileName);
            Log.d("Files", "Path: " + mediaStorageDir);
            if (!mediaStorageDir.getParentFile().exists() && !mediaStorageDir.getParentFile().mkdirs()) {// Create the storage directory if it does not exist
                Log.d(APP_TAG, "failed to create directory");
            }
            file = new File(mediaStorageDir.getParentFile().getPath() + File.separator + fileName);
            fileUri = FileProvider.getUriForFile(
                    this.getApplicationContext(),
                    "au.edu.sydney.comp5216.homework2.fileProvider", file);
        } catch (Exception ex) {
            Log.d("getFileUri", ex.getStackTrace().toString());
        }
        Log.i("FileUri:", "" + fileUri);
        return fileUri;
    }


    /**
    * Get Uris for all photos from folder "/image"
    * and store them in photoUriList
    */
    private void getAllPhotoUris() {
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/images/";
        File mediaStorageDir = new File(path);

        if(!mediaStorageDir.exists()){
            Log.d("Files", "Dir does not exist: " + mediaStorageDir);
            return;
        }
        File[] files = mediaStorageDir.listFiles();
        if (files != null) {
//            Log.d("Files", "Size: " + files.length);
            for (File file : files) {
                photoUriList.add(Uri.fromFile(new File(mediaStorageDir.getPath() + File.separator
                        + file.getName())));
//                Log.d("Files", "FileName:" + file.getName());
            }
        }
    }

    public void onTakePhotoClick(View view) {
        if (!marshmallowPermission.checkPermissionForCamera()
                || !marshmallowPermission.checkPermissionForExternalStorage()) {
            marshmallowPermission.requestPermissionForCamera();
        } else {
            Intent intent = new Intent(MainActivity.this, CameraActivity.class);
            startActivityForResult(intent, CAMERA_ACTIVITY_REQUEST_CODE);
        }
    }









}
