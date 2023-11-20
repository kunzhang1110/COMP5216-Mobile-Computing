package au.edu.sydney.comp5216.homework2;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.theartofdev.edmodo.cropper.CropImage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Edit Activity: Crop a selected image and display the result.
 * The user can undo changes.
 * @author Kun Zhang
 */
public class EditActivity extends AppCompatActivity {
    final private static int CROP_REQUEST_CODE = 3;

    private ImageView imageView;

    private ImageAdapter imageAdapter;
    private Uri photoUri;
    private Uri resultUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        imageView = (ImageView)findViewById(R.id.imageView);
        if (getIntent().getExtras() != null){
            photoUri = getIntent().getParcelableExtra("photoUri");
            imageView.setImageURI(photoUri);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                resultUri = result.getUri();
                Log.i("xxx", ""+resultUri.toString());
                imageView.setImageURI(resultUri);
            }
        }

    }

    public void onCrop(View view){
        File file = new File(photoUri.getPath());
        Uri fileUri = FileProvider.getUriForFile( //file provider needed
                this.getApplicationContext(),
                "au.edu.sydney.comp5216.homework2.fileProvider", file);
        CropImage.activity(fileUri).start(this);
    }

    public void onUndo(View view){
        imageView.setImageURI(photoUri);
        resultUri = null;
    }

    /**
     * Replace original file with edited file and back to main view
     * @param view
     */
    public void onBack(View view){
        if(resultUri != null){
            moveFile(resultUri.getPath(),photoUri.getPath());
        }
        finish();
    }

    private void moveFile(String inputPath, String outputPath) {
        InputStream in;
        OutputStream out;
        try {
            in = new FileInputStream(inputPath);
            out = new FileOutputStream(outputPath);

            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            in.close();

            // write the output file
            out.flush();
            out.close();

            // delete the original file
            new File(inputPath).delete();

        }

        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
