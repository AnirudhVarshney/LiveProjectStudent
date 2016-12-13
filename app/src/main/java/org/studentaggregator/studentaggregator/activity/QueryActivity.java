package org.studentaggregator.studentaggregator.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.studentaggregator.studentaggregator.R;
import org.studentaggregator.studentaggregator.adapter.QueryAdapter;
import org.studentaggregator.studentaggregator.helper.DBHelper;
import org.studentaggregator.studentaggregator.helper.GetUserCallback;
import org.studentaggregator.studentaggregator.helper.JSONParser;
import org.studentaggregator.studentaggregator.helper.LocalDB;
import org.studentaggregator.studentaggregator.helper.MyUtils;
import org.studentaggregator.studentaggregator.helper.ServerHelper;
import org.studentaggregator.studentaggregator.model.Query;
import org.studentaggregator.studentaggregator.model.Student;

import java.util.ArrayList;
import java.util.GregorianCalendar;

public class QueryActivity extends AppCompatActivity {

    private FloatingActionButton fabWrite;
    private TextView textViewEmpty;
    private RecyclerView recyclerView;
    private ArrayList<Query> queryList;
    private Context context = this;
    private DBHelper dbHelper;
    private LocalDB localDB;
    private QueryAdapter queryAdapter;
    private Student student;
    private String dialogMessage;
    private ServerHelper serverHelper;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        dbHelper = new DBHelper(this);
        textViewEmpty = (TextView) findViewById(R.id.textViewEmpty);
        fabWrite = (FloatingActionButton) findViewById(R.id.fabWrite);
        progressDialog = new ProgressDialog(context, R.style.ProgressDialogStyle);
        progressDialog.setMessage("Processing...");
        progressDialog.setCancelable(false);

        localDB = new LocalDB(context);
        student = localDB.getStudent();

        queryList = dbHelper.getAllQuery(student.getId());
        recyclerView = (RecyclerView) findViewById(R.id.rv_recycler);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);

        if (queryList == null) {
            textViewEmpty.setVisibility(View.VISIBLE);
            MyUtils.logThis("no records in Query");
        } else {
            queryAdapter = new QueryAdapter(context, queryList);
            recyclerView.setAdapter(queryAdapter);
        }

        setOnClickListeners();
    }

    public void setOnClickListeners() {
        fabWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowInputAlert();
            }
        });
    }

    private void ShowInputAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Submit new query");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        builder.setView(input);

        builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialogMessage = input.getText().toString();
                if (TextUtils.isEmpty(dialogMessage)) {
                    Toast.makeText(context, getResources().getString(R.string.error_field_required), Toast.LENGTH_LONG).show();
                } else {
                    sendQuery();
                }

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    //back button pressed
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent i = new Intent(QueryActivity.this, MenuActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        return true;
    }//onOptionsItemSelected

    public void sendQuery() {
        /*    studentid    message    reply    date    */
        ContentValues postParams = new ContentValues();
        postParams.put("studentid", student.getId());
        postParams.put("message", dialogMessage);

        String URL = MyUtils.BASE_URL + "set_query.php";

        serverHelper = new ServerHelper(URL, postParams, new GetUserCallback() {
            @Override
            public void done() {
                hideProgressDialog();
                String reply = serverHelper.getReply();
                if (reply == null) {
                    String errorDescription = getResources().getString(R.string.no_internet_warning);
                    MyUtils.logThis("QueryActivity - errorDescription = " + errorDescription);
                    Toast.makeText(context, errorDescription, Toast.LENGTH_LONG).show();
                    return;
                }
                parseReply(reply);
            }
        });
        showProgressDialog();
        serverHelper.execute((Void) null);
    }

    //todo check this code
    private void parseReply(String reply) {
        String errorDescription;
        final JSONParser parser = new JSONParser();

        Boolean success = parser.parseSuccess(reply);

        // Un-Successful parsing
        if (!success) {
            errorDescription = parser.getErrorDescription();
            MyUtils.logThis("QueryActivity - errorDescription = " + errorDescription);
            Toast.makeText(context, errorDescription, Toast.LENGTH_LONG).show();
        }
        //Successful parsing
        else {
            GregorianCalendar gregorianCalendar = new GregorianCalendar();
            String date = MyUtils.getDate(gregorianCalendar);
            /*    studentid    message    reply    date    */
            Query query = new Query(student.getId(), dialogMessage, "null", date);
            ArrayList<Query> queryListLocal = new ArrayList<>();
            queryListLocal.add(query);
            dbHelper.addQuery(queryListLocal);
            queryList.add(query);
            queryAdapter.notifyDataSetChanged();
        }//else i.e successful parsing

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
