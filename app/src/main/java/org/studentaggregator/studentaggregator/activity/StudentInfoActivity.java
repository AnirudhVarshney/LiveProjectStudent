package org.studentaggregator.studentaggregator.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import org.studentaggregator.studentaggregator.R;
import org.studentaggregator.studentaggregator.helper.DBHelper;
import org.studentaggregator.studentaggregator.helper.LocalDB;
import org.studentaggregator.studentaggregator.helper.MyUtils;
import org.studentaggregator.studentaggregator.model.Student;
import org.studentaggregator.studentaggregator.model.Timetable;

import java.util.ArrayList;
import java.util.List;


public class StudentInfoActivity extends AppCompatActivity {

    TextView textViewFathersName;
    TextView textViewMothersName;
    TextView textViewContactNumber;
    TextView textViewDateOfBirth;
    LocalDB localDB;
    Context context = this;
    DBHelper dbHelper;
    Student student;
    Spinner spinner;
    TextView textViewTime;
    TextView textViewSubject;
    TextView textViewSubjectList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_info);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        localDB = new LocalDB(context);
        dbHelper = new DBHelper(context);
        student = localDB.getStudent();
        bindUIComponents();
        showData();
        setupTimetable();
        setupSubjects();
    }

    private void setupSubjects() {
        List<String> subjectList = dbHelper.getSubjects(student);
        String displaySubject = "";
        if (subjectList == null) {
            displaySubject = "Nothing to show";
        } else {
            for (int i = 0; i < subjectList.size(); i++)
                displaySubject += subjectList.get(i) + "\n";
        }
        textViewSubjectList.setText(displaySubject);
    }

    private void setupTimetable() {
        final List<String> list = new ArrayList<>();
        list.add("Monday");
        list.add("Tuesday");
        list.add("Wednesday");
        list.add("Thursday");
        list.add("Friday");
        list.add("Saturday");
        final ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(context,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                MyUtils.logThis("position = " + position);
                ArrayList<Timetable> timetableList = dbHelper.getTimetableFor(student.getClassOfStudent(), student.getDivision(), list.get(position));

                String time = "";
                String subject = "";
                if (timetableList != null) {
                    for (int i = 0; i < timetableList.size(); i++) {
                        String startTimeLocal = timetableList.get(i).getStartTime();
                        int startHour = Integer.parseInt(startTimeLocal.substring(0, 2));
                        String startampm = "am";
                        if (startHour > 12) {
                            startampm = "pm";
                        }
                        String startTimeTrim = startTimeLocal.substring(0, 5);
                        String endTimeLocal = timetableList.get(i).getEndTime();
                        String endTimeTrim = endTimeLocal.substring(0, 5);

                        String showTime = startTimeTrim + " - " + endTimeTrim + " " + startampm;
                        time += showTime + "\n";
                        subject += timetableList.get(i).getSubject() + "\n";
                    }
                }
                MyUtils.logThis("time=" + time);
                MyUtils.logThis("subject=" + subject);
                textViewTime.setText(time);
                textViewSubject.setText(subject);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void bindUIComponents() {
        textViewFathersName = (TextView) findViewById(R.id.textViewFathersName);
        textViewMothersName = (TextView) findViewById(R.id.textViewMothersName);
        textViewContactNumber = (TextView) findViewById(R.id.textViewContactNumber);
        textViewDateOfBirth = (TextView) findViewById(R.id.textViewDateOfBirth);
        spinner = (Spinner) findViewById(R.id.spinner);
        textViewTime = (TextView) findViewById(R.id.textViewTime);
        textViewSubject = (TextView) findViewById(R.id.textViewSubject);
        textViewSubjectList = (TextView) findViewById(R.id.textViewSubjectList);
    }//bindUIComponents

    private void showData() {
        Student student = localDB.getStudent();
        textViewFathersName.setText(student.getFather());
        textViewMothersName.setText(student.getMother());
        textViewContactNumber.setText(student.getContact());
        textViewDateOfBirth.setText(student.getDob());

    }

    //back button pressed
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent i = new Intent(context, MenuActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        return true;
    }//onOptionsItemSelected
}
