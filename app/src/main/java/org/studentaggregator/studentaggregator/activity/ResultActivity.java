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
import org.studentaggregator.studentaggregator.adapter.AdapterResults;
import org.studentaggregator.studentaggregator.helper.DBHelper;
import org.studentaggregator.studentaggregator.helper.LocalDB;
import org.studentaggregator.studentaggregator.helper.ServerHelper;
import org.studentaggregator.studentaggregator.model.Student;
import org.studentaggregator.studentaggregator.model.StudentResult;

import java.util.ArrayList;

public class ResultActivity extends AppCompatActivity {
    ServerHelper serverHelper;
    String error;
    RecyclerView mRecycler;
    DBHelper dbHelper;
    LocalDB localDB;
    Context context = this;
    TextView textViewEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        dbHelper = new DBHelper(context);
        localDB = new LocalDB(context);

        textViewEmpty = (TextView) findViewById(R.id.textViewEmpty);

        mRecycler = (RecyclerView) findViewById(R.id.rv_result);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        mRecycler.setLayoutManager(manager);

        Student student = localDB.getStudent();
        ArrayList<StudentResult> resultList = dbHelper.getAllResult(student.getId());

        if (resultList == null) {
            textViewEmpty.setVisibility(View.VISIBLE);
        } else {
            AdapterResults resultAdapter = new AdapterResults(context, resultList);
            mRecycler.setAdapter(resultAdapter);
        }

    }


    //back button pressed
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent i = new Intent(context, MenuActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        return true;
    }//onOptionsItemSelected
}

