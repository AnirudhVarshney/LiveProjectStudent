package org.studentaggregator.studentaggregator.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import org.studentaggregator.studentaggregator.R;
import org.studentaggregator.studentaggregator.adapter.FullScreenImageAdapter;
import org.studentaggregator.studentaggregator.helper.MyUtils;

import java.util.ArrayList;

public class FullscreenImageActivity extends AppCompatActivity {

    Button buttonClose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen_image);

        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        buttonClose = (Button)findViewById(R.id.buttonClose);


        Intent intent = getIntent();
        String id = intent.getStringExtra("Id");
        int position = intent.getIntExtra("position", 0);
        int noOfImages = Integer.parseInt(intent.getStringExtra("noOfImages"));

        String folder = getResources().getString(R.string.institute_folder);
        ArrayList<String> arrayImageUrl = new ArrayList<>();
        for (int i = 0; i < noOfImages; i++) {
            String imageURL = MyUtils.BASE_URL + "events/" + folder + "/" + id + "_" + (i + 1) + ".png";
            arrayImageUrl.add(imageURL);
        }

        FullScreenImageAdapter adapter = new FullScreenImageAdapter(FullscreenImageActivity.this, arrayImageUrl, id);
        assert viewPager != null;
        viewPager.setAdapter(adapter);
        // displaying selected image first
        viewPager.setCurrentItem(position);


        buttonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}


