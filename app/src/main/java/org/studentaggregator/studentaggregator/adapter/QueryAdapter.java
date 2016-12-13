package org.studentaggregator.studentaggregator.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.studentaggregator.studentaggregator.R;
import org.studentaggregator.studentaggregator.helper.MyUtils;
import org.studentaggregator.studentaggregator.model.Query;

import java.util.ArrayList;

public class QueryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private LayoutInflater layoutInflater;
    ArrayList<Query> queryList;
    Context context;

    public QueryAdapter(Context context, ArrayList<Query> queryList) {
        layoutInflater = LayoutInflater.from(context);
        this.queryList = queryList;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.fragment_query, parent, false);
        return new QueryHolder(view);
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof QueryHolder) {

            QueryHolder eventHolder = (QueryHolder) holder;
            Query event = queryList.get(position);

            String formattedDate = MyUtils.getFormattedDate(event.getDate());
            eventHolder.textViewDate.setText(formattedDate);
            eventHolder.textViewMessage.setText(event.getMessage());
            if (!(event.getReply().equalsIgnoreCase("null") || event.getReply() == null || event.getReply().length() < 1)) {
                eventHolder.textViewStatus.setText("Reply");
                eventHolder.textViewReply.setText(event.getReply());
            }
        }
    }//onBindViewHolder


    @Override
    public int getItemCount() {
        return queryList.size();
    }//getItemCount

    private class QueryHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView textViewDate;
        TextView textViewMessage;
        TextView textViewStatus;
        TextView textViewReply;

        public QueryHolder(View itemView) {
            super(itemView);

            textViewDate = (TextView) itemView.findViewById(R.id.textViewDate);
            textViewMessage = (TextView) itemView.findViewById(R.id.textViewMessage);
            textViewStatus = (TextView) itemView.findViewById(R.id.textViewStatus);
            textViewReply = (TextView) itemView.findViewById(R.id.textViewReply);
        }

        @Override
        public void onClick(View v) {

        }
    }//QueryHolder


}
