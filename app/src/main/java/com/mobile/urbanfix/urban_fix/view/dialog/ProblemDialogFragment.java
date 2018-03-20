package com.mobile.urbanfix.urban_fix.view.dialog;


import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobile.urbanfix.urban_fix.R;
import com.mobile.urbanfix.urban_fix.model.Problem;
import com.mobile.urbanfix.urban_fix.presenter.MainMVP;
import com.mobile.urbanfix.urban_fix.presenter.ProblemDialogPresenter;

public class ProblemDialogFragment extends DialogFragment implements MainMVP.IProblemDialogView {

    private static Problem problem;
    private ImageView problemPhotoImageView;
    private MainMVP.IProblemDialogPresenter presenter;


    public static ProblemDialogFragment newInstance(Problem problem) {
        ProblemDialogFragment dialogFragment = new ProblemDialogFragment();
        ProblemDialogFragment.problem = problem;
        return dialogFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_problem, container);
        TextView kindOfProblemTextView, dateTextView, descriptionTextView, locationTextView,
        statusTextView, urgencyTextView;
        startMVP();

        kindOfProblemTextView = (TextView) view.findViewById(R.id.kindOfProblemTextView);
        dateTextView = (TextView) view.findViewById(R.id.dateTextView);
        descriptionTextView = (TextView) view.findViewById(R.id.descriptionTextView);
        locationTextView = (TextView) view.findViewById(R.id.locationTextView);
        statusTextView = (TextView) view.findViewById(R.id.statusTextView);
        urgencyTextView = (TextView) view.findViewById(R.id.urgencyTextView);
        problemPhotoImageView = (ImageView) view.findViewById(R.id.problemPhotoImageView);

        presenter.setInformations(problem, kindOfProblemTextView, dateTextView, statusTextView, locationTextView,
                descriptionTextView, urgencyTextView, problemPhotoImageView);

        return view;
    }


    @Override
    public void setImageBitmap(Bitmap bitmap) {
        problemPhotoImageView.setImageBitmap(bitmap);
    }

    private void startMVP() {
        this.presenter = new ProblemDialogPresenter(this);
    }
}
