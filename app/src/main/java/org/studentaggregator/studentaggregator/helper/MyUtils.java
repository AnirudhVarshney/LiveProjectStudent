package org.studentaggregator.studentaggregator.helper;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Created by Ray on 06-Jun-16.
 */
public class MyUtils {
    public static String LOG_TAG = "mylog";
    public static String BASE_URL = "http://www.studentaggregator.org/application/";

    public static boolean hasActiveInternetConnection(Context con) {

        ConnectivityManager connMgr = (ConnectivityManager) con.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            Log.d(LOG_TAG, "ServerUtilities - Connected to Internet");
            return true;
        } else {
            Log.d(LOG_TAG, "ServerUtilities - Not Connected to Internet");
            return false;
        }

    }

    public static String getFormattedDate(String date) {

        int year = Integer.parseInt(date.substring(0, 4));
        int month = Integer.parseInt(date.substring(5, 7));
        int day = Integer.parseInt(date.substring(8, 10));

        Date d = new Date();
        d.setDate(day);
        d.setMonth(month - 1);
        d.setYear(year);

        SimpleDateFormat dateFormat = new SimpleDateFormat("d MMM", Locale.getDefault());
        String formattedDate = dateFormat.format(d);
        return formattedDate + " " + year;
    }


    public static void logThis(String message) {
        Log.d(LOG_TAG, message);
    }


    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    public static GregorianCalendar getGregorianCalendar(String yyyyddmm) {

        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date inputDate;

        try {
            inputDate = fmt.parse(yyyyddmm);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }

        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.setTime(inputDate);

        return gregorianCalendar;
    }


    public static String getDate(GregorianCalendar gregorianCalendar) {
        String day = gregorianCalendar.get(Calendar.DATE) + "";
        String month = (gregorianCalendar.get(Calendar.MONTH) + 1) + "";
        String year = gregorianCalendar.get(Calendar.YEAR) + "";

        if (month.length() == 1) {
            month = "0" + month;
        }
        if (day.length() == 1) {
            day = "0" + day;
        }
        return year + "-" + month + "-" + day;
    }
}
