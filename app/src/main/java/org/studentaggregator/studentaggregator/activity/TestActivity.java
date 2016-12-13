package org.studentaggregator.studentaggregator.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Spinner;
import android.widget.TextView;

import org.studentaggregator.studentaggregator.R;
import org.studentaggregator.studentaggregator.helper.DBHelper;
import org.studentaggregator.studentaggregator.helper.LocalDB;
import org.studentaggregator.studentaggregator.model.Student;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ray on 26-Aug-16.
 */
public class TestActivity extends AppCompatActivity {

    Context context = this;
    DBHelper dbHelper;
    LocalDB localDB;
    Student student;
    Spinner spinner;
    TextView textViewTime;
    TextView textViewSubject;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        dbHelper = new DBHelper(context);
        localDB = new LocalDB(context);
        student = localDB.getStudent();

        spinner = (Spinner) findViewById(R.id.spinner);
        textViewTime = (TextView) findViewById(R.id.textViewTime);
        textViewSubject = (TextView) findViewById(R.id.textViewSubject);

        final List<String> list = new ArrayList<>();
        list.add("Monday");
        list.add("Tuesday");
        list.add("Wednesday");
        list.add("Thursday");
        list.add("Friday");
        list.add("Saturday");



    }


}
