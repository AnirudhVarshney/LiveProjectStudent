package org.studentaggregator.studentaggregator.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.studentaggregator.studentaggregator.R;
import org.studentaggregator.studentaggregator.activity.FullscreenImageActivity;
import org.studentaggregator.studentaggregator.helper.CircleTransform;
import org.studentaggregator.studentaggregator.helper.MyUtils;
import org.studentaggregator.studentaggregator.model.Event;

public class NotificationImagesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private LayoutInflater layoutInflater;
    Context context;

    String[] arrayImageUrl;
    View view;
    Event event;
    int noOfImages;


    public NotificationImagesAdapter(Context context, Event event) {
        layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.event = event;
        noOfImages = Integer.parseInt(event.getImages());
        arrayImageUrl = new String[noOfImages];
        String folder = context.getResources().getString(R.string.institute_folder);
        for (int i = 0; i < noOfImages; i++) {
            arrayImageUrl[i] = MyUtils.BASE_URL + "events/" + folder + "/" + event.getId() + "_" + (i + 1) + ".png";
            //arrayImageUrl[i] = "http://www.studentaggregator.org/events/" + event.getId() + "_" + (i + 1) + ".png";
            MyUtils.logThis(arrayImageUrl[i]);
        }
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = layoutInflater.inflate(R.layout.fragment_notification_image, parent, false);
        return new EventHolder(view);

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof EventHolder) {
            final EventHolder eventHolder = (EventHolder) holder;
/*
            Glide.with(con).load(arrayImageUrl[position]).centerCrop().listener((new RequestListener<String, GlideDrawable>() {
                @Override
                public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                    return false;
                }

                @Override
                public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                    eventHolder.spinner.setVisibility(View.GONE);
                    return false;
                }
            })).crossFade().into(eventHolder.imageView);
            eventHolder.imageView.setOnClickListener(new OnImageClickListener(event.getId(), position));
*/
            MyUtils.logThis(arrayImageUrl[position]);
            Uri uri = Uri.parse(arrayImageUrl[position]);

            Picasso.Builder builder = new Picasso.Builder(context);
            builder.listener(new Picasso.Listener() {
                @Override
                public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                    exception.printStackTrace();
                    Picasso.with(context).load(R.drawable.error).transform(new CircleTransform()).into(eventHolder.imageView);
                    eventHolder.spinner.setVisibility(View.GONE);
                }
            });
            builder.build().load(arrayImageUrl[position]).into(eventHolder.imageView, new Callback() {
                @Override
                public void onSuccess() {
                    eventHolder.spinner.setVisibility(View.GONE);
                }

                @Override
                public void onError() {
                    Picasso.with(context).load(R.drawable.error).transform(new CircleTransform()).into(eventHolder.imageView);
                    eventHolder.spinner.setVisibility(View.GONE);
                }
            });

            //set Click Listener on this image
            eventHolder.imageView.setOnClickListener(new OnImageClickListener(event.getId(), position));

            /*Picasso.with(context).load(uri).networkPolicy(NetworkPolicy.OFFLINE).
                    into(eventHolder.imageView, new Callback() {
                        @Override
                        public void onSuccess() {
                        }

                        @Override
                        public void onError() {
                            Picasso.with(context).load(R.drawable.error).transform(new CircleTransform()).into(eventHolder.imageView);
                            eventHolder.spinner.setVisibility(View.GONE);
                        }
                    });
        */
        }

    }

    @Override
    public int getItemCount() {
        return Integer.parseInt(event.getImages());
    }


    private class EventHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageView;
        ProgressBar spinner;

        public EventHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.event_image);
            spinner = (ProgressBar) itemView.findViewById(R.id.progressBar);
        }

        @Override
        public void onClick(View v) {
        }

    }

    class OnImageClickListener implements View.OnClickListener {
        int position;
        String id;

        // constructor
        public OnImageClickListener(String id, int position) {
            this.position = position;
            this.id = id;
        }

        @Override
        public void onClick(View v) {
            Log.d("mylog", "AdapterEventImages - image position = " + position);

            Intent i = new Intent(context, FullscreenImageActivity.class);
            i.putExtra("Id", id);
            i.putExtra("position", position);
            i.putExtra("noOfImages", "" + noOfImages);
            context.startActivity(i);
        }
    }

}
