package org.studentaggregator.studentaggregator.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.studentaggregator.studentaggregator.R;
import org.studentaggregator.studentaggregator.helper.MyUtils;
import org.studentaggregator.studentaggregator.model.Event;

import java.util.ArrayList;

public class NotificationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private LayoutInflater layoutInflater;
    ArrayList<Event> eventList;
    Context context;

    public NotificationAdapter(Context context, ArrayList<Event> eventList) {
        layoutInflater = LayoutInflater.from(context);
        this.eventList = eventList;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.fragment_notification, parent, false);
        return new EventHolder(view);
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof EventHolder) {

            EventHolder eventHolder = (EventHolder) holder;
            Event event = eventList.get(position);

            String formattedDate = MyUtils.getFormattedDate(event.getDate());
            eventHolder.textViewDate.setText(formattedDate);
            eventHolder.textViewHeading.setText(event.getTitle());
            eventHolder.textViewDescription.setText(event.getDescription());

            int noOfImages = Integer.parseInt(event.getImages());
            if (noOfImages != 0) {
                NotificationImagesAdapter adapterEventImages = new NotificationImagesAdapter(context, event);
                eventHolder.recyclerEventImages.setAdapter(adapterEventImages);
                LinearLayoutManager manager = new LinearLayoutManager(layoutInflater.getContext());
                manager.setOrientation(LinearLayoutManager.HORIZONTAL);
                eventHolder.recyclerEventImages.setLayoutManager(manager);
            } else {
                eventHolder.recyclerEventImages.setVisibility(View.GONE);
            }
        }
    }//onBindViewHolder


    @Override
    public int getItemCount() {
        return eventList.size();
    }//getItemCount

    private class EventHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView textViewDate;
        TextView textViewHeading;
        TextView textViewDescription;
        RecyclerView recyclerEventImages;

        public EventHolder(View itemView) {
            super(itemView);

            textViewDate = (TextView) itemView.findViewById(R.id.textViewDate);
            textViewHeading = (TextView) itemView.findViewById(R.id.textViewHeading);
            textViewDescription = (TextView) itemView.findViewById(R.id.textViewDescription);
            recyclerEventImages = (RecyclerView) itemView.findViewById(R.id.recyclerEventImages);
        }

        @Override
        public void onClick(View v) {

        }
    }//EventHolder


}
