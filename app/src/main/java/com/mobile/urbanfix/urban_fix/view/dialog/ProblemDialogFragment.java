package com.mobile.urbanfix.urban_fix.view.dialog;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mobile.urbanfix.urban_fix.R;
import com.mobile.urbanfix.urban_fix.model.Problem;

public class ProblemDialogFragment extends DialogFragment {

    private static Problem problem;

    public static ProblemDialogFragment newInstance(Problem problem) {
        ProblemDialogFragment dialogFragment = new ProblemDialogFragment();
        ProblemDialogFragment.problem = problem;
        return dialogFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_problem, container);
        TextView kindOfProblemTextView, dateTextView, descriptionTextView, locationTextView;
        kindOfProblemTextView = (TextView) view.findViewById(R.id.kindOfProblemTextView);
        dateTextView = (TextView) view.findViewById(R.id.locationTextView);
        descriptionTextView = (TextView) view.findViewById(R.id.descriptionTextView);
        locationTextView = (TextView) view.findViewById(R.id.locationTextView);

        kindOfProblemTextView.setText(problem.getKindOfProblem());
        dateTextView.setText(problem.getDate());
        descriptionTextView.setText(problem.getDescription());
        locationTextView.setText(problem.getLocation());

        return view;
    }

}
