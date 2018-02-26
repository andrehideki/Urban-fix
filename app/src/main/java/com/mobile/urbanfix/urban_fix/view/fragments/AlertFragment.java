package com.mobile.urbanfix.urban_fix.view.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.mobile.urbanfix.urban_fix.R;
import com.mobile.urbanfix.urban_fix.presenter.AlertPresenter;
import com.mobile.urbanfix.urban_fix.presenter.MainMVP;

public class AlertFragment extends Fragment implements  MainMVP.IAlertView,
                                                        View.OnClickListener,
                                                        AdapterView.OnItemSelectedListener {

    private ImageView photoImageView;
    private Button finishAlertButton;
    private TextView locationTextView;
    private EditText alertDescriptionEditText;
    private Spinner typeOfProblemSpinner;
    private SeekBar urgencySeekBar;
    private FloatingActionButton cameraButton;
    private MainMVP.IAlertPresenter presenter;

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_alert, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        startMVP();
        photoImageView  = (ImageView) view.findViewById(R.id.photoImageView);
        locationTextView = (TextView) view.findViewById(R.id.locationTextView);
        alertDescriptionEditText = (EditText) view.findViewById(R.id.alertDescriptionEditText);
        typeOfProblemSpinner = (Spinner) view.findViewById(R.id.typeOfProblemSpinner);
        urgencySeekBar = (SeekBar) view.findViewById(R.id.urgencySeekBar);
        cameraButton = (FloatingActionButton) view.findViewById(R.id.cameraButton);
        finishAlertButton = (Button) view.findViewById(R.id.finishAlertButton);

        finishAlertButton.setOnClickListener(this);
        cameraButton.setOnClickListener(this);

        presenter.initAlert();
        presenter.setupSpinner(getActivity(), typeOfProblemSpinner);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.cameraButton: {
                presenter.dispachTakePhotoIntent(this, this);
                break;
            }
            case R.id.finishAlertButton: {
                presenter.finishAlert(alertDescriptionEditText.getText().toString(),
                        urgencySeekBar.getProgress());
                break;
            }
        }
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        presenter.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        presenter.onRequestPermissionResult(this, requestCode, permissions, grantResults);
    }

    @Override
    public void setupPhotoImageView(Bitmap bitmap) {
        photoImageView.setImageBitmap(bitmap);
    }

    private void startMVP() {
        this.presenter = new AlertPresenter();
    }

}
