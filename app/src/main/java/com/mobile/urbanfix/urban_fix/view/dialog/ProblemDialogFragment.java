package com.mobile.urbanfix.urban_fix.view.dialog;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobile.urbanfix.urban_fix.R;
import com.mobile.urbanfix.urban_fix.model.Problem;
import com.mobile.urbanfix.urban_fix.MainMVP;
import com.mobile.urbanfix.urban_fix.presenter.AlertDialogPresenter;

public class ProblemDialogFragment extends DialogFragment implements MainMVP.IProblemDialogView {

    private static Problem problem;
    private ImageView problemPhotoImageView;
    private MainMVP.IProblemDialogPresenter presenter;


    public static ProblemDialogFragment newInstance(Problem problem) {
        ProblemDialogFragment dialogFragment = new ProblemDialogFragment();
        ProblemDialogFragment.problem = problem;
        return dialogFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.AppTheme);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_problem, container);
        TextView kindOfProblemTextView, dateTextView, descriptionTextView, addressTextView,
        statusTextView, urgencyTextView;
        startMVP();

        kindOfProblemTextView = view.findViewById(R.id.kindOfProblemTextView);
        dateTextView = view.findViewById(R.id.dateTextView);
        descriptionTextView = view.findViewById(R.id.descriptionTextView);
        addressTextView = view.findViewById(R.id.addressTextView);
        statusTextView = view.findViewById(R.id.textView);
        urgencyTextView = view.findViewById(R.id.urgencyTextView);
        problemPhotoImageView = view.findViewById(R.id.problemPhotoImageView);

        presenter.setInformations(problem, kindOfProblemTextView, dateTextView, statusTextView, addressTextView,
                descriptionTextView, urgencyTextView, problemPhotoImageView);


        return view;
    }


    @Override
    public void setImageBitmap(Bitmap bitmap) {
        problemPhotoImageView.setImageBitmap(bitmap);
    }

    private void startMVP() {
        this.presenter = new AlertDialogPresenter(this);
    }
}
