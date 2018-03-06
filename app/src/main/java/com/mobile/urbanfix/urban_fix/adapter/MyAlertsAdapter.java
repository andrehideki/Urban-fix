package com.mobile.urbanfix.urban_fix.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mobile.urbanfix.urban_fix.R;
import com.mobile.urbanfix.urban_fix.model.Problem;

import java.util.ArrayList;

public class MyAlertsAdapter extends RecyclerView.Adapter<MyAlertsAdapter.MyAlertsViewHolder> {

    private Context context;
    private ArrayList<Problem> myAlertsList;

    public MyAlertsAdapter(Context context, ArrayList<Problem> myAlertsList) {
        this.context = context;
        this.myAlertsList = myAlertsList;
    }

    public static class MyAlertsViewHolder extends RecyclerView.ViewHolder {
        TextView dateTextView, kindOfProblemTextView;
        public MyAlertsViewHolder(View itemView) {
            super(itemView);
            dateTextView = itemView.findViewById(R.id.datetextView);
            kindOfProblemTextView = itemView.findViewById(R.id.kindOfProblemTextView);
        }
    }


    @Override
    public MyAlertsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(this.context).inflate(R.layout.list_item_problem, parent,
                false);
        MyAlertsViewHolder holder = new MyAlertsViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyAlertsViewHolder holder, int position) {
        Problem problem = this.myAlertsList.get(position);
        Log.i("Script", "Inflando a View");
        holder.dateTextView.setText(problem.getDate());
        holder.kindOfProblemTextView.setText(problem.getKindOfProblem());
    }


    @Override
    public int getItemCount() {
        return this.myAlertsList.size();
    }

}
