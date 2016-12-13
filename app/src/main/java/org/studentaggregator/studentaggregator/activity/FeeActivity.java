package org.studentaggregator.studentaggregator.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.studentaggregator.studentaggregator.R;
import org.studentaggregator.studentaggregator.adapter.FeeAdapter;
import org.studentaggregator.studentaggregator.helper.DBHelper;
import org.studentaggregator.studentaggregator.helper.LocalDB;
import org.studentaggregator.studentaggregator.helper.MyUtils;
import org.studentaggregator.studentaggregator.model.Fee;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class FeeActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    FeeAdapter feeAdapter;
    DBHelper dbHelper;
    ArrayList<Fee> feeList;
    LinearLayout footer;

    TextView textViewDueDate;
    TextView textViewAmount;
    TextView textViewEmpty;
    LocalDB localDB;
    Context context = this;
    Fee nextFee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fee);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        bindUIComponents();

        localDB = new LocalDB(context);
        dbHelper = new DBHelper(this);
        nextFee = localDB.getNextPayment(localDB.getStudent().getId());

        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);

        feeList = dbHelper.getAllFee(localDB.getStudent().getId());
        if (feeList == null) {
            textViewEmpty.setVisibility(View.VISIBLE);
        } else {
            feeAdapter = new FeeAdapter(context, feeList);
            recyclerView.setAdapter(feeAdapter);
        }
        if (nextFee != null) {
            footer.setVisibility(View.VISIBLE);
            setFooterText();
            checkNextPayment();
        }
        setOnClickListeners();
    }

    void bindUIComponents() {
        recyclerView = (RecyclerView) findViewById(R.id.rv_recycler);
        textViewEmpty = (TextView) findViewById(R.id.textViewEmpty);
        footer = (LinearLayout) findViewById(R.id.footer);
        textViewDueDate = (TextView) findViewById(R.id.textViewDueDate);
        textViewAmount = (TextView) findViewById(R.id.textViewAmount);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            //int id = item.getItemId();
            Intent i = new Intent(getApplicationContext(), MenuActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            finish();
        }
        return true;
    }

    private void setFooterText() {
        String formattedDate = MyUtils.getFormattedDate(nextFee.getDate());
        String dueDate = "Due Date : " + formattedDate;
        String dueAmount = "Due Amount : " + nextFee.getAmount() + " INR";
        textViewDueDate.setText(dueDate);
        textViewAmount.setText(dueAmount);

    }

    private void checkNextPayment() {
        String dueDate = nextFee.getDate();

        GregorianCalendar inputDate = MyUtils.getGregorianCalendar(dueDate);
        //  add 7 days to input date
        //  7 days margin for next payment
        assert inputDate != null;
        inputDate.add(Calendar.DATE, 7);
        GregorianCalendar todaysDate = new GregorianCalendar();

        if (todaysDate.compareTo(inputDate) == -1) {
            MyUtils.logThis("-1");
            footer.setBackgroundColor(ContextCompat.getColor(FeeActivity.this, R.color.colorAbsent));
        }
    }

    private void ShowAlert(String message) {
        //Generate views to pass to AlertDialog.Builder and to set the text
        View dlg;
        TextView tvText;
        try {
            //Inflate the custom view
            LayoutInflater inflater = getLayoutInflater();
            dlg = inflater.inflate(R.layout.dialog_mono, (ViewGroup) findViewById(R.id.dlgView));
            tvText = (TextView) dlg.findViewById(R.id.dlgText);
        } catch (InflateException e) {
            dlg = tvText = new TextView(context);
        }
        //Set the text
        tvText.setText(message);
        //Build and show the dialog
        new AlertDialog.Builder(context)
                .setTitle("Next fee details")
                .setCancelable(true)
                .setIcon(R.drawable.notification_fee)
                .setPositiveButton("Later", null)
                .setNegativeButton("Pay now", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent i = new Intent(context, PaymentActivity.class);
                        startActivity(i);
                    }
                })
                .setView(dlg)
                .show();    //Builder method returns allow for method chaining
    }

    private void setOnClickListeners() {
        footer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String description = nextFee.getDescription();
                String[] desc = description.split("[,]");

                String dialogMessage = "";
                //int total = 0;
                for (String aDesc : desc) {
                    String[] data = aDesc.split("[:]");
                    String name = data[0].toUpperCase();
                    String value = data[1].toUpperCase();

                    name = String.format("%-20s", name);
                    value = String.format("%-10s", value);

                    dialogMessage += name + value + "\n";
                }
                ShowAlert(dialogMessage);

                /*
                final Dialog dialog = new Dialog(new ContextThemeWrapper(con,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth));

                dialog.setContentView(R.layout.dialog_simple);
                dialog.setTitle("Fee Details");

                TextView textViewContent = (TextView) dialog.findViewById(R.id.textViewContent);

                String s = nextFee.getDescription();

                Log.d(LOG_TAG, "description is " + s);
                String[] desc = s.split("[,]");
                int size = desc.length;
                Log.d(LOG_TAG, "desc size = " + size);

                String dialogString = "";
                //int total = 0;
                for (int i = 0; i < size; i++) {
                    String[] data = desc[i].split("[:]");
                    String name = data[0];
                    String value = data[1];

                    dialogString += name + "\t\t\t\t\t\t\t" + value + "\n";

                    Log.d(LOG_TAG, "desc[" + i + "] = " + name + "=" + value);
                }
                textViewContent.setText(dialogString);


                Button buttonokay = (Button) dialog.findViewById(R.id.buttonCancel);
                Button buttonSend = (Button) dialog.findViewById(R.id.buttonSend);
                buttonSend.setText("Pay now");
                buttonSend.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        Intent i = new Intent(ActivityFee.this, Payment.class);
                        startActivity(i);
                    }
                });
                buttonokay.setText("Ok");
                buttonokay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
                */

            }
        });
    }


}
