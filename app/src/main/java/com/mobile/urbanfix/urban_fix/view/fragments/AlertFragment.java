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
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mobile.urbanfix.urban_fix.R;
import com.mobile.urbanfix.urban_fix.presenter.AlertPresenter;
import com.mobile.urbanfix.urban_fix.MainMVP;

import java.util.ArrayList;

public class AlertFragment extends Fragment implements  MainMVP.IAlertView,
                                                        View.OnClickListener,
                                                        AdapterView.OnItemSelectedListener {

    private ImageView photoImageView;
    private Button finishAlertButton;
    private TextView urgencyTextView;
    private TextInputEditText alertDescriptionEditText;
    private TextInputLayout alertDescriptionLayout;
    private Spinner typeOfProblemSpinner, userAddressSpinner;
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
        photoImageView  =  view.findViewById(R.id.photoImageView);
        urgencyTextView = view.findViewById(R.id.urgencyTextView);
        alertDescriptionEditText = view.findViewById(R.id.alertDescriptionEditText);
        alertDescriptionLayout = view.findViewById(R.id.descriptionTextInputLayout);
        typeOfProblemSpinner = view.findViewById(R.id.typeOfProblemSpinner);
        userAddressSpinner = view.findViewById(R.id.userAddressSpinner);
        urgencySeekBar = view.findViewById(R.id.urgencySeekBar);
        cameraButton = view.findViewById(R.id.cameraButton);
        finishAlertButton = view.findViewById(R.id.finishAlertButton);

        alertDescriptionEditText.setOnClickListener(this);
        finishAlertButton.setOnClickListener(this);
        cameraButton.setOnClickListener(this);
        urgencySeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                presenter.setUrgency(urgencySeekBar.getProgress());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        typeOfProblemSpinner.setOnItemSelectedListener(this);
        presenter.initAlert(getContext());

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_MODE_CHANGED);
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.problems_array, android.R.layout.simple_expandable_list_item_1);
        typeOfProblemSpinner.setAdapter(spinnerAdapter);
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
    public void finishView() {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.popBackStack();
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
    public void onAddressHasBeenFetched(ArrayList<String> addressesList) {
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_list_item_1, addressesList);
        userAddressSpinner.setAdapter(spinnerAdapter);
        userAddressSpinner.setOnItemSelectedListener(this);
    }

    @Override
    public void changeUrgencyStatus(String urgencyStatus) {
        urgencyTextView.setText(urgencyStatus);
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
    }

    private void startMVP() {
        this.presenter = new AlertPresenter(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        int selectViewId = parent.getId();
        if (this.typeOfProblemSpinner.getId() == selectViewId) {
            this.presenter.setKindOfProblem(position, parent.getSelectedItem().toString());
            showMessage(parent.getSelectedItem().toString());
        } else if (this.userAddressSpinner.getId() == selectViewId) {
            this.presenter.setAddress(position, parent.getSelectedItem().toString());
            showMessage(parent.getSelectedItem().toString());
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {}


}
