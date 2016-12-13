package org.studentaggregator.studentaggregator.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.studentaggregator.studentaggregator.R;
import org.studentaggregator.studentaggregator.helper.MyUtils;
import org.studentaggregator.studentaggregator.model.StudentResult;

import java.util.ArrayList;


public class AdapterResults extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private LayoutInflater lauoutInflater;
    ArrayList<StudentResult> resultList;
    Context con;

    public AdapterResults(Context context, ArrayList<StudentResult> list) {

        lauoutInflater = LayoutInflater.from(context);
        con = context;
        resultList = list;
        Log.d("ani", resultList.size() + "in adapter results");
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = lauoutInflater.inflate(R.layout.fragment_result, parent, false);
        return new ResultHolder(view);
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ResultHolder resultHolder = (ResultHolder) holder;

        resultHolder.Title.setText(resultList.get(position).getTitle());

        String formattedDate = MyUtils.getFormattedDate(resultList.get(position).getDate());
        resultHolder.Date.setText(formattedDate);
        resultHolder.Description.setText(resultList.get(position).getDescription());

        ResultScoreAdapter mAdapter = new ResultScoreAdapter(con, resultList.get(position).getData());
        resultHolder.recyclerViewchild.setAdapter(mAdapter);
        LinearLayoutManager manager = new LinearLayoutManager(lauoutInflater.getContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        resultHolder.recyclerViewchild.setLayoutManager(manager);
    }

    @Override
    public int getItemCount() {
        return resultList.size();
    }

    private class ResultHolder extends RecyclerView.ViewHolder {


        TextView Title;
        TextView Description;
        TextView Date;
        RecyclerView recyclerViewchild;

        public ResultHolder(View itemView) {
            super(itemView);

            recyclerViewchild = (RecyclerView) itemView.findViewById(R.id.rv_resultscores);
            Title = (TextView) itemView.findViewById(R.id.resulttitle);
            Description = (TextView) itemView.findViewById(R.id.resultDesc);
            Date = (TextView) itemView.findViewById(R.id.resultdate);

        }
    }
}
