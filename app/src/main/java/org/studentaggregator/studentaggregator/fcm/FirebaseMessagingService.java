package org.studentaggregator.studentaggregator.fcm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;

import org.studentaggregator.studentaggregator.R;
import org.studentaggregator.studentaggregator.activity.AddStudentActivity;
import org.studentaggregator.studentaggregator.activity.FeeActivity;
import org.studentaggregator.studentaggregator.activity.NotificationActivity;
import org.studentaggregator.studentaggregator.activity.QueryActivity;
import org.studentaggregator.studentaggregator.activity.ResultActivity;
import org.studentaggregator.studentaggregator.activity.TaskActivity;
import org.studentaggregator.studentaggregator.helper.DBHelper;
import org.studentaggregator.studentaggregator.helper.JSONParser;
import org.studentaggregator.studentaggregator.helper.LocalDB;
import org.studentaggregator.studentaggregator.helper.MyUtils;
import org.studentaggregator.studentaggregator.model.Attendance;
import org.studentaggregator.studentaggregator.model.Event;
import org.studentaggregator.studentaggregator.model.Fee;
import org.studentaggregator.studentaggregator.model.Query;
import org.studentaggregator.studentaggregator.model.StudentResult;
import org.studentaggregator.studentaggregator.model.Task;
import com.google.firebase.messaging.RemoteMessage;

import java.util.ArrayList;

/**
 * Created by Ray on 29/08/2016.
 */
public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {

    protected static int NOTIFICATION_ID_ATTENDANCE = 1;
    protected static int NOTIFICATION_ID_FEE = 2;
    protected static int NOTIFICATION_ID_NEXT_FEE = 3;
    protected static int NOTIFICATION_ID_NOTIFICATION = 4;
    protected static int NOTIFICATION_ID_QUERY = 5;
    protected static int NOTIFICATION_ID_RESULT = 6;
    protected static int NOTIFICATION_ID_TASK = 7;


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        //showNotification(remoteMessage.getData().get("message"));
        String notificationType = remoteMessage.getData().get("type");
        String message = remoteMessage.getData().get("message");

        if (notificationType.equals("attendance")) {
            processAttendance(message);
        } else if (notificationType.equals("fee")) {
            processFee(message);
        } else if (notificationType.equals("nextfee")) {
            processNextFee(message);
        } else if (notificationType.equals("notification")) {
            processNotification(message);
        } else if (notificationType.equals("query")) {
            processQuery(message);
        } else if (notificationType.equals("result")) {
            processResult(message);
        } else if (notificationType.equals("task")) {
            processTask(message);
        } else {
            MyUtils.logThis("Invalid notification type");
        }
    }


    //----------------------------     Attendance     -------------------------------
    private void processAttendance(String message) {
        String errorDescription;
        final JSONParser parser = new JSONParser();

        ArrayList<Attendance> attendanceList = parser.parseAttendance(message);

        // Un-Successful parsing
        if (attendanceList == null) {
            errorDescription = parser.getErrorDescription();
            MyUtils.logThis("FirebaseMessagingService - JSONParser returned empty list - errorDescription = " + errorDescription);
            return;
        } else {
            showAttendanceNotification(attendanceList.get(0));
        }
    }//processAttendance

    private void showAttendanceNotification(Attendance attendance) {

        LocalDB localDB = new LocalDB(this);
        DBHelper dbHelper = new DBHelper(this);
        ArrayList<Attendance> attendanceList = new ArrayList<>();
        attendanceList.add(attendance);
        dbHelper.addAttendance(attendanceList);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);
        notificationBuilder.setSmallIcon(R.drawable.small_icon);
        notificationBuilder.setAutoCancel(true);
        notificationBuilder.setDefaults(Notification.DEFAULT_ALL);
        notificationBuilder.setContentTitle("Attendance");

        String status;
        Bitmap bm;
        if (attendance.getStatus().equals("1")) {
            status = "present";
            bm = BitmapFactory.decodeResource(getResources(), R.drawable.notification_present);
        } else {
            status = "absent";
            bm = BitmapFactory.decodeResource(getResources(), R.drawable.notification_absent);
        }
        String formattedDate = MyUtils.getFormattedDate(attendance.getDate());


        String studentName = localDB.getStudent().getName();
        notificationBuilder.setContentText(studentName + " was marked " + status + " on " + formattedDate);
        notificationBuilder.setLargeIcon(bm);
        notificationBuilder.setTicker(studentName + " was marked " + status);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, AddStudentActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
        notificationBuilder.setContentIntent(contentIntent);

        NotificationManager mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotifyMgr.notify(NOTIFICATION_ID_ATTENDANCE, notificationBuilder.build());
    }


    //----------------------------     Fee     -------------------------------
    private void processFee(String message) {
        String errorDescription;
        final JSONParser parser = new JSONParser();

        ArrayList<Fee> feeList = parser.parseFee(message);

        // Un-Successful parsing
        if (feeList == null) {
            errorDescription = parser.getErrorDescription();
            MyUtils.logThis("FirebaseMessagingService - JSONParser returned empty list - errorDescription = " + errorDescription);
            return;
        } else {
            showFeeNotification(feeList.get(0));
        }
    }//processFee

    private void showFeeNotification(Fee fee) {
        DBHelper dbHelper = new DBHelper(this);
        ArrayList<Fee> feeList = new ArrayList<>();
        feeList.add(fee);
        dbHelper.addFee(feeList);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);
        notificationBuilder.setSmallIcon(R.drawable.small_icon);
        notificationBuilder.setAutoCancel(true);
        notificationBuilder.setDefaults(Notification.DEFAULT_ALL);
        notificationBuilder.setContentTitle("Fee payment");

        String formattedDate = MyUtils.getFormattedDate(fee.getDate());
        notificationBuilder.setContentText(fee.getAmount() + " INR paid on " + formattedDate);
        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.notification_fee);
        notificationBuilder.setLargeIcon(bm);
        notificationBuilder.setTicker("Fee payment");

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, FeeActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
        notificationBuilder.setContentIntent(contentIntent);

        NotificationManager mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotifyMgr.notify(NOTIFICATION_ID_FEE, notificationBuilder.build());
    }//showFeeNotification


    //----------------------------     NextFee     -------------------------------
    private void processNextFee(String message) {
        String errorDescription;
        final JSONParser parser = new JSONParser();

        ArrayList<Fee> feeList = parser.parseFee(message);

        // Un-Successful parsing
        if (feeList == null) {
            errorDescription = parser.getErrorDescription();
            MyUtils.logThis("FirebaseMessagingService - JSONParser returned empty list - errorDescription = " + errorDescription);
            return;
        } else {
            showNextFeeNotification(feeList.get(0));
        }
    }//processNextFee

    private void showNextFeeNotification(Fee fee) {

        LocalDB localDB = new LocalDB(this);
        localDB.saveNextPayment(fee);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);
        notificationBuilder.setSmallIcon(R.drawable.small_icon);
        notificationBuilder.setAutoCancel(true);
        notificationBuilder.setDefaults(Notification.DEFAULT_ALL);
        notificationBuilder.setContentTitle("Pending fee");

        String formattedDate = MyUtils.getFormattedDate(fee.getDate());
        notificationBuilder.setContentText(fee.getAmount() + " INR with last submission date " + formattedDate);
        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.notification_fee);
        notificationBuilder.setLargeIcon(bm);
        notificationBuilder.setTicker("Pending fee");

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, FeeActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
        notificationBuilder.setContentIntent(contentIntent);

        NotificationManager mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotifyMgr.notify(NOTIFICATION_ID_NEXT_FEE, notificationBuilder.build());
    }//showNextFeeNotification


    //----------------------------     Notification     -------------------------------
    private void processNotification(String message) {
        String errorDescription;
        final JSONParser parser = new JSONParser();

        ArrayList<Event> eventList = parser.parseNotification(message);

        // Un-Successful parsing
        if (eventList == null) {
            errorDescription = parser.getErrorDescription();
            MyUtils.logThis("FirebaseMessagingService - JSONParser returned empty list - errorDescription = " + errorDescription);
            return;
        } else {
            showNotificationNotification(eventList.get(0));
        }
    }//processNotification

    private void showNotificationNotification(Event event) {

        DBHelper dbHelper = new DBHelper(this);
        ArrayList<Event> eventList = new ArrayList<>();
        eventList.add(event);
        dbHelper.addEvent(eventList);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);
        notificationBuilder.setSmallIcon(R.drawable.small_icon);
        notificationBuilder.setAutoCancel(true);
        notificationBuilder.setDefaults(Notification.DEFAULT_ALL);
        notificationBuilder.setContentTitle("Notification - " + event.getTitle());

        notificationBuilder.setContentText(event.getDescription());
        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.notification_event);
        notificationBuilder.setLargeIcon(bm);
        notificationBuilder.setTicker("Notification update");

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, NotificationActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
        notificationBuilder.setContentIntent(contentIntent);

        NotificationManager mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotifyMgr.notify(NOTIFICATION_ID_NOTIFICATION, notificationBuilder.build());
    }//showNotificationNotification


    //----------------------------     Query     -------------------------------
    private void processQuery(String message) {
        String errorDescription;
        final JSONParser parser = new JSONParser();

        ArrayList<Query> queryList = parser.parseQuery(message);

        // Un-Successful parsing
        if (queryList == null) {
            errorDescription = parser.getErrorDescription();
            MyUtils.logThis("FirebaseMessagingService - JSONParser returned empty list - errorDescription = " + errorDescription);
            return;
        } else {
            showQueryNotification(queryList.get(0));
        }
    }//processQuery

    private void showQueryNotification(Query query) {
        DBHelper dbHelper = new DBHelper(this);
        ArrayList<Query> queryList = new ArrayList<>();
        queryList.add(query);
        dbHelper.addQuery(queryList);


        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);
        notificationBuilder.setSmallIcon(R.drawable.small_icon);
        notificationBuilder.setAutoCancel(true);
        notificationBuilder.setDefaults(Notification.DEFAULT_ALL);
        notificationBuilder.setContentTitle("Query response");

        notificationBuilder.setContentText(query.getReply());
        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.notification_query);
        notificationBuilder.setLargeIcon(bm);
        notificationBuilder.setTicker("Query response");

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, QueryActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
        notificationBuilder.setContentIntent(contentIntent);

        NotificationManager mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotifyMgr.notify(NOTIFICATION_ID_QUERY, notificationBuilder.build());
    }//showQueryNotification


    //----------------------------     Result     -------------------------------
    private void processResult(String message) {
        String errorDescription;
        final JSONParser parser = new JSONParser();

        ArrayList<StudentResult> resultList = parser.parseStudentResults(message);

        // Un-Successful parsing
        if (resultList == null) {
            errorDescription = parser.getErrorDescription();
            MyUtils.logThis("FirebaseMessagingService - JSONParser returned empty list - errorDescription = " + errorDescription);
            return;
        } else {
            showResultNotification(resultList.get(0));
        }
    }//processResult

    private void showResultNotification(StudentResult studentResult) {

        DBHelper dbHelper = new DBHelper(this);
        ArrayList<StudentResult> list = new ArrayList<>();
        list.add(studentResult);
        dbHelper.addResult(list);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);
        notificationBuilder.setSmallIcon(R.drawable.small_icon);
        notificationBuilder.setAutoCancel(true);
        notificationBuilder.setDefaults(Notification.DEFAULT_ALL);
        notificationBuilder.setContentTitle("Result update");

        LocalDB localDB = new LocalDB(this);
        String studentName = localDB.getStudent().getName();
        notificationBuilder.setContentText("Scores for " + studentName);
        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.notification_result);
        notificationBuilder.setLargeIcon(bm);
        notificationBuilder.setTicker("Result update");

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, ResultActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
        notificationBuilder.setContentIntent(contentIntent);

        NotificationManager mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotifyMgr.notify(NOTIFICATION_ID_RESULT, notificationBuilder.build());
    }//showResultNotification


    //----------------------------     Task     -------------------------------
    private void processTask(String message) {
        String errorDescription;
        final JSONParser parser = new JSONParser();

        ArrayList<Task> taskList = parser.parseTask(message);

        // Un-Successful parsing
        if (taskList == null) {
            errorDescription = parser.getErrorDescription();
            MyUtils.logThis("FirebaseMessagingService - JSONParser returned empty list - errorDescription = " + errorDescription);
            return;
        } else {
            showTaskNotification(taskList.get(0));
        }
    }//processTask

    private void showTaskNotification(Task task) {

        LocalDB localDB = new LocalDB(this);
        DBHelper dbHelper = new DBHelper(this);
        ArrayList<Task> list = new ArrayList<>();
        list.add(task);
        dbHelper.addTask(list);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);
        notificationBuilder.setSmallIcon(R.drawable.small_icon);
        notificationBuilder.setAutoCancel(true);
        notificationBuilder.setDefaults(Notification.DEFAULT_ALL);
        notificationBuilder.setContentTitle("Task update");

        String studentName = localDB.getStudent().getName();
        notificationBuilder.setContentText("New task is available for " + studentName);
        //notificationBuilder.setContentText("New task is available");

        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.notification_task);
        notificationBuilder.setLargeIcon(bm);
        notificationBuilder.setTicker("Task update");

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, TaskActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
        notificationBuilder.setContentIntent(contentIntent);

        NotificationManager mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotifyMgr.notify(NOTIFICATION_ID_TASK, notificationBuilder.build());
    }//showTaskNotification


}