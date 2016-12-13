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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.studentaggregator.studentaggregator.R;
import org.studentaggregator.studentaggregator.activity.VideoPlayerActivity;
import org.studentaggregator.studentaggregator.model.Video;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

//Created by RAY on 12-06-2016.

public class VideoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private LayoutInflater layoutInflater;
    private ArrayList<Video> videoList;
    private Context context;

    public VideoAdapter(Context context, ArrayList<Video> videoList) {
        layoutInflater = LayoutInflater.from(context);
        this.videoList = videoList;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.fragment_video, parent, false);
        return new VideoHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof VideoHolder) {

            final VideoHolder videoHolder = (VideoHolder) holder;
            final Video video = videoList.get(position);

            videoHolder.textViewTitle.setText(video.getTitle());
            videoHolder.textViewDescription.setText(video.getDescription());

            final String id = video.getId();
            String imageurl = "http://img.youtube.com/vi/" + id + "/0.jpg";

            Uri uri = Uri.parse(imageurl);

            Picasso.Builder builder = new Picasso.Builder(context);
            builder.listener(new Picasso.Listener() {
                @Override
                public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                    exception.printStackTrace();
                    Picasso.with(context).load(R.drawable.error).networkPolicy(NetworkPolicy.OFFLINE)
                            .into(videoHolder.imageViewVideo);
                }
            });
            builder.build().load(uri).into(videoHolder.imageViewVideo, new Callback() {
                @Override
                public void onSuccess() {
                    videoHolder.spinner.setVisibility(View.GONE);
                }

                @Override
                public void onError() {
                    videoHolder.spinner.setVisibility(View.GONE);
                    Picasso.with(context).load(R.drawable.error).into(videoHolder.imageViewVideo);
                }
            });
/*
            Glide.with(con).load(imageurl).centerCrop().listener((new RequestListener<String, GlideDrawable>() {
                @Override
                public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                    return false;
                }

                @Override
                public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                    videoHolder.spinner.setVisibility(View.GONE);
                    return false;
                }

            })).crossFade().into(videoHolder.imageViewVideo);
*/

            videoHolder.linearLayoutVideo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, VideoPlayerActivity.class);
                    i.putExtra("id", id);
                    context.startActivity(i);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }

    public class VideoHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        LinearLayout linearLayoutVideo;
        ImageView imageViewVideo;
        TextView textViewTitle;
        TextView textViewDescription;
        ProgressBar spinner;

        public VideoHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            linearLayoutVideo = (LinearLayout) itemView.findViewById(R.id.linearLayoutVideo);
            textViewTitle = (TextView) itemView.findViewById(R.id.textViewTitle);
            textViewDescription = (TextView) itemView.findViewById(R.id.textViewDescription);
            imageViewVideo = (ImageView) itemView.findViewById(R.id.imageViewVideo);
            spinner = (ProgressBar) itemView.findViewById(R.id.progressBarLoadImage);
        }

        @Override
        public void onClick(View v) {
            Log.d("mylog", "position clicked");
        }
    }
}
