package com.mobile.urbanfix.urban_fix.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.mobile.urbanfix.urban_fix.Connection;
import com.mobile.urbanfix.urban_fix.R;

public class SettingsFragment extends Fragment {

    private Button logoutButton;
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    public void initViews() {
        logoutButton = (Button) getActivity().findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userLogout();
            }
        });
    }

    public void userLogout() {
        AlertDialog.Builder msg = new AlertDialog.Builder(getContext());
        msg.setTitle(getString(R.string.setting_exit_title));
        msg.setMessage(getString(R.string.setting_exit_ask));
        msg.setPositiveButton(getString(R.string.setting_exit_option), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Connection.logout();
                getActivity().finish();
            }
        });
        msg.setCancelable(true);
        msg.show();
    }
}
