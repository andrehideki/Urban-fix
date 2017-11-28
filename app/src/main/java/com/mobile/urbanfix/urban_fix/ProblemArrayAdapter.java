package com.mobile.urbanfix.urban_fix;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mobile.urbanfix.urban_fix.model.Problem;

import java.util.ArrayList;

public class ProblemArrayAdapter extends ArrayAdapter<Problem> {

    private Context context;
    private ArrayList<Problem> problemArrayList;


    public ProblemArrayAdapter(Context context, ArrayList<Problem> problemArrayList) {
        super(context,0, problemArrayList);
        this.context = context;
        this.problemArrayList = problemArrayList;
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Problem problemPosition = this.problemArrayList.get( position );
        convertView = LayoutInflater.from(this.context).inflate(R.layout.list_item_problem, null);
        TextView datetextView, kindOfProblemTextView, descriptionTextView, statusTextView, locationTextView, checkedTextView;

        datetextView = (TextView) convertView.findViewById(R.id.datetextView);
        kindOfProblemTextView = (TextView) convertView.findViewById(R.id.kindOfProblemTextView);
        descriptionTextView = (TextView) convertView.findViewById(R.id.descriptionTextView);
        statusTextView = (TextView) convertView.findViewById(R.id.statusTextView);
        locationTextView = (TextView) convertView.findViewById(R.id.locationTextView);
        checkedTextView = (TextView) convertView.findViewById(R.id.checkedTextView);

        datetextView.setText( problemPosition.getDate() );
        kindOfProblemTextView.setText( problemPosition.getKindOfProblem() );
        descriptionTextView.setText( problemPosition.getDescription() );
        statusTextView.setText( problemPosition.getStatus() );
        locationTextView.setText( problemPosition.getLocation() );
        checkedTextView.setText( problemPosition.getChecked() );

        return convertView;
    }

}
