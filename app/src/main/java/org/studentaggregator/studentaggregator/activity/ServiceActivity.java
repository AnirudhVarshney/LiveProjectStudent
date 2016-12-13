package org.studentaggregator.studentaggregator.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import org.studentaggregator.studentaggregator.R;
import org.studentaggregator.studentaggregator.adapter.VideoAdapter;
import org.studentaggregator.studentaggregator.model.Video;

import java.util.ArrayList;

public class ServiceActivity extends AppCompatActivity {

    private RecyclerView recyclerViewVideo;
    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        recyclerViewVideo = (RecyclerView) findViewById(R.id.recyclerViewVideo);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerViewVideo.setLayoutManager(manager);

        ArrayList<Video> videoList = new ArrayList<>();

        Video video1 = new Video("https://www.youtube.com/watch?v=r8iGnwD8i9I", "Nursery Rhymes", "ABC Song and many more Nursery Rhymes for Children", "r8iGnwD8i9I");
        Video video2 = new Video("https://www.youtube.com/watch?v=7FP0ZwGRlxI", "Science Project", "Science project by class 8 students 05 simple circuit", "7FP0ZwGRlxI");
        Video video3 = new Video("https://www.youtube.com/watch?v=SSwbLYiH_ig", "Science Project", "Science project by class 8 students 06 simple circuit", "SSwbLYiH_ig");
        Video video4 = new Video("https://www.youtube.com/watch?v=5c_lL6I3OaA", "Geography Project", "Planets in our solar system | Sun and solar system for children", "5c_lL6I3OaA");
        Video video5 = new Video("https://www.youtube.com/watch?v=3pD68uxRLkM", "life science  Project", "Photosynthesis in plants | Biology basics for children", "3pD68uxRLkM");
        Video video6 = new Video("https://www.youtube.com/watch?v=mfrp78wla7g", "History Project", "Aurangzeb Alamgir -The Greatest Strongest & Invincible Mughal Emperor 1618-1707", "mfrp78wla7g");

        videoList.add(video1);
        videoList.add(video2);
        videoList.add(video3);
        videoList.add(video4);
        videoList.add(video5);
        videoList.add(video6);

        VideoAdapter adapterVideo = new VideoAdapter(context, videoList);
        recyclerViewVideo.setAdapter(adapterVideo);

    }


    //back button pressed
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            //int id = item.getItemId();
            Intent i = new Intent(getApplicationContext(), MenuActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
        }
        return true;
    }
}
