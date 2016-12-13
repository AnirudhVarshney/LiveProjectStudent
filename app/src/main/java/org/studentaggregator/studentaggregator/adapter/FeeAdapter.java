package org.studentaggregator.studentaggregator.adapter;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.studentaggregator.studentaggregator.R;
import org.studentaggregator.studentaggregator.helper.MyUtils;
import org.studentaggregator.studentaggregator.model.Fee;

import java.util.ArrayList;


public class FeeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private LayoutInflater layoutInflater;
    ArrayList<Fee> feeList;
    Context context;

    private static String FILE_URL = "http://studentaggregator.org/demo/downloadfeerecipt.php";
    private static String FOLDER_NAME = "studentaggregator";
    private static String FILE_NAME = "Fee Recipt";

    public FeeAdapter(Context context, ArrayList<Fee> feeList) {
        layoutInflater = LayoutInflater.from(context);
        this.feeList = feeList;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.fragment_fee, parent, false);
        return new FeeHolder(view);
    }

    /*    studentid date amount description type  */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        int[] icons = {R.drawable.check, R.drawable.exclamation, R.drawable.exam, R.drawable.tour};

        if (holder instanceof FeeHolder) {
            FeeHolder schoolHolder = (FeeHolder) holder;
            final Fee fee = feeList.get(position);
            MyUtils.logThis("fee is " + fee.toString());

            String setAmount = "Paid Rs " + fee.getAmount();
            schoolHolder.textViewAmount.setText(setAmount);
            final String formattedDate = MyUtils.getFormattedDate(fee.getDate());

            String setDate = "on " + formattedDate;
            schoolHolder.textViewDate.setText(setDate);
            String online = fee.getType();

            if (online.equalsIgnoreCase("basic")) {
                schoolHolder.imageView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorGreen));
                schoolHolder.imageView.setImageResource(icons[0]);
            } else if (online.equalsIgnoreCase("tour")) {
                schoolHolder.imageView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorYellow));
                schoolHolder.imageView.setImageResource(icons[3]);
            } else if (online.equalsIgnoreCase("fine")) {
                schoolHolder.imageView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorRed));
                schoolHolder.imageView.setImageResource(icons[1]);
            } else if (online.equalsIgnoreCase("exam")) {
                schoolHolder.imageView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorYellow));
                schoolHolder.imageView.setImageResource(icons[2]);
            }

            schoolHolder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Log.d("mylog", "image clicked" + position);
                    // custom dialog
                    ShowAlertDialog(fee);
                }
            });
            schoolHolder.textViewDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Log.d("mylog", "image clicked" + position);
                    // custom dialog
                    ShowAlertDialog(fee);
                }
            });
            schoolHolder.textViewAmount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Log.d("mylog", "image clicked" + position);
                    // custom dialog
                    ShowAlertDialog(fee);
                }
            });

            schoolHolder.imageButtonDownload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (MyUtils.hasActiveInternetConnection(context)) {

                        //download file
                        Toast.makeText(context, "Downloading started", Toast.LENGTH_SHORT).show();
                        String url = MyUtils.BASE_URL + "get_fee_receipt.php";
                        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
                        request.setDescription("Downloading Fee receipt");
                        request.setTitle("Downloading Fee receipt");
                        // in order for this if to run, you must use the android 3.2 to compile your app
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                            request.allowScanningByMediaScanner();
                            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                        }
                        request.setDestinationInExternalPublicDir("studentaggregator/", FILE_NAME + " - " + formattedDate + ".pdf");

                        // get download service and enqueue file
                        DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
                        manager.enqueue(request);
                        Toast.makeText(context, "Downloading started", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(context, R.string.no_internet_warning, Toast.LENGTH_LONG).show();
                    }
                }
            });

        }
    }


    @Override
    public int getItemCount() {
        return feeList.size();
    }

    private void ShowAlertDialog(Fee fee) {

        String description = fee.getDescription();
        String[] desc = description.split("[,]");
        int size = desc.length;
        String dialogMessage = "";
        //int total = 0;
        for (int i = 0; i < size; i++) {
            String[] data = desc[i].split("[:]");
            String name = data[0].toUpperCase();
            String value = data[1].toUpperCase();

            name = String.format("%-20s", name);
            value = String.format("%-10s", value);

            dialogMessage += name + value + "\n";
        }

        //Generate views to pass to AlertDialog.Builder and to set the text
        View dlg;
        TextView tvText;
        try {
            //Inflate the custom view
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //LayoutInflater inflater = LayoutInflater.from(context);
            dlg = inflater.inflate(R.layout.dialog_mono, null);
            //dlg = inflater.inflate(R.layout.dialog_mono, (ViewGroup) findViewById(R.id.dlgView));
            tvText = (TextView) dlg.findViewById(R.id.dlgText);
        } catch (InflateException e) {
            dlg = tvText = new TextView(context);
        }
        //Set the text
        tvText.setText(dialogMessage);
        //Build and show the dialog
        new AlertDialog.Builder(context)
                .setTitle("Fee Details")
                .setCancelable(true)
                .setIcon(R.drawable.notification_fee)
                .setPositiveButton("OK", null)
                .setView(dlg)
                .show();    //Builder method returns allow for method chaining
    }

    public class FeeHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView textViewAmount;
        TextView textViewDate;
        ImageView imageView;
        ImageButton imageButtonDownload;

        public FeeHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            textViewAmount = (TextView) itemView.findViewById(R.id.textViewPaid);
            textViewDate = (TextView) itemView.findViewById(R.id.textViewDate);
            imageView = (ImageView) itemView.findViewById(R.id.imageViewPayment);
            imageButtonDownload = (ImageButton) itemView.findViewById(R.id.imageButtonDownload);

        }

        @Override
        public void onClick(View v) {
            //recyclerListner.onRecyclerClick(getAdapterPosition());
            Log.d("mylog", "position clicked");
            //Toast.makeText(SchoolHolder.this,"pos", Toast.LENGTH_SHORT).show();
        }
    }

}