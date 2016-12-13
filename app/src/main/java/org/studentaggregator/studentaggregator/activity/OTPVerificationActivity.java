package org.studentaggregator.studentaggregator.activity;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.studentaggregator.studentaggregator.R;
import org.studentaggregator.studentaggregator.helper.GetUserCallback;
import org.studentaggregator.studentaggregator.helper.JSONParser;
import org.studentaggregator.studentaggregator.helper.LocalDB;
import org.studentaggregator.studentaggregator.helper.MyUtils;
import org.studentaggregator.studentaggregator.helper.ServerHelper;
import org.studentaggregator.studentaggregator.model.Student;
import com.google.firebase.iid.FirebaseInstanceId;

/**
 * Created by Ray on 11-Jun-16.
 */
public class OTPVerificationActivity extends AppCompatActivity {


    private ServerHelper serverHelper;
    private Student student;
    private LocalDB localDB;
    private Context context = this;
    private String tag_string_req = "string_req";

    // UI references
    private EditText editTextCode;
    private Button buttonSubmit;
    private TextView textViewNumber;
    private ProgressDialog progressDialog;
    private String fcmid;
    private String code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_verification);

        bindUIComponents();
        setOnClickListeners();

        localDB = new LocalDB(context);
        student = localDB.getTempStudent();
        String contact = "+91 " + student.getContact();
        textViewNumber.setText(contact);
        fcmid = FirebaseInstanceId.getInstance().getToken();
        progressDialog = new ProgressDialog(context, R.style.ProgressDialogStyle);
        progressDialog.setMessage("Processing...");
        progressDialog.setCancelable(false);


    }//onCreate


    private void bindUIComponents() {
        editTextCode = (EditText) findViewById(R.id.editTextCode);
        buttonSubmit = (Button) findViewById(R.id.buttonSubmit);
        textViewNumber = (TextView) findViewById(R.id.textViewNumber);
    }//bindUIComponents

    private void setOnClickListeners() {

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                code = editTextCode.getText().toString();
                if (TextUtils.isEmpty(code)) {
                    editTextCode.setError(getString(R.string.error_field_required));
                    editTextCode.requestFocus();
                    return;
                }
                if (!isCodeValid(code)) {
                    editTextCode.setError(getString(R.string.error_invalid_code));
                    editTextCode.requestFocus();
                    return;
                }

                ContentValues postParams = new ContentValues();
                postParams.put("studentid", student.getId());
                postParams.put("code", code);
                postParams.put("fcmid", fcmid);

                String URL = MyUtils.BASE_URL + "otp_verification.php";

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
                //sendNetworkRequest();
            }
        });
    }//setOnClickListeners

    /*
    public void sendNetworkRequest() {

        String URL = MyUtils.BASE_URL + "otp_verification.php";
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
                MyUtils.logThis("Error: " + error.getMessage());
            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("studentid", student.getId());
                params.put("code", code);
                params.put("fcmid", fcmid);
                return params;
            }
        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }//sendNetworkRequest
*/
    private void parseReply(String reply) {

        String errorDescription;
        final JSONParser parser = new JSONParser();

        Boolean success = parser.parseSuccess(reply);

        // Un-Successful parsing
        if (!success) {
            errorDescription = parser.getErrorDescription();
            MyUtils.logThis("OTPVerificationActivity - errorDescription = " + errorDescription);
            Toast.makeText(context, errorDescription, Toast.LENGTH_LONG).show();
        }
        //Successful parsing
        else {
            MyUtils.logThis("OTPVerificationActivity - Verification Successful");
            //localDB.setStudent(student);

            progressDialog.cancel();
            //DBHelper db = new DBHelper(context);
            //db.addStudent(student);

            Intent i = new Intent(context, InitializeActivity.class);
            startActivity(i);
            finish();
        }
    }//parseReply

    private boolean isCodeValid(String code) {
        return code.length() == 6;
    }//isCodeValid

    private void showProgressDialog() {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }//showProgressDialog

    private void hideProgressDialog() {
        if (progressDialog.isShowing())
            progressDialog.hide();
    }//hideProgressDialog

    //back button pressed
    public boolean onOptionsItemSelected(MenuItem item) {
        return true;
    }//onOptionsItemSelected

    @Override
    protected void onDestroy() {
        super.onDestroy();
        progressDialog.dismiss();
    }

}
