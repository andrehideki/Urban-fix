package com.mobile.urbanfix.urban_fix.view.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
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
    private TextInputEditText alertDescriptionEditText;
    private TextInputLayout alertDescriptionLayout;
    private Spinner typeOfProblemSpinner;
    private SeekBar urgencySeekBar;
    private FloatingActionButton cameraButton;
    private MainMVP.IAlertPresenter presenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_alert, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        startMVP();
        photoImageView  = (ImageView) view.findViewById(R.id.photoImageView);
        locationTextView = (TextView) view.findViewById(R.id.locationTextView);
        alertDescriptionEditText = (TextInputEditText) view.findViewById(R.id.alertDescriptionEditText);
        alertDescriptionLayout = (TextInputLayout) view.findViewById(R.id.descriptionTextInputLayout);
        typeOfProblemSpinner = (Spinner) view.findViewById(R.id.typeOfProblemSpinner);
        urgencySeekBar = (SeekBar) view.findViewById(R.id.urgencySeekBar);
        cameraButton = (FloatingActionButton) view.findViewById(R.id.cameraButton);
        finishAlertButton = (Button) view.findViewById(R.id.finishAlertButton);

        alertDescriptionEditText.setOnClickListener(this);
        finishAlertButton.setOnClickListener(this);
        cameraButton.setOnClickListener(this);

        presenter.setupSpinner(getActivity(), typeOfProblemSpinner);
        typeOfProblemSpinner.setOnItemSelectedListener(this);
        presenter.initAlert(getContext());
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.startGPS(getContext());
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
                presenter.setDescription(alertDescriptionEditText.getText().toString(), alertDescriptionLayout,
                        getContext());
                presenter.setUrgency(urgencySeekBar.getProgress());
                presenter.finishAlert(getActivity());
                break;
            }
            case R.id.alertDescriptionEditText: {
                presenter.setDescription(alertDescriptionEditText.getText().toString(),
                       alertDescriptionLayout, getContext());
                break;
            }
        }
    }

    @Override
    public Context getContext() {
        return getActivity();
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

    @Override
    public void onLocationDefined(String location) {
        locationTextView.setText(location);
    }

    @Override
    public void showMessage(String message) {

    }

    private void startMVP() {
        this.presenter = new AlertPresenter(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        this.presenter.setKindOfProblem(position, parent.getSelectedItem().toString());
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
