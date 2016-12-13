package org.studentaggregator.studentaggregator.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import org.studentaggregator.studentaggregator.R;
import org.studentaggregator.studentaggregator.adapter.NotificationAdapter;
import org.studentaggregator.studentaggregator.helper.DBHelper;
import org.studentaggregator.studentaggregator.model.Event;

import java.util.ArrayList;

public class NotificationActivity extends AppCompatActivity {


    TextView textViewEmpty;
    RecyclerView recyclerView;
    ArrayList<Event> eventList;
    Context context = this;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        dbHelper = new DBHelper(this);
        textViewEmpty = (TextView) findViewById(R.id.textViewEmpty);
        recyclerView = (RecyclerView) findViewById(R.id.rv_recycler);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);

        eventList = dbHelper.getAllEvents();
        if (eventList == null) {
            textViewEmpty.setVisibility(View.VISIBLE);
        } else {
            NotificationAdapter notificationAdapter = new NotificationAdapter(context, eventList);
            recyclerView.setAdapter(notificationAdapter);
        }
    }

    //back button pressed
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent i = new Intent(NotificationActivity.this, MenuActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        return true;
    }//onOptionsItemSelected

}
