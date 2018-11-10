package au.edu.sydney.comp5216.homework2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

/**
 * An adapter that bridges between imageView and photo uris
 * @author Kun Zhang
 */

public class ImageAdapter extends BaseAdapter {


    final private static int sampleScale = 12; //down sample scale
    final private static int imageViewScale = 4; //down sample scale

    private Context mContext;
    private ArrayList<Uri> photoUriList;     // references to our images
    private int imageHeight;
    private int imageWidth;


    public ImageAdapter(Context context, ArrayList<Uri> uriList) {
        mContext = context;
        photoUriList = uriList;
        imageHeight = (int) mContext.getResources().getDimension(R.dimen.imageHeight);
        imageWidth = (int) mContext.getResources().getDimension(R.dimen.imageWidth);
    }


    public int getCount() {
        return photoUriList.size();
    }

    public Object getItem(int position) {
        return photoUriList.get(position);
    }

    public long getItemId(int position) {
        return 0;//TODO: return the row id of the item
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;

        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(imageHeight/imageViewScale, imageWidth/imageViewScale)); //maintain the image's aspect ratio
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP); //cropped toward the center
        } else {
            imageView = (ImageView) convertView;
        }

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = sampleScale; //down sample
        Bitmap photoBitmap =  BitmapFactory.decodeFile(photoUriList.get(position).getPath(),options);
        imageView.setImageBitmap(photoBitmap);
        return imageView;
    }
}