package org.studentaggregator.studentaggregator.alarm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;

import org.studentaggregator.studentaggregator.R;
import org.studentaggregator.studentaggregator.activity.FeeActivity;
import org.studentaggregator.studentaggregator.helper.LocalDB;
import org.studentaggregator.studentaggregator.helper.MyUtils;
import org.studentaggregator.studentaggregator.model.Fee;
import org.studentaggregator.studentaggregator.model.Student;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class AlarmReceiver extends BroadcastReceiver {

    private LocalDB localDB;
    private Context context;
    protected static int NOTIFICATION_ID_NEXT_FEE = 3;
    private Fee fee;
    private Student student;


    @Override
    public void onReceive(Context context, Intent intent) {
        //Toast.makeText(context, "Alarmmmmmmed", Toast.LENGTH_LONG).show();
        //Log.d("mylog", "Triggered");
        this.context = context;
        localDB = new LocalDB(context);
        student = localDB.getStudent();

        fee = localDB.getNextPayment(student.getId());

        if (fee==null){
            return;
        }


        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date inputDate;
        Date todaysDate = new Date();
        String dueTime = fee.getDate();

        try {
            inputDate = fmt.parse(dueTime);
        } catch (ParseException e) {
            e.printStackTrace();
            return;
        }

        inputDate.setDate(inputDate.getDate() - 4);
        //Log.d(MyUtils.LOG_TAG, "Alarm receiver - inputDate=" + inputDate);
        //Log.d(MyUtils.LOG_TAG, "Alarm receiver - todaysDate=" + todaysDate);

        if (todaysDate.compareTo(inputDate) > 0) {
            showNextFeeNotification();
        }
    }


    private void showNextFeeNotification() {
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context);
        notificationBuilder.setSmallIcon(R.drawable.small_icon);
        notificationBuilder.setAutoCancel(true);
        notificationBuilder.setDefaults(Notification.DEFAULT_ALL);
        notificationBuilder.setContentTitle("Pending fee");

        String formattedDate = MyUtils.getFormattedDate(fee.getDate());
        notificationBuilder.setContentText(student.getName() + " fee is Due : Amount = " + fee.getAmount() + " INR");
        Bitmap bm = BitmapFactory.decodeResource(context.getResources(), R.drawable.notification_fee);
        notificationBuilder.setLargeIcon(bm);
        notificationBuilder.setTicker("Pending fee");

        PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
                new Intent(context, FeeActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
        notificationBuilder.setContentIntent(contentIntent);

        NotificationManager mNotifyMgr = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        mNotifyMgr.notify(NOTIFICATION_ID_NEXT_FEE, notificationBuilder.build());
    }//showNextFeeNotification

}
