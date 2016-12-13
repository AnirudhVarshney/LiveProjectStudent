package org.studentaggregator.studentaggregator.activity;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.studentaggregator.studentaggregator.R;
import org.studentaggregator.studentaggregator.helper.DBHelper;
import org.studentaggregator.studentaggregator.helper.GetUserCallback;
import org.studentaggregator.studentaggregator.helper.JSONParser;
import org.studentaggregator.studentaggregator.helper.LocalDB;
import org.studentaggregator.studentaggregator.helper.MyUtils;
import org.studentaggregator.studentaggregator.helper.ServerHelper;
import org.studentaggregator.studentaggregator.model.Attendance;
import org.studentaggregator.studentaggregator.model.Student;

import java.util.ArrayList;

/**
 * Created by Ray on 04-Sep-16.
 */
public class AbsenceReasonActivity extends AppCompatActivity {

    private RadioGroup radioGroupReason;
    private EditText editTextReason;
    private TextView textViewDescription;
    private Button buttonSubmit;
    Context context = this;
    DBHelper dbHelper;
    LocalDB localDB;
    private ServerHelper serverHelper;

    private String tag_string_req = "string_req";
    private ProgressDialog progressDialog;

    String date;
    String reason;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_absence_reason);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            Toast.makeText(context, getResources().getString(R.string.Oops), Toast.LENGTH_LONG).show();
            finish();
        }

        assert extras != null;
        date = extras.getString("date");
        MyUtils.logThis("AbsenceReasonActivity - date=" + date);
        /*month = extras.getString("month");
        year = extras.getString("year");*/
        //mdatetime = year + "-" + month + "-" + date + " 00:00:00";

        localDB = new LocalDB(context);
        dbHelper = new DBHelper(context);
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Processing...");
        progressDialog.setCancelable(false);

        String formattedDate = MyUtils.getFormattedDate(date);

        BindUIComponents();
        setOnClickListeners();

        String description = localDB.getStudent().getName() + " was absent on " + formattedDate + " for reason";
        textViewDescription.setText(description);

    }

    private void setOnClickListeners() {
        radioGroupReason.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                switch (v.getId()) {
                    case R.id.radioButton6:
                        editTextReason.setVisibility(View.VISIBLE);
                        break;
                    default:
                        editTextReason.setVisibility(View.GONE);
                        break;
                }
            }
        });

        radioGroupReason.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radioButton6:
                        editTextReason.setVisibility(View.VISIBLE);
                        break;
                    default:
                        editTextReason.setVisibility(View.GONE);
                        break;
                }
            }
        });

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = radioGroupReason.getCheckedRadioButtonId();
                reason = editTextReason.getText().toString();

                switch (id) {
                    case R.id.radioButton1:
                        reason = "Health issue";
                        break;
                    case R.id.radioButton2:
                        reason = "Family Function";
                        break;
                    case R.id.radioButton3:
                        reason = "Rainy day";
                        break;
                    case R.id.radioButton4:
                        reason = "Natural Disaster";
                        break;
                    case R.id.radioButton5:
                        reason = "Out of station";
                        break;
                    case R.id.radioButton6:
                        reason = editTextReason.getText().toString();
                        break;
                }


                String URL = MyUtils.BASE_URL + "set_application.php";

                final Student student = localDB.getStudent();
                final String sendDate = date;

                ContentValues postParams = new ContentValues();
                postParams.put("studentid", student.getId());
                postParams.put("date", sendDate);
                postParams.put("description", reason);

                serverHelper = new ServerHelper(URL, postParams, new GetUserCallback() {
                    @Override
                    public void done() {
                        hideProgressDialog();
                        String reply = serverHelper.getReply();
                        if (reply == null) {
                            String errorDescription = getResources().getString(R.string.no_internet_warning);
                            MyUtils.logThis("AddAccount - errorDescription = " + errorDescription);
                            Toast.makeText(context, errorDescription, Toast.LENGTH_LONG).show();
                            return;
                        } else if (reply.equals("kill")) {
                            MyUtils.logThis("killing me");
                            Intent i = getBaseContext().getPackageManager()
                                    .getLaunchIntentForPackage(getBaseContext().getPackageName());
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(i);
                        }
                        parseReply(reply);
                    }
                });
                showProgressDialog();
                serverHelper.execute((Void) null);
                //sendNetworkRequest();
                /*
                showProgress(true);


                ContentValues postParams = new ContentValues();
                postParams.put("studentid", student.getId());
                postParams.put("datetime", sendDate);
                postParams.put("description", reason);

                String url = "http://www.studentaggregator.org/demo/requestapplication.php";
                serverHelper = new ServerHelper(url, postParams, new GetUserCallback() {
                    @Override
                    public void done() {
                        String reply = serverHelper.getReply();
                        if (reply == null) {
                            String errorDescription = getResources().getString(R.string.no_internet_warning);
                            Log.d(LOG_TAG, "Services - errorDescription = " + errorDescription);
                            Toast.makeText(Absence.this, errorDescription, Toast.LENGTH_LONG).show();
                            showProgress(false);
                            return;
                        }
                        parseReply(reply);
                        showProgress(false);
                        //Toast.makeText(Absence.this, R.string.message_sent, Toast.LENGTH_LONG).show();
                    }
                });
                serverHelper.execute((Void) null);
                */


            }
        });
    }

    private void BindUIComponents() {
        radioGroupReason = (RadioGroup) findViewById(R.id.radioGroupReason);
        editTextReason = (EditText) findViewById(R.id.editTextReason);
        buttonSubmit = (Button) findViewById(R.id.buttonSubmit);
        textViewDescription = (TextView) findViewById(R.id.textViewDescription);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            //int id = item.getItemId();
            //todo check other id also
            Intent i = new Intent(context, MenuActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            finish();
        }
        return true;
    }

    private void parseReply(String reply) {

        String errorDescription;
        final JSONParser parser = new JSONParser();

        Boolean success = parser.parseSuccess(reply);

        // Un-Successful parsing
        if (!success) {
            errorDescription = parser.getErrorDescription();
            MyUtils.logThis("AbsenceReasonActivity - errorDescription = " + errorDescription);
            Toast.makeText(context, errorDescription, Toast.LENGTH_LONG).show();
        }
        //Successful parsing
        else {
            MyUtils.logThis("AbsenceReasonActivity - submission successful");

            ArrayList<Attendance> attendanceList = new ArrayList<>();

            Student student = localDB.getStudent();
            String mid = student.getId();
            String mstatus = "0";
            String mtime = "01:00:00";

            /*  studentid    status   description   date    time    */
            Attendance attendance = new Attendance(mid, mstatus, reason, date, mtime);
            MyUtils.logThis("Absense attendance=" + attendance);
            attendanceList.add(attendance);
            dbHelper.addAttendance(attendanceList);
            Toast.makeText(context, "We have received your request", Toast.LENGTH_LONG).show();

            finish();

        }
    }//parseReply

    private void showProgressDialog() {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }

    private void hideProgressDialog() {
        if (progressDialog.isShowing())
            progressDialog.hide();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        progressDialog.dismiss();
    }

}
/*
    public void sendNetworkRequest() {

        final Student student = localDB.getStudent();
        final String sendDate = year + "-" + month + "-" + date;

        String URL = MyUtils.BASE_URL + "set_application.php";
        showProgressDialog();


        StringRequest strReq = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                MyUtils.logThis("response = " + response);
                hideProgressDialog();
                parseReply(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, getString(R.string.Oops), Toast.LENGTH_LONG).show();
                hideProgressDialog();
                MyUtils.logThis("VolleyError = " + error.getMessage());
            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("studentid", student.getId());
                params.put("date", sendDate);
                params.put("description", reason);
                return params;
            }
        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }//sendNetworkRequest*/