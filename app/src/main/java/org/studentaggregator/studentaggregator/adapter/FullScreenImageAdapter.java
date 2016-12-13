package org.studentaggregator.studentaggregator.adapter;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.studentaggregator.studentaggregator.R;
import org.studentaggregator.studentaggregator.helper.CircleTransform;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FullScreenImageAdapter extends PagerAdapter {

    private Context context;
    private ArrayList<String> imagePathList;
    private LayoutInflater inflater;
    String id;
    static final String FOLDER_NAME = "studentaggregator";

    // constructor
    public FullScreenImageAdapter(Activity activity,
                                  ArrayList<String> imagePaths, String Id) {
        this.context = activity;
        this.imagePathList = imagePaths;
        this.id = Id;
    }

    @Override
    public int getCount() {
        return this.imagePathList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        final ImageView imgDisplay;
        final ImageButton btnsave;

        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewLayout = inflater.inflate(R.layout.layout_fullscreen_image, container,
                false);

        imgDisplay = (ImageView) viewLayout.findViewById(R.id.imgDisplay);


        btnsave = (ImageButton) viewLayout.findViewById(R.id.save);


        Uri uri = Uri.parse(imagePathList.get(position));

        Picasso.Builder builder = new Picasso.Builder(context);
        builder.listener(new Picasso.Listener() {
            @Override
            public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                exception.printStackTrace();
                Picasso.with(context).load(R.drawable.error).networkPolicy(NetworkPolicy.OFFLINE).
                        into(imgDisplay);
            }
        });
        builder.build().load(imagePathList.get(position)).into(imgDisplay, new Callback() {
            @Override
            public void onSuccess() {
            }

            @Override
            public void onError() {
                Picasso.with(context).load(R.drawable.error).transform(new CircleTransform()).into(imgDisplay);
            }
        });
        //Glide.with(context).load(imagePathList.get(position)).into(imgDisplay);

        btnsave.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String url = imagePathList.get(position);
                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
                request.setDescription("Downloading Image");
                request.setTitle("Downloading Image");
                // in order for this if to run, you must use the android 3.2 to compile your app
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    request.allowScanningByMediaScanner();
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                }
                request.setDestinationInExternalPublicDir("studentaggregator", "event " + id + "_" + position + ".png");

                // get download service and enqueue file
                DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
                manager.enqueue(request);
                Toast.makeText(context, "Download Started", Toast.LENGTH_SHORT).show();

                /*
                imgDisplay.buildDrawingCache();
                Bitmap bm = imgDisplay.getDrawingCache();
                OutputStream fOut = null;
                try {
                    File root = new File(Environment.getExternalStorageDirectory()
                            + File.separator + FOLDER_NAME + File.separator);
                    root.mkdirs();
                    File sdImageMainDirectory = new File(root, "event " + id + "_" + position + ".png");
                    fOut = new FileOutputStream(sdImageMainDirectory);
                } catch (Exception e) {
                    Toast.makeText(_activity, "Error occured. Please try again later.",
                            Toast.LENGTH_SHORT).show();
                }

                try {
                    bm.compress(Bitmap.CompressFormat.PNG, 100, fOut);
                    fOut.flush();
                    fOut.close();
                } catch (Exception e) {
                }
                */
                btnsave.setVisibility(View.GONE);
            }
        }));

        ((ViewPager) container).addView(viewLayout);

        return viewLayout;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((RelativeLayout) object);
    }
}
