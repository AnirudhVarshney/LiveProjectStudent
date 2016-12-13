package org.studentaggregator.studentaggregator.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import org.studentaggregator.studentaggregator.R;
import org.studentaggregator.studentaggregator.helper.LocalDB;
import org.studentaggregator.studentaggregator.helper.MyUtils;
import org.studentaggregator.studentaggregator.model.Student;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

public class SplashActivity extends AppCompatActivity {


    private static final int SPLASH_TIME_OUT = 4 * 1000;
    Context context;
    LocalDB localDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        context = SplashActivity.this;
        localDB = new LocalDB(context);

        //localDB.clear();
        //DBHelper dbHelper = new DBHelper(context);
        //dbHelper.clear();

        FirebaseMessaging.getInstance().subscribeToTopic("Test");
        FirebaseInstanceId.getInstance().getToken();

        //Intent i = new Intent(SplashActivity.this, TestActivity.class);
        //startActivity(i);
        //finish();

        splashAndGoNext();
    }


    public void splashAndGoNext() {
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {

                Intent i;
                //App is not initializes
                if (!localDB.getInitialised()) {
                    localDB.setStudent(new Student(null, null, null, null, null, null, null, null, null, null));
                    //todo below
                    //new DBHelper(context).clear();
                    i = new Intent(context, AddStudentActivity.class);
                }
                //All good user can proceed
                else {
                    MyUtils.logThis("SplashScreen - App is initialised");
                    i = new Intent(context, MenuActivity.class);
                }

                startActivity(i);
                finish();
            }
        }, SPLASH_TIME_OUT);
    }


}
