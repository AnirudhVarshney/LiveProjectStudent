package org.studentaggregator.studentaggregator.activity;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

import org.studentaggregator.studentaggregator.R;

/**
 * Created by Ray on 12-Jun-16.
 */
public class VideoPlayerActivity extends Activity {

    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the layout from video_main.xml
        setContentView(R.layout.activity_video_player);

        // Locate the button in activity_main.xml
        webView = (WebView) findViewById(R.id.webView);

        String videoID;
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                videoID = null;
            } else {
                videoID = extras.getString("id");
            }
        } else {
            videoID = (String) savedInstanceState.getSerializable("id");
        }

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setPluginState(WebSettings.PluginState.ON);
        webView.loadUrl("http://www.youtube.com/embed/" + videoID + "?autoplay=1&vq=small");
        //webView.setWebChromeClient(new WebChromeClient());
    }

    @Override
    protected void onResume() {
        super.onResume();
        finish();
    }
}
