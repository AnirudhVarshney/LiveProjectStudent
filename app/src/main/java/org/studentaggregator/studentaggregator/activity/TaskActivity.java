package org.studentaggregator.studentaggregator.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import org.studentaggregator.studentaggregator.R;
import org.studentaggregator.studentaggregator.adapter.TaskAdapter;
import org.studentaggregator.studentaggregator.helper.DBHelper;
import org.studentaggregator.studentaggregator.helper.LocalDB;
import org.studentaggregator.studentaggregator.model.Student;
import org.studentaggregator.studentaggregator.model.Task;

import java.util.ArrayList;

public class TaskActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    TaskAdapter taskAdapter;
    Student student;
    DBHelper dbHelper;
    LocalDB localDB;
    ArrayList<Task> taskList;
    Context context;
    TextView textViewEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewTasks);
        textViewEmpty = (TextView) findViewById(R.id.textViewEmpty);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        context = TaskActivity.this;
        dbHelper = new DBHelper(context);
        localDB = new LocalDB(context);
        student = localDB.getStudent();
        taskList = dbHelper.getAllTask(student.getClassOfStudent(), student.getDivision());

        if (taskList == null) {
            textViewEmpty.setVisibility(View.VISIBLE);
            Log.d("mylog", "no records in task");
        } else {
            taskAdapter = new TaskAdapter(context, taskList);
            recyclerView.setAdapter(taskAdapter);
        }

    }

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
