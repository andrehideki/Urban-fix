package com.mobile.urbanfix.urban_fix.adapter;


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

    private ArrayList<Problem> myAlertsList;

    public MyAlertsAdapter(ArrayList<Problem> myAlertsList) {
        this.myAlertsList = myAlertsList;
    }

    public static class MyAlertsViewHolder extends RecyclerView.ViewHolder {
        TextView dateTextView, kindOfProblemTextView, statusTextView, locationTextView,
        descriptionTextView;
        public MyAlertsViewHolder(View itemView) {
            super(itemView);
            dateTextView = itemView.findViewById(R.id.datetextView);
            kindOfProblemTextView = itemView.findViewById(R.id.kindOfProblemTextView);
            statusTextView = itemView.findViewById(R.id.statusTextView);
            locationTextView = itemView.findViewById(R.id.addressTextView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
        }
    }


    @Override
    public MyAlertsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_problem, parent,
                false);
        return new MyAlertsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyAlertsViewHolder holder, int position) {
        Problem problem = this.myAlertsList.get(position);
        Log.i("Script", "Inflando a View");
        holder.dateTextView.setText(problem.getDate());
        holder.kindOfProblemTextView.setText(problem.getKindOfProblem());
        holder.statusTextView.setText(problem.getStatus());
        holder.locationTextView.setText(problem.getLocation());
        holder.descriptionTextView.setText(problem.getDescription());
    }

    @Override
    public int getItemCount() {
        return this.myAlertsList.size();
    }

}
