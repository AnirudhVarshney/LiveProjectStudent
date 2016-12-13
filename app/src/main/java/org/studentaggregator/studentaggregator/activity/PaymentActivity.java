package org.studentaggregator.studentaggregator.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.studentaggregator.studentaggregator.R;
import org.studentaggregator.studentaggregator.model.Fee;


public class PaymentActivity extends AppCompatActivity {

    Button buttonPayNow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        buttonPayNow = (Button) findViewById(R.id.buttonPayNow);

        buttonPayNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PaymentActivity.this, "Only for demo purpose", Toast.LENGTH_LONG).show();
                finish();
            }
        });

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            //int id = item.getItemId();
            Intent i = new Intent(PaymentActivity.this, Fee.class);
            startActivity(i);
            finish();
        }
        return true;
    }
}
