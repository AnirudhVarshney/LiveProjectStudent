package org.studentaggregator.studentaggregator.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.studentaggregator.studentaggregator.R;

import java.util.ArrayList;


public class ResultScoreAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private LayoutInflater minflater;

    Context con;
    String Data;

    public ResultScoreAdapter(Context context, String data) {
        minflater = LayoutInflater.from(context);
        con = context;
        Data = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = minflater.inflate(R.layout.fragment_result_score, parent, false);
        return new ScoreHolder(view);
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ScoreHolder scoreHolder = (ScoreHolder) holder;
        String[] descrip = Data.split("[,]");
        ArrayList<String> subject = new ArrayList<>();
        ArrayList<String> score = new ArrayList<>();
        //Log.d("ani", "desc size = " + size);
        for (String aDescrip : descrip) {
            String[] data = aDescrip.split("[:]");
            String s1 = data[0];
            String s2 = data[1];
            subject.add(s1);
            score.add(s2);
        }
        scoreHolder.Subject.setText(subject.get(position));
        scoreHolder.Score.setText(score.get(position));

    }


    @Override
    public int getItemCount() {
        String[] descrip = Data.split("[,]");
        int size = descrip.length;
        return size;
    }

    private class ScoreHolder extends RecyclerView.ViewHolder {


        TextView Subject;
        TextView Score;


        public ScoreHolder(View itemView) {
            super(itemView);
            Subject = (TextView) itemView.findViewById(R.id.subject);
            Score = (TextView) itemView.findViewById(R.id.score);

        }
    }
}
