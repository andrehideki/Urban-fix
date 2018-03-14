package com.mobile.urbanfix.urban_fix.view.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.mobile.urbanfix.urban_fix.presenter.MainMVP;
import com.mobile.urbanfix.urban_fix.presenter.MapsPresenter;
import com.mobile.urbanfix.urban_fix.R;

public class MapsFragment extends Fragment
        implements  View.OnClickListener,
                    MainMVP.IMapsView {

    private FloatingActionButton alertButton;
    private MainMVP.IMapsPresenter presenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_maps, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        startMVP();
        presenter.getLocationPermissions(getActivity());
        alertButton = (FloatingActionButton) view.findViewById(R.id.alertButton);
        alertButton.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.startLocationListener(getActivity());
        presenter.initMap();
    }

    @Override
    public Context getContext() {
        return getActivity();
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.stopLocationListener();
    }

   @Override
    public void showMessage(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void finishView() {
        getFragmentManager().popBackStack();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        presenter.onRequestPermissionResult(this, requestCode, permissions, grantResults);
    }

    @Override
    public void onStop() {
        super.onStop();
        presenter.stopLocationListener();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.alertButton){
            presenter.openAlertFragment((AppCompatActivity) getActivity());
        }
    }

    @Override
    public FragmentManager getCurrentFragmentManager() {
        return getFragmentManager();
    }
    private void startMVP() {
        this.presenter = new MapsPresenter(this);
    }
}
