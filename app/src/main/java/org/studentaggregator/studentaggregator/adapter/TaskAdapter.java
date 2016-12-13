package org.studentaggregator.studentaggregator.adapter;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.studentaggregator.studentaggregator.R;
import org.studentaggregator.studentaggregator.helper.MyUtils;
import org.studentaggregator.studentaggregator.model.Task;

import java.util.ArrayList;

public class TaskAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private LayoutInflater layoutInflater;
    ArrayList<Task> taskList;
    Context context;

    public TaskAdapter(Context context, ArrayList<Task> taskList) {
        layoutInflater = LayoutInflater.from(context);
        this.taskList = taskList;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.fragment_task, parent, false);
        return new TaskHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof TaskHolder) {
            TaskHolder taskHolder = (TaskHolder) holder;
            final Task task = taskList.get(position);
            //MyUtils.logThis("task is " + task.toString());

            String formattedDate = MyUtils.getFormattedDate(task.getDate());
            taskHolder.textViewDate.setText(formattedDate);
            taskHolder.textViewTitle.setText(task.getTitle());
            taskHolder.textViewDescription.setText(task.getDescription());
            taskHolder.buttonDownload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String url = MyUtils.BASE_URL + "task/" + context.getResources().getString(R.string.institute_folder)
                            + "/" + task.getFilename();
                    //http://192.168.43.50/studentaggregator/application/task/st_jhons/sample-paper.pdf
                    //MyUtils.logThis(url);
                    DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
                    request.setDescription("Downloading " + task.getFilename());
                    request.setTitle("Downloading " + task.getFilename());
                    // in order for this if to run, you must use the android 3.2 to compile your app
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                        request.allowScanningByMediaScanner();
                        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                    }
                    request.setDestinationInExternalPublicDir("studentaggregator", task.getFilename());

                    // get download service and enqueue file
                    DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
                    manager.enqueue(request);
                    Toast.makeText(context, "Download started", Toast.LENGTH_LONG).show();
                }
            });
        }
    }


    @Override
    public int getItemCount() {
        return taskList.size();
    }


    public class TaskHolder extends RecyclerView.ViewHolder {

        TextView textViewDate;
        TextView textViewTitle;
        TextView textViewDescription;
        Button buttonDownload;

        public TaskHolder(View itemView) {
            super(itemView);
            textViewDate = (TextView) itemView.findViewById(R.id.textViewDate);
            textViewTitle = (TextView) itemView.findViewById(R.id.textViewTitle);
            textViewDescription = (TextView) itemView.findViewById(R.id.textViewDescription);
            buttonDownload = (Button) itemView.findViewById(R.id.buttonDownload);
        }
    }
}