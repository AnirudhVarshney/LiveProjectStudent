package org.studentaggregator.studentaggregator.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
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
import org.studentaggregator.studentaggregator.model.Holiday;
import org.studentaggregator.studentaggregator.model.Student;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Created by Ray on 4/20/2016.
 */

public class AttendanceActivity extends AppCompatActivity {

    final Context context = AttendanceActivity.this;
    int finalDayofmonth;

    //UI refrences
    private ProgressBar progressBar;
    private ImageButton imageButtonPrevMonth;
    private ImageButton imageButtonNextMonth;
    private TextView textViewSelectedDate;
    private TextView textViewPercentage;

    //class variables
    ServerHelper serverHelper;
    private GregorianCalendar selectedDate;
    private GregorianCalendar sessionStartDate;
    private GregorianCalendar sessionEndDate;
    GregorianCalendar gcNow;
    private ProgressDialog progressDialog;

    LocalDB localDB;
    Student student;
    DBHelper dbHelper;


    private String[] monthName = new String[]{"January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //initialize fields
        selectedDate = new GregorianCalendar();
        selectedDate.set(Calendar.DATE, 1);
        gcNow = new GregorianCalendar();

        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Processing...");
        progressDialog.setCancelable(false);

        dbHelper = new DBHelper(context);
        localDB = new LocalDB(AttendanceActivity.this);
        student = localDB.getStudent();

        //April 2016
        sessionStartDate = new GregorianCalendar();
        sessionStartDate.set(2016, 4 - 1, 1, 0, 0, 0);
        //March 2017
        sessionEndDate = new GregorianCalendar();
        sessionEndDate.set(2017, 3 - 1, 31, 0, 0, 0);


        bindUIComponents();
        seOnClickListeners();
        showCalendarForMonth();
        calculatePercentage();
    }


    private void bindUIComponents() {
        imageButtonPrevMonth = (ImageButton) findViewById(R.id.imageButtonPrevMonth);
        imageButtonNextMonth = (ImageButton) findViewById(R.id.imageButtonNextMonth);
        textViewSelectedDate = (TextView) findViewById(R.id.textViewSelectedDate);
        textViewPercentage = (TextView) findViewById(R.id.textViewPercentage);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        if (progressBar != null) {
            progressBar.setProgress(0);
            textViewPercentage.setText("0%");
        }
    }


    private void seOnClickListeners() {
        imageButtonNextMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GregorianCalendar temp = new GregorianCalendar();
                temp.set(selectedDate.get(Calendar.YEAR), selectedDate.get(Calendar.MONTH) + 1, selectedDate.get(Calendar.DATE), 0, 0, 0);
                if (!(temp.get(Calendar.YEAR) <= sessionEndDate.get(Calendar.YEAR) && temp.get(Calendar.MONTH) - 1 == sessionEndDate.get(Calendar.MONTH))) {
                    selectedDate.set(Calendar.MONTH, selectedDate.get(Calendar.MONTH) + 1);
                    showCalendarForMonth();
                } else {
                    Toast.makeText(AttendanceActivity.this, "Academic Session ends in March " + sessionEndDate.get(Calendar.YEAR), Toast.LENGTH_SHORT).show();
                }
            }
        });
        imageButtonPrevMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GregorianCalendar temp = new GregorianCalendar();
                temp.set(selectedDate.get(Calendar.YEAR), selectedDate.get(Calendar.MONTH) - 2, selectedDate.get(Calendar.DATE), 0, 0, 0);
                if (!(temp.get(Calendar.YEAR) <= sessionStartDate.get(Calendar.YEAR) && temp.get(Calendar.MONTH) + 1 == sessionStartDate.get(Calendar.MONTH))) {
                    selectedDate.set(Calendar.MONTH, selectedDate.get(Calendar.MONTH) - 1);
                    showCalendarForMonth();
                } else {
                    Toast.makeText(AttendanceActivity.this, "Academic Session starts in April " + sessionStartDate.get(Calendar.YEAR), Toast.LENGTH_SHORT).show();
                }

            }
        });
    }


    /*---------------------------------showCalendarForMonth---------------------------------------*/
    private void showCalendarForMonth() {

        //Set textViewSelectedDate text
        String stringSelectedDate = monthName[selectedDate.get(Calendar.MONTH)] + " " + selectedDate.get(Calendar.YEAR);
        textViewSelectedDate.setText(stringSelectedDate);
        setupInitialCalendar();
        if (gcNow.get(Calendar.MONTH) >= selectedDate.get(Calendar.MONTH) && gcNow.get(Calendar.YEAR) >= selectedDate.get(Calendar.YEAR)) {
            setupAttendance();
        }
        setupWeekend();
        //todo
        setupHolidays();

    }//showCalendarForMonth

    private void setupHolidays() {

        ArrayList<Holiday> holidaylist;
        Holiday holiday;
        holidaylist = dbHelper.getHolidaysForMonth(selectedDate);
        int dayofmonth = selectedDate.get(Calendar.DAY_OF_WEEK);
        //dayofmonth will skip blank days of first week
        //sun=1 mon=2 tue=3 wed=4 thu=5 fri=6 sat = 7 but our first day is monday and should not be skipped
        //for this we need to make mon=0 and sun=7
        dayofmonth = dayofmonth == 1 ? 8 : dayofmonth;
        //so now mon = 0 ... and sun = 6
        dayofmonth -= 1;

        if (holidaylist == null) {
            return;
        }

        int noofholidays = holidaylist.size();
        Resources res = getResources();

        for (int i = 0; i < noofholidays; i++) {

            holiday = holidaylist.get(i);

            int my = Integer.parseInt(holiday.getDate().charAt(8) + "" + holiday.getDate().charAt(9));
            final String hDescription = holiday.getDescription();
            int place = dayofmonth + my - 1;


            Button b = (Button) findViewById(res.getIdentifier("b" + (place), "id", getPackageName()));
            if (b != null) {
                b.setBackgroundColor(ContextCompat.getColor(context, R.color.colorHoliday));
                b.setTextColor(ContextCompat.getColor(context, R.color.colorSoftText));
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        /*
                        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                        dialog.setTitle("Holiday");
                        dialog.setMessage("" + hDescription);
                        dialog.setCancelable(true);
                        dialog.show();
                        */
                        final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                        alertDialog.setTitle("Holiday");
                        alertDialog.setMessage("" + hDescription);
                        alertDialog.setIcon(R.drawable.notification_holiday);
                        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Write your code here to execute after dialog closed
                                //Toast.makeText(getApplicationContext(), "You clicked on OK", Toast.LENGTH_SHORT).show();
                                alertDialog.cancel();
                            }
                        });
                        alertDialog.show();

                    }
                });
            }//if b!= null
        }//end for loop

    }//setupHolidays

    private void setupWeekend() {

        int dayofmonth = selectedDate.get(Calendar.DAY_OF_WEEK);
        //dayofmonth will skip blank days of first week
        //sun=1 mon=2 ... and sat = 7 but our first day is monday and should not be skipped
        //for this we need to make mon=0 and sun=7
        dayofmonth = dayofmonth == 1 ? 8 : dayofmonth;
        //so mon = 0 ... and sun = 6
        dayofmonth -= 1;

        int lastdate = selectedDate.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);


        int place, i;
        Resources res = getResources();
        for (i = 0; i < lastdate; i++) {
            place = dayofmonth + i;
            GregorianCalendar gc = new GregorianCalendar();
            gc.set(selectedDate.get(Calendar.YEAR), selectedDate.get(Calendar.MONTH), selectedDate.get(Calendar.DATE), 0, 0, 0);
            gc.set(Calendar.DATE, i + 1);
            if (gc.get(Calendar.DAY_OF_WEEK) == 1 || gc.get(Calendar.DAY_OF_WEEK) == 7) {
                Button b = (Button) findViewById(res.getIdentifier("b" + (place), "id", getPackageName()));

                if (b != null) {
                    b.setBackgroundColor(ContextCompat.getColor(context, R.color.colorGreyBackground));
                    b.setTextColor(ContextCompat.getColor(context, R.color.colorSoftText));
                    b.setOnClickListener(null);
                }
            }
        }

    }


    /*---------------------------------setInitialCalendar----------------------------------------*/
    public void setupInitialCalendar() {

        int dayofmonth = selectedDate.get(Calendar.DAY_OF_WEEK);
        //dayofmonth will skip blank days of first week
        //sun=1 mon=2 ... and sat = 7 but our first day is monday and should not be skipped
        //for this we need to make mon=0 and sun=7
        dayofmonth = dayofmonth == 1 ? 8 : dayofmonth;
        //so mon = 0 ... and sun = 6
        dayofmonth -= 2;

        int lastdate = selectedDate.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);

        //Log.d("mylog", "Last date of Required month = " + lastdate);
        //Log.d("mylog", "First day of month starts on = " + dayofmonth);

        int total = 0, count = 1, i;
        Resources res = getResources();

        //skip blank days of week
        for (i = total; i < dayofmonth; i++) {
            Button b = (Button) findViewById(res.getIdentifier("b" + (total + 1), "id", getPackageName()));
            //android says so :p
            if (b != null) {
                b.setText("");
                b.setBackgroundColor(ContextCompat.getColor(context, R.color.colorWhite));
                b.setOnClickListener(null);
            }
            total++;
        }//end 1st for loop

        //initialize normal days
        for (i = total; count <= lastdate; i++) {
            Button b = (Button) findViewById(res.getIdentifier("b" + (total + 1), "id", getPackageName()));
            String date = "" + count++;
            //android says so :p
            if (b != null) {
                b.setText(date);
                b.setBackgroundColor(ContextCompat.getColor(context, R.color.colorWhite));
                b.setTextColor(ContextCompat.getColor(context, R.color.colorSoftText));
                b.setOnClickListener(null);
            }
            total++;
        }//end 2nd for loop

        //skip left blank days
        for (i = 0; i < 42 - total; i++) {
            Button b = (Button) findViewById(res.getIdentifier("b" + (total + 1 + i), "id", getPackageName()));
            if (b != null) {
                b.setText("");
                b.setBackgroundColor(ContextCompat.getColor(context, R.color.colorWhite));
                b.setOnClickListener(null);
            }
        }//end 3rd for loop

    }//setInitialCalendar


    /*---------------------------------setupAttendance--------------------------------------------*/
    private void setupAttendance() {

        int lastdate = selectedDate.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);

        boolean[] done = new boolean[lastdate + 1];
        final String[] arrayDescription = new String[lastdate + 1];

        int x;
        for (x = 0; x < lastdate + 1; x++) {
            done[x] = false;
            arrayDescription[x] = "Unknown";
        }
        ArrayList<Attendance> attendanceList;
        Attendance attendance;
        attendanceList = dbHelper.getAttendanceForMonth(student.getId(), selectedDate);

        Resources res = getResources();
        int dayofmonth = selectedDate.get(Calendar.DAY_OF_WEEK);
        //dayofmonth will skip blank days of first week
        //sun=1 mon=2 ... and sat = 7 but our first day is monday and should not be skipped
        //for this we need to make mon=0 and sun=7
        dayofmonth = dayofmonth == 1 ? 8 : dayofmonth;
        //so mon = 0 ... and sun = 6
        dayofmonth -= 2;

        if (gcNow.get(Calendar.MONTH) == selectedDate.get(Calendar.MONTH) && gcNow.get(Calendar.YEAR) == selectedDate.get(Calendar.YEAR)) {
            lastdate = gcNow.get(Calendar.DATE);
        }

        //show absent if list is null
        if (attendanceList == null) {
            int place = dayofmonth;
            for (x = 1; x < lastdate + 1; x++) {
                place++;
                //MyUtils.logThis(x + " " + done[x]);
                final Button b = (Button) findViewById(res.getIdentifier("b" + (place), "id", getPackageName()));

                if (b != null) {
                    b.setBackgroundColor(ContextCompat.getColor(context, R.color.colorAbsent));

                    b.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
                    final int finalX = x;
                    b.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            /*final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                            alertDialog.setTitle("Absence Reason");
                            alertDialog.setMessage("" + arrayDescription[finalX]);
                            alertDialog.setIcon(R.drawable.notification_absent);
                            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Write your code here to execute after dialog closed
                                    //Toast.makeText(getApplicationContext(), "You clicked on OK", Toast.LENGTH_SHORT).show();
                                    alertDialog.dismiss();
                                }
                            });
                            alertDialog.show();*/
                            ShowUnknownAbsentDialog(b.getText().toString());

                        }
                    });
                }
            }//end for
            return;
        }//end if attendacelist is null

        //if attendence is there then
        int noOfAttendance = attendanceList.size();

        //loop runs through all attendance one by one
        for (int i = 0; i < noOfAttendance; i++) {

            attendance = attendanceList.get(i);

            //MyUtils.logThis("attendance" + i + "=" + attendance.toString());
            String hstatus = attendance.getStatus();
            String hdescription = attendance.getDesc();
            String hdate = attendance.getDate();

            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date inputDate;

            try {
                inputDate = fmt.parse(hdate);
            } catch (ParseException e) {
                e.printStackTrace();
                return;
            }
            GregorianCalendar cal = new GregorianCalendar();
            cal.setTime(inputDate);

            done[cal.get(Calendar.DATE)] = hstatus.equals("1");
            if (!done[cal.get(Calendar.DATE)]) {
                arrayDescription[cal.get(Calendar.DATE)] = hdescription;
            }
            //MyUtils.logThis("hdescription=" + hdescription);
        }//end of for loop

        int place = dayofmonth;
        for (x = 1; x < lastdate + 1; x++) {
            place++;
            //MyUtils.logThis(x + " " + done[x]);
            final Button b = (Button) findViewById(res.getIdentifier("b" + (place), "id", getPackageName()));
            if (done[x]) {
                if (b != null) {
                    b.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPresent));
                    b.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
                }
            } else {
                if (b != null) {
                    b.setBackgroundColor(ContextCompat.getColor(context, R.color.colorAbsent));
                    b.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
                    final int finalX = x;
                    finalDayofmonth = place;

                    b.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if (!arrayDescription[finalX].equals("Unknown")) {
                                final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                                alertDialog.setTitle("Absence Reason");
                                alertDialog.setMessage("" + arrayDescription[finalX]);
                                alertDialog.setIcon(R.drawable.notification_absent);
                                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // Write your code here to execute after dialog closed
                                        //Toast.makeText(getApplicationContext(), "You clicked on OK", Toast.LENGTH_SHORT).show();
                                        alertDialog.cancel();
                                    }
                                });
                                alertDialog.show();


                            }
                            //if reason is unknown
                            else {
                                ShowUnknownAbsentDialog(b.getText().toString());
                                /*----------------------------------------------------------------------*/
                                /*
                                final Dialog dialog = new Dialog(new ContextThemeWrapper(context,
                                        android.R.style.Theme_Holo_Light_Dialog_MinWidth));
                                dialog.setContentView(R.layout.dialog_absence_unknown);
                                dialog.setTitle("Absence reason");

                                View buttonRectification = dialog.findViewById(R.id.buttonRectification);
                                View buttonApplication = dialog.findViewById(R.id.buttonApplication);

                                buttonRectification.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.cancel();
                                        final Dialog dialog2 = new Dialog(new ContextThemeWrapper(context,
                                                android.R.style.Theme_Holo_Light_Dialog_MinWidth));
                                        dialog2.setContentView(R.layout.dialog_simple);
                                        dialog2.setTitle("Attendance Rectification");

                                        TextView textViewContent = (TextView) dialog2.findViewById(R.id.textViewContent);
                                        View buttonCancel = dialog2.findViewById(R.id.buttonCancel);
                                        View buttonSend = dialog2.findViewById(R.id.buttonSend);

                                        final Student student = localDB.getStudent();
                                        textViewContent.setText(student.getName() + " was present.\nPlease check and resolve his attendance");
                                        dialog2.show();
                                        buttonCancel.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                dialog2.cancel();
                                            }
                                        });
                                        buttonSend.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                dialog2.cancel();

                                                String guardianid = localDB.getStudent().getId();
                                                String studentid = localDB.getUser().getId();

                                                showProgress(true);

                                                ContentValues postParams = new ContentValues();
                                                postParams.put("guardianid", guardianid);
                                                postParams.put("studentid", studentid);
                                                postParams.put("message", "rectification for student "
                                                        + student.toString() + " on date " + b.getText() + "/"
                                                        + (selectedDate.get(Calendar.MONTH) + 1) + "/"
                                                        + selectedDate.get(Calendar.YEAR));

                                                String url = "http://www.studentaggregator.org/demo/requestquery.php";
                                                serverHelper = new ServerHelper(url, postParams, new GetUserCallback() {
                                                    @Override
                                                    public void done() {
                                                        String reply = serverHelper.getReply();
                                                        if (reply == null) {
                                                            String errorDescription = getResources().getString(R.string.no_internet_warning);
                                                            Log.d(LOG_TAG, "Services - errorDescription = " + errorDescription);
                                                            Toast.makeText(AttendanceActivity.this, errorDescription, Toast.LENGTH_LONG).show();
                                                            showProgress(false);
                                                            return;
                                                        }
                                                        parseReply(reply);
                                                        Toast.makeText(AttendanceActivity.this, R.string.message_sent, Toast.LENGTH_LONG).show();
                                                    }
                                                });
                                                serverHelper.execute((Void) null);
                                            }
                                        });
                                    }
                                });
                                buttonApplication.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.cancel();
                                        Intent i = new Intent(context, Absence.class);
                                        String date = b.getText().toString();
                                        i.putExtra("date", "" + date);
                                        i.putExtra("month", "" + (selectedDate.get(Calendar.MONTH) + 1));
                                        i.putExtra("year", "" + selectedDate.get(Calendar.YEAR));
                                        MyUtils.logThis("Attendance - " + date + "-" + selectedDate.get(Calendar.MONTH) + "-" + selectedDate.get(Calendar.YEAR));
                                        //Toast.makeText(context,""+ finalDayofmonth,Toast.LENGTH_LONG).show();


                                        startActivity(i);
                                    }
                                });

                                dialog.show();*/

                                /*xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx*/


                                /*----------------------------------------------------------------------*/
                            }//else
                        }
                    });
                }
            }
        }//end for
    }//setupAttendance()

    private void ShowUnknownAbsentDialog(final String touchDate) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle("Absence Reason");
        alertDialog.setMessage("Unknown");
        alertDialog.setIcon(R.drawable.notification_absent);

        alertDialog.setPositiveButton("Request Rectification", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                // Write your code here to invoke YES event
                //Toast.makeText(getApplicationContext(), "You clicked on YES", Toast.LENGTH_SHORT).show();
                dialog.cancel();
                String day = touchDate;
                //i.putExtra("date", "" + date);
                //i.putExtra("month", "" + (selectedDate.get(Calendar.MONTH) + 1));
                //i.putExtra("year", "" + selectedDate.get(Calendar.YEAR));
                if (day.length() == 1) {
                    day = "0" + day;
                }
                String month = (selectedDate.get(Calendar.MONTH) + 1) + "";
                if (month.length() == 1) {
                    month = "0" + month;
                }
                String year = selectedDate.get(Calendar.YEAR) + "";
                String date = year + "-" + month + "-" + day;

                AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(context);
                alertDialog2.setTitle("Attendance Rectification");

                String formattedDate = MyUtils.getFormattedDate(date);
                String message = student.getName() + " was present on " + formattedDate
                        + ".\nPlease check and resolve his attendance";
                alertDialog2.setMessage(message);
                alertDialog2.setIcon(R.drawable.notification_present);

                alertDialog2.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        // Write your code here to invoke YES event
                        //Toast.makeText(getApplicationContext(), "You clicked on YES", Toast.LENGTH_SHORT).show();
                        dialog.cancel();

                        String studentid = student.getId();

                        ContentValues postParams = new ContentValues();
                        postParams.put("studentid", studentid);
                        postParams.put("message", "rectification for student "
                                + " on date " + touchDate + "/"
                                + (selectedDate.get(Calendar.MONTH) + 1) + "/"
                                + selectedDate.get(Calendar.YEAR));

                        String URL = MyUtils.BASE_URL + "set_query.php";
                        //String URL = "http://www.studentaggregator.org/demo/requestquery.php";
                        serverHelper = new ServerHelper(URL, postParams, new GetUserCallback() {
                            @Override
                            public void done() {
                                hideProgressDialog();
                                String reply = serverHelper.getReply();
                                if (reply == null) {
                                    String errorDescription = getResources().getString(R.string.no_internet_warning);
                                    MyUtils.logThis("AttendanceActivity - errorDescription = " + errorDescription);
                                    Toast.makeText(AttendanceActivity.this, errorDescription, Toast.LENGTH_LONG).show();
                                    return;
                                }
                                parseReply(reply);
                            }
                        });
                        showProgressDialog();
                        serverHelper.execute((Void) null);

                    }
                });

                alertDialog2.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to invoke NO event
                        //Toast.makeText(getApplicationContext(), "You clicked on NO", Toast.LENGTH_SHORT).show();
                        dialog.cancel();
                    }
                });

                alertDialog2.show();
                //mmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmeaw

            }
        });
        alertDialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.setNegativeButton("Give Application", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                Intent i = new Intent(context, AbsenceReasonActivity.class);
                String day = touchDate;
                //i.putExtra("date", "" + date);
                //i.putExtra("month", "" + (selectedDate.get(Calendar.MONTH) + 1));
                //i.putExtra("year", "" + selectedDate.get(Calendar.YEAR));
                if (day.length() == 1) {
                    day = "0" + day;
                }
                String month = (selectedDate.get(Calendar.MONTH) + 1) + "";
                if (month.length() == 1) {
                    month = "0" + month;
                }
                String year = selectedDate.get(Calendar.YEAR) + "";
                String date = year + "-" + month + "-" + day;
                i.putExtra("date", date);
                //MyUtils.logThis("Attendance - " + date + "-" + selectedDate.get(Calendar.MONTH) + "-" + selectedDate.get(Calendar.YEAR));
                startActivity(i);
            }
        });

        alertDialog.show();
    }

    private void parseReply(String reply) {

        String errorDescription;
        final JSONParser parser = new JSONParser();

        Boolean success = parser.parseSuccess(reply);

        // Un-Successful parsing
        if (!success) {
            errorDescription = parser.getErrorDescription();
            MyUtils.logThis("AttendanceActivity - errorDescription = " + errorDescription);
            Toast.makeText(AttendanceActivity.this, errorDescription, Toast.LENGTH_LONG).show();
        }
        //Successful parsing
        else {
            MyUtils.logThis("AttendanceActivity - submission successful");
            Toast.makeText(AttendanceActivity.this, R.string.message_sent, Toast.LENGTH_LONG).show();
        }
    }//parseReply

    private void calculatePercentage() {

        int holidayWeekendCount = dbHelper.getHolidayCountOnWeekends(sessionStartDate, gcNow);
        //MyUtils.logThis("holidayWeekendCount = " + holidayWeekendCount);

        int total_holidays_till_now = dbHelper.getHolidayCountBetween(sessionStartDate, gcNow);
        //MyUtils.logThis("total_holidays_till_now = " + total_holidays_till_now);

        long total_weekdays_till_now = days(sessionStartDate, gcNow);
        //MyUtils.logThis("total_weekdays_between = " + total_weekdays_till_now);

        long working_days = total_weekdays_till_now - total_holidays_till_now + holidayWeekendCount;
        //MyUtils.logThis("working_days = " + working_days);

        int total_present = dbHelper.getTotalPresent(student.getId());
        //MyUtils.logThis("total_present = " + total_present);

        int percentage = (int) Math.round(((double) total_present / (double) working_days) * 100);
        //MyUtils.logThis("percentage = " + percentage);

        textViewPercentage.setText(percentage + "%");
        progressBar.setProgress(percentage);
    }

    static long days(GregorianCalendar c1, GregorianCalendar c2) {
        //Ignore argument check

        int w1 = c1.get(Calendar.DAY_OF_WEEK);
        c1.add(Calendar.DAY_OF_WEEK, -w1);

        int w2 = c2.get(Calendar.DAY_OF_WEEK);
        c2.add(Calendar.DAY_OF_WEEK, -w2);

        //end Saturday to start Saturday
        long days = (c2.getTimeInMillis() - c1.getTimeInMillis()) / (1000 * 60 * 60 * 24);
        long daysWithoutWeekendDays = days - (days * 2 / 7);

        // Adjust days to add on (w2) and days to subtract (w1) so that Saturday
        // and Sunday are not included
        if (w1 == Calendar.SUNDAY && w2 != Calendar.SATURDAY) {
            w1 = Calendar.MONDAY;
        } else if (w1 == Calendar.SATURDAY && w2 != Calendar.SUNDAY) {
            w1 = Calendar.FRIDAY;
        }

        if (w2 == Calendar.SUNDAY) {
            w2 = Calendar.MONDAY;
        } else if (w2 == Calendar.SATURDAY) {
            w2 = Calendar.FRIDAY;
        }

        return daysWithoutWeekendDays - w1 + w2;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            //int id = item.getItemId();
            //todo check other id also
            Intent i = new Intent(context, MenuActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        showCalendarForMonth();
    }

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

}//end Activity