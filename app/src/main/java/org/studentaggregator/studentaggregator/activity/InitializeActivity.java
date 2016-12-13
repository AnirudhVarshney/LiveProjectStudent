package org.studentaggregator.studentaggregator.activity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import org.studentaggregator.studentaggregator.R;
import org.studentaggregator.studentaggregator.helper.DBHelper;
import org.studentaggregator.studentaggregator.helper.GetUserCallback;
import org.studentaggregator.studentaggregator.helper.JSONParser;
import org.studentaggregator.studentaggregator.helper.LocalDB;
import org.studentaggregator.studentaggregator.helper.MyUtils;
import org.studentaggregator.studentaggregator.helper.ServerHelper;
import org.studentaggregator.studentaggregator.model.Attendance;
import org.studentaggregator.studentaggregator.model.Event;
import org.studentaggregator.studentaggregator.model.Fee;
import org.studentaggregator.studentaggregator.model.Holiday;
import org.studentaggregator.studentaggregator.model.Query;
import org.studentaggregator.studentaggregator.model.Student;
import org.studentaggregator.studentaggregator.model.StudentResult;
import org.studentaggregator.studentaggregator.model.Task;
import org.studentaggregator.studentaggregator.model.Timetable;

import java.util.ArrayList;

/**
 * Created by Ray on 4/26/2016.
 */
public class InitializeActivity extends AppCompatActivity {

    private Context context = this;
    private Student student;
    private DBHelper dbHelper;
    LocalDB localDB;
    Boolean hasAttendance, hasHoliday, hasFee, cancel, hasNextPayment, hasNotification, hasResult, hasTask, hasQuery, hasTimetable = false;

    ArrayList<Attendance> attendanceList;
    ArrayList<Holiday> holidayList;
    ArrayList<Fee> feeList;
    ArrayList<Fee> nextFeeList;
    ArrayList<Event> notificationList;
    ArrayList<StudentResult> resultList;
    ArrayList<Task> taskList;
    ArrayList<Query> queryList;
    ArrayList<Timetable> timetableList;

    private ServerHelper serverHelperForAttendance = null;
    private ServerHelper serverHelperForHoliday = null;
    private ServerHelper serverHelperForFee = null;
    private ServerHelper serverHelperForNextFee = null;
    private ServerHelper serverHelperForNotification = null;
    private ServerHelper serverHelperForTask = null;
    private ServerHelper serverHelperForResult = null;
    private ServerHelper serverHelperForQuery = null;
    private ServerHelper serverHelperForTimetable = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initialize);

        hasAttendance = hasHoliday = hasFee = cancel = hasNextPayment = hasNotification = hasResult = hasTask = hasQuery = hasTimetable = false;
        dbHelper = new DBHelper(context);
        localDB = new LocalDB(context);
        student = new LocalDB(context).getTempStudent();

        getAttendance();
        getFee();
        getNextFee();
        getTask();
        getResult();
        getQuery();
        getTimetable();

        //todo check and skip if Task is already added for same class and division
        //todo check and skip if Timetable is already added for same class and division


        if (!localDB.getInitialised()) {
            MyUtils.logThis("InitializeActivity - adding student for first time");
            getHoliday();
            getNotification();
        } else {
            hasHoliday = true;
            hasNotification = true;
        }
    }//onCreate


    //--------------------------     Holiday      -------------------------------
    public void getHoliday() {

        String URL = MyUtils.BASE_URL + "get_holiday.php";
        ContentValues postParams = new ContentValues();
        postParams.put("studentid", student.getId());

        serverHelperForHoliday = new ServerHelper(URL, postParams, new GetUserCallback() {
            @Override
            public void done() {
                String reply = serverHelperForHoliday.getReply();
                if (reply == null) {
                    String errorDescription = getResources().getString(R.string.no_internet_warning);
                    MyUtils.logThis("InitializeActivity - errorDescription = " + errorDescription);
                    Toast.makeText(context, errorDescription, Toast.LENGTH_LONG).show();
                    return;
                }
                parseReplyForHoliday(reply);
            }
        });
        serverHelperForHoliday.execute((Void) null);

        /*
        StringRequest strReq = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                MyUtils.logThis("response = " + response);
                parseReplyForHoliday(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                cancel = true;
                Toast.makeText(context, getString(R.string.Oops), Toast.LENGTH_LONG).show();
                MyUtils.logThis("InitializeActivity VolleyError = " + error.getMessage());
                MyUtils.logThis("InitializeActivity VolleyError = " + error.getMessage());
            }
        }
        );
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

        */
    }//getHoliday

    private void parseReplyForHoliday(String reply) {

        String errorDescription;
        JSONParser parser = new JSONParser();

        holidayList = parser.parseHoliday(reply);

        // Un-Successful parsing
        if (holidayList == null) {
            errorDescription = parser.getErrorDescription();
            MyUtils.logThis("InitializeActivity - parseReplyForHoliday - errorDescription = " + errorDescription);
            Toast.makeText(context, errorDescription, Toast.LENGTH_LONG).show();
            cancel = true;
            return;
        }
        MyUtils.logThis("InitializeActivity - Holiday Added");
        hasHoliday = true;
        onCompleteInitializing();
    }//parseReplyForHoliday


    //--------------------------     Notification      -------------------------------
    public void getNotification() {

        String URL = MyUtils.BASE_URL + "get_notification.php";

        ContentValues postParams = new ContentValues();
        postParams.put("studentid", student.getId());

        serverHelperForNotification = new ServerHelper(URL, postParams, new GetUserCallback() {
            @Override
            public void done() {
                String reply = serverHelperForNotification.getReply();
                if (reply == null) {
                    String errorDescription = getResources().getString(R.string.no_internet_warning);
                    MyUtils.logThis("InitializeActivity - errorDescription = " + errorDescription);
                    Toast.makeText(context, errorDescription, Toast.LENGTH_LONG).show();
                    return;
                }
                parseReplyForNotification(reply);
            }
        });
        serverHelperForNotification.execute((Void) null);
        /*
        StringRequest strReq = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                MyUtils.logThis("response = " + response);
                parseReplyForNotification(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, getString(R.string.Oops), Toast.LENGTH_LONG).show();
                cancel = true;
                MyUtils.logThis("InitializeActivity VolleyError = " + error.getMessage());
            }
        }
        );
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
        */

    }//getNotification

    private void parseReplyForNotification(String reply) {

        String errorDescription;
        JSONParser parser = new JSONParser();

        notificationList = parser.parseNotification(reply);

        // Un-Successful parsing
        if (notificationList == null) {
            errorDescription = parser.getErrorDescription();
            MyUtils.logThis("InitializeActivity - parseReplyForNotification - errorDescription = " + errorDescription);
            Toast.makeText(context, errorDescription, Toast.LENGTH_LONG).show();
            cancel = true;
            return;
        }
        MyUtils.logThis("InitializeActivity - Notifications Added");
        hasNotification = true;
        onCompleteInitializing();
    }//parseReplyForAttendance


    //--------------------------     Attendance      -------------------------------
    public void getAttendance() {

        String URL = MyUtils.BASE_URL + "get_attendance.php";

        ContentValues postParams = new ContentValues();
        postParams.put("studentid", student.getId());

        serverHelperForAttendance = new ServerHelper(URL, postParams, new GetUserCallback() {
            @Override
            public void done() {
                String reply = serverHelperForAttendance.getReply();
                if (reply == null) {
                    String errorDescription = getResources().getString(R.string.no_internet_warning);
                    MyUtils.logThis("InitializeActivity - errorDescription = " + errorDescription);
                    Toast.makeText(context, errorDescription, Toast.LENGTH_LONG).show();
                    return;
                }
                parseReplyForAttendance(reply);
            }
        });
        serverHelperForAttendance.execute((Void) null);

        /*
        StringRequest strReq = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                MyUtils.logThis("response = " + response);
                parseReplyForAttendance(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                cancel = true;
                Toast.makeText(context, getString(R.string.Oops), Toast.LENGTH_LONG).show();
                MyUtils.logThis("InitializeActivity VolleyError = " + error.getMessage());
            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("studentid", student.getId());
                return params;
            }
        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
        */

    }//getAttendance

    private void parseReplyForAttendance(String reply) {

        String errorDescription;
        final JSONParser parser = new JSONParser();

        attendanceList = parser.parseAttendance(reply);

        // Un-Successful parsing
        if (attendanceList == null) {
            errorDescription = parser.getErrorDescription();
            MyUtils.logThis("InitializeActivity - parseReplyForAttendance - errorDescription = " + errorDescription);
            Toast.makeText(context, errorDescription, Toast.LENGTH_LONG).show();
            cancel = true;
            return;
        }
        MyUtils.logThis("InitializeActivity - Attendance Added");
        hasAttendance = true;
        onCompleteInitializing();
    }//parseReplyForAttendance


    //--------------------------     Fee      -------------------------------
    public void getFee() {

        String URL = MyUtils.BASE_URL + "get_fee.php";

        ContentValues postParams = new ContentValues();
        postParams.put("studentid", student.getId());

        serverHelperForFee = new ServerHelper(URL, postParams, new GetUserCallback() {
            @Override
            public void done() {
                String reply = serverHelperForFee.getReply();
                if (reply == null) {
                    String errorDescription = getResources().getString(R.string.no_internet_warning);
                    MyUtils.logThis("InitializeActivity - errorDescription = " + errorDescription);
                    Toast.makeText(context, errorDescription, Toast.LENGTH_LONG).show();
                    return;
                }
                parseReplyForFee(reply);
            }
        });
        serverHelperForFee.execute((Void) null);
        /*
        StringRequest strReq = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                MyUtils.logThis("response = " + response);
                parseReplyForFee(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                cancel = true;
                Toast.makeText(context, getString(R.string.Oops), Toast.LENGTH_LONG).show();
                MyUtils.logThis("InitializeActivity VolleyError = " + error.getMessage());
            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("studentid", student.getId());
                return params;
            }
        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
        */
    }//getFee

    private void parseReplyForFee(String reply) {

        String errorDescription;
        final JSONParser parser = new JSONParser();

        feeList = parser.parseFee(reply);

        // Un-Successful parsing
        if (feeList == null) {
            errorDescription = parser.getErrorDescription();
            MyUtils.logThis("InitializeActivity - parseReplyForAttendance - errorDescription = " + errorDescription);
            Toast.makeText(context, errorDescription, Toast.LENGTH_LONG).show();
            cancel = true;
            return;
        }
        MyUtils.logThis("InitializeActivity - Fee Added");
        hasFee = true;
        onCompleteInitializing();
    }//parseReplyForFee


    //--------------------------     NextFee      -------------------------------
    public void getNextFee() {

        String URL = MyUtils.BASE_URL + "get_nextfee.php";

        ContentValues postParams = new ContentValues();
        postParams.put("studentid", student.getId());

        serverHelperForNextFee = new ServerHelper(URL, postParams, new GetUserCallback() {
            @Override
            public void done() {
                String reply = serverHelperForNextFee.getReply();
                if (reply == null) {
                    String errorDescription = getResources().getString(R.string.no_internet_warning);
                    MyUtils.logThis("InitializeActivity - errorDescription = " + errorDescription);
                    Toast.makeText(context, errorDescription, Toast.LENGTH_LONG).show();
                    return;
                }
                parseReplyForNextFee(reply);
            }
        });
        serverHelperForNextFee.execute((Void) null);
        /*
        StringRequest strReq = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                MyUtils.logThis("response = " + response);
                parseReplyForNextFee(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                cancel = true;
                Toast.makeText(context, getString(R.string.Oops), Toast.LENGTH_LONG).show();
                MyUtils.logThis("InitializeActivity VolleyError = " + error.getMessage());
            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("studentid", student.getId());
                return params;
            }
        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
        */
    }//getNextFee

    private void parseReplyForNextFee(String reply) {

        String errorDescription;
        final JSONParser parser = new JSONParser();

        nextFeeList = parser.parseFee(reply);

        // Un-Successful parsing
        if (feeList == null) {
            errorDescription = parser.getErrorDescription();
            MyUtils.logThis("InitializeActivity - parseReplyForAttendance - errorDescription = " + errorDescription);
            Toast.makeText(context, errorDescription, Toast.LENGTH_LONG).show();
            cancel = true;
            return;
        }
        MyUtils.logThis("InitializeActivity - Next Payment Added");
        hasNextPayment = true;
        onCompleteInitializing();
    }//parseReplyForNextFee


    //--------------------------     Task      -------------------------------
    public void getTask() {

        String URL = MyUtils.BASE_URL + "get_task.php";

        ContentValues postParams = new ContentValues();
        postParams.put("class", student.getClassOfStudent());
        postParams.put("division", student.getDivision());

        serverHelperForTask = new ServerHelper(URL, postParams, new GetUserCallback() {
            @Override
            public void done() {
                String reply = serverHelperForTask.getReply();
                if (reply == null) {
                    String errorDescription = getResources().getString(R.string.no_internet_warning);
                    MyUtils.logThis("InitializeActivity - errorDescription = " + errorDescription);
                    Toast.makeText(context, errorDescription, Toast.LENGTH_LONG).show();
                    return;
                }
                parseReplyForTask(reply);
            }
        });
        serverHelperForTask.execute((Void) null);
        /*
        StringRequest strReq = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                MyUtils.logThis("response = " + response);
                parseReplyForTask(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                cancel = true;
                Toast.makeText(context, getString(R.string.Oops), Toast.LENGTH_LONG).show();
                MyUtils.logThis("InitializeActivity VolleyError = " + error.getMessage());
            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("class", student.getClassOfStudent());
                params.put("division", student.getDivision());
                return params;
            }
        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
        */
    }//getTask

    private void parseReplyForTask(String reply) {

        String errorDescription;
        final JSONParser parser = new JSONParser();

        taskList = parser.parseTask(reply);

        // Un-Successful parsing
        if (taskList == null) {
            errorDescription = parser.getErrorDescription();
            MyUtils.logThis("InitializeActivity - parseReplyForTask - errorDescription = " + errorDescription);
            Toast.makeText(context, errorDescription, Toast.LENGTH_LONG).show();
            cancel = true;
            return;
        }
        MyUtils.logThis("InitializeActivity - Tasks Added");
        hasTask = true;
        onCompleteInitializing();
    }//parseReplyForTask

    //--------------------------     Timetable      -------------------------------
    public void getTimetable() {

        String URL = MyUtils.BASE_URL + "get_timetable.php";

        ContentValues postParams = new ContentValues();
        postParams.put("class", student.getClassOfStudent());
        postParams.put("division", student.getDivision());

        serverHelperForTimetable = new ServerHelper(URL, postParams, new GetUserCallback() {
            @Override
            public void done() {
                String reply = serverHelperForTimetable.getReply();
                if (reply == null) {
                    String errorDescription = getResources().getString(R.string.no_internet_warning);
                    MyUtils.logThis("InitializeActivity - errorDescription = " + errorDescription);
                    Toast.makeText(context, errorDescription, Toast.LENGTH_LONG).show();
                    return;
                }
                parseReplyForTimetable(reply);
            }
        });
        serverHelperForTimetable.execute((Void) null);
    }//getTimetable


    private void parseReplyForTimetable(String reply) {

        String errorDescription;
        final JSONParser parser = new JSONParser();

        timetableList = parser.parseTimetable(reply);

        // Un-Successful parsing
        if (timetableList == null) {
            errorDescription = parser.getErrorDescription();
            MyUtils.logThis("InitializeActivity - parseReplyForTimetable - errorDescription = " + errorDescription);
            Toast.makeText(context, errorDescription, Toast.LENGTH_LONG).show();
            cancel = true;
            return;
        }
        MyUtils.logThis("InitializeActivity - Timetable Added");
        hasTimetable = true;
        onCompleteInitializing();
    }//parseReplyForTask


    //--------------------------     Result      -------------------------------
    public void getResult() {

        String URL = MyUtils.BASE_URL + "get_result.php";

        ContentValues postParams = new ContentValues();
        postParams.put("studentid", student.getId());

        serverHelperForResult = new ServerHelper(URL, postParams, new GetUserCallback() {
            @Override
            public void done() {
                String reply = serverHelperForResult.getReply();
                if (reply == null) {
                    String errorDescription = getResources().getString(R.string.no_internet_warning);
                    MyUtils.logThis("InitializeActivity - errorDescription = " + errorDescription);
                    Toast.makeText(context, errorDescription, Toast.LENGTH_LONG).show();
                    return;
                }
                parseReplyForResult(reply);
            }
        });
        serverHelperForResult.execute((Void) null);
        /*
        StringRequest strReq = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                MyUtils.logThis("response = " + response);
                parseReplyForResult(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                cancel = true;
                Toast.makeText(context, getString(R.string.Oops), Toast.LENGTH_LONG).show();
                MyUtils.logThis("InitializeActivity VolleyError = " + error.getMessage());
            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("studentid", student.getId());
                return params;
            }
        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
        */
    }//getResult

    private void parseReplyForResult(String reply) {

        String errorDescription;
        final JSONParser parser = new JSONParser();

        resultList = parser.parseStudentResults(reply);

        // Un-Successful parsing
        if (resultList == null) {
            errorDescription = parser.getErrorDescription();
            MyUtils.logThis("InitializeActivity - parseReplyForResult - errorDescription = " + errorDescription);
            Toast.makeText(context, errorDescription, Toast.LENGTH_LONG).show();
            cancel = true;
            return;
        }
        MyUtils.logThis("InitializeActivity - Result Added");
        hasResult = true;
        onCompleteInitializing();
    }//parseReplyForResult


    //--------------------------     Query      -------------------------------
    public void getQuery() {

        String URL = MyUtils.BASE_URL + "get_query.php";

        ContentValues postParams = new ContentValues();
        postParams.put("studentid", student.getId());

        serverHelperForQuery = new ServerHelper(URL, postParams, new GetUserCallback() {
            @Override
            public void done() {
                String reply = serverHelperForQuery.getReply();
                if (reply == null) {
                    String errorDescription = getResources().getString(R.string.no_internet_warning);
                    MyUtils.logThis("InitializeActivity - errorDescription = " + errorDescription);
                    Toast.makeText(context, errorDescription, Toast.LENGTH_LONG).show();
                    return;
                }
                parseReplyForQuery(reply);
            }
        });
        serverHelperForQuery.execute((Void) null);
        /*
        StringRequest strReq = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                MyUtils.logThis("response = " + response);
                parseReplyForQuery(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                cancel = true;
                Toast.makeText(context, getString(R.string.Oops), Toast.LENGTH_LONG).show();
                MyUtils.logThis("InitializeActivity VolleyError = " + error.getMessage());
            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("studentid", student.getId());
                return params;
            }
        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
        */
    }//getQuery

    private void parseReplyForQuery(String reply) {

        String errorDescription;
        final JSONParser parser = new JSONParser();

        queryList = parser.parseQuery(reply);

        // Un-Successful parsing
        if (queryList == null) {
            errorDescription = parser.getErrorDescription();
            MyUtils.logThis("InitializeActivity - parseReplyForQuery - errorDescription = " + errorDescription);
            Toast.makeText(context, errorDescription, Toast.LENGTH_LONG).show();
            cancel = true;
            return;
        }
        MyUtils.logThis("InitializeActivity - Query Added");
        hasQuery = true;
        onCompleteInitializing();
    }//parseReplyForQuery

    //--------------------------     Completion      -------------------------------
    public void onCompleteInitializing() {
        //there was some error in initializing
        if (cancel) {
            MyUtils.logThis("InitializeActivity - Error in initializing : hasAttendance=" + hasAttendance +
                    " hasFee=" + hasFee + " hasHoliday=" + hasHoliday + " hasNextPayment=" + hasNextPayment +
                    " hasNotification=" + hasNotification + " hasTask=" + hasTask + " hasQuery=" + hasQuery +
                    " hasTimetable=" + hasTimetable);
            //rollback everything

            //localDB.clear();
            //dbHelper.clear();

            Intent i = new Intent(context, AddStudentActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            finish();

        } else if (hasAttendance && hasHoliday && hasFee && hasNextPayment && hasNotification && hasTask && hasQuery && hasTimetable) {

            if (!localDB.getInitialised()) {
                dbHelper.addHoliday(holidayList);
                dbHelper.addEvent(notificationList);
            }
            dbHelper.addAttendance(attendanceList);
            dbHelper.addFee(feeList);
            dbHelper.addResult(resultList);
            dbHelper.addTask(taskList);
            dbHelper.addQuery(queryList);
            dbHelper.addTimetable(timetableList);
            if (nextFeeList.size() != 0) {
                localDB.saveNextPayment(nextFeeList.get(0));
            }

            dbHelper.addStudent(student);
            MyUtils.logThis("InitializeActivity - Everything is initialized successfully");

            localDB.setInitialised(true);
            localDB.setStudent(student);

            Intent i = new Intent(getApplicationContext(), MenuActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            finish();
        }
    }
}
