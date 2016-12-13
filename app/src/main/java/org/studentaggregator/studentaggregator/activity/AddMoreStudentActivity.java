package org.studentaggregator.studentaggregator.activity;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.studentaggregator.studentaggregator.R;
import org.studentaggregator.studentaggregator.helper.DBHelper;
import org.studentaggregator.studentaggregator.helper.GetUserCallback;
import org.studentaggregator.studentaggregator.helper.JSONParser;
import org.studentaggregator.studentaggregator.helper.LocalDB;
import org.studentaggregator.studentaggregator.helper.MyUtils;
import org.studentaggregator.studentaggregator.helper.ServerHelper;
import org.studentaggregator.studentaggregator.model.Student;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;


public class AddMoreStudentActivity extends AppCompatActivity {
    //DBHelper db;

    private Context context = this;
    private ServerHelper serverHelper;

    // UI references
    private EditText editTextName;
    private EditText editTextContact;
    private Button buttonAddStudent;
    private ProgressDialog progressDialog;


    //global values
    private String studentName;
    private String contact;
    private String fcmid;
    // This tag will be used to cancel the request
    private String tag_string_req = "string_req";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_more_student);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        context = AddMoreStudentActivity.this;
        progressDialog = new ProgressDialog(context, R.style.ProgressDialogStyle);
        progressDialog.setMessage("Processing...");
        progressDialog.setCancelable(false);

        fcmid = FirebaseInstanceId.getInstance().getToken();

        bindUIComponents();
        setOnclickListeners();

    }//onCreate


    private void bindUIComponents() {
        buttonAddStudent = (Button) findViewById(R.id.buttonAddStudent);
        editTextName = (EditText) findViewById(R.id.editTextStudentName);
        editTextContact = (EditText) findViewById(R.id.editTextContact);
    }//bindUIComponents

    public void setOnclickListeners() {

        editTextContact.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.editTextAdd || id == EditorInfo.IME_NULL) {
                    attemptFetchStudent();
                    return true;
                }
                return false;
            }
        });

        buttonAddStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MyUtils.hasActiveInternetConnection(context)) {
                    attemptFetchStudent();
                } else {
                    Toast.makeText(context, R.string.no_internet_warning, Toast.LENGTH_LONG).show();
                }
            }
        });

    }//setOnclickListeners


    private void attemptFetchStudent() {

        //Reset errors
        editTextName.setError(null);
        editTextContact.setError(null);

        // Store values
        studentName = editTextName.getText().toString();
        contact = editTextContact.getText().toString();

        //cancel will be set to true if there are form errors (invalid email, missing fields, etc.)
        //and field will be focused
        boolean cancel = false;
        View focusView = null;

        // Check for a valid contact
        if (TextUtils.isEmpty(contact)) {
            editTextContact.setError(getString(R.string.error_field_required));
            focusView = editTextContact;
            cancel = true;
        }
        // Check for a valid contact, if the user entered one.
        else if (!isContactValid(contact)) {
            editTextContact.setError(getString(R.string.error_invalid_contact));
            focusView = editTextContact;
            cancel = true;
        }

        // Check for a valid name.
        if (TextUtils.isEmpty(studentName)) {
            editTextName.setError(getString(R.string.error_field_required));
            focusView = editTextName;
            cancel = true;
        }

        // There was an error; don't attempt signup and focus the first form field with an error.
        if (cancel) {
            focusView.requestFocus();
        } else {

            ContentValues postParams = new ContentValues();
            postParams.put("student_name", studentName);
            postParams.put("contact", contact);
            postParams.put("fcmid", fcmid);

            String URL = MyUtils.BASE_URL + "get_student.php";

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
                    }
                    parseReply(reply);
                }
            });
            showProgressDialog();
            serverHelper.execute((Void) null);

            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            //sendNetworkRequest();
            //makeStringReq();
            //makeStringReq();
        }
    }//attemptFetchStudent

    /*
        public void sendNetworkRequest() {
            String URL = MyUtils.BASE_URL + "get_student.php";
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
                    params.put("student_name", studentName);
                    params.put("contact", contact);
                    params.put("fcmid", fcmid);
                    return params;
                }
            };
            // Adding request to request queue
            AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
        }//sendNetworkRequest

    /*
    private void makeStringReq() {
        String URL = "http://www.studentaggregator.org/application/get_student.php";
        showProgressDialog();
        //String URL = "http://192.168.43.224/fcm/test.php";

        StringRequest strReq = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                MyUtils.logThis(response);
                hideProgressDialog();
                parseReply(response);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(MyUtils.LOG_TAG, "Error: " + error.getMessage());
                hideProgressDialog();
            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("student_name", studentName);
                params.put("contact", contact);
                params.put("fcmid", fcmid);
                return params;
            }
        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }

    */

    private void parseReply(String reply) {
        String errorDescription;
        final JSONParser parser = new JSONParser();

        Student student = parser.parseStudent(reply);

        // Un-Successful parsing
        if (student == null) {
            errorDescription = parser.getErrorDescription();
            MyUtils.logThis("Login - errorDescription = " + errorDescription);
            Toast.makeText(context, errorDescription, Toast.LENGTH_LONG).show();
        }
        //Successful parsing
        else {
            LocalDB localdb = new LocalDB(context);
            localdb.setTempStudent(student);

            DBHelper dbHelper = new DBHelper(context);
            ArrayList<Student> studentList;
            studentList = dbHelper.getAllStudents();
            if (studentList != null) {
                for (int i = 0; i < studentList.size(); i++) {
                    if (student.getId().equals(studentList.get(i).getId())) {
                        Toast.makeText(context, "Student " + student.getName() + " is already added", Toast.LENGTH_LONG).show();
                        return;
                    }
                }
            }

            Intent i = new Intent(context, OTPVerificationActivity.class);
            startActivity(i);

        }//else where student!=null

    }//parseReply


    private boolean isContactValid(String contact) {
        return contact.length() == 10;
    }//isContactValid

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

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent i = new Intent(AddMoreStudentActivity.this, MenuActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            finish();
        }
        return true;
    }

}//AddStudent
