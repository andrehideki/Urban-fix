package com.mobile.urbanfix.urban_fix;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import com.mobile.urbanfix.urban_fix.fragments.AccountFragment;
import com.mobile.urbanfix.urban_fix.fragments.AlertFragment;
import com.mobile.urbanfix.urban_fix.fragments.MapsFragment;
import com.mobile.urbanfix.urban_fix.fragments.SettingsFragment;
import com.mobile.urbanfix.urban_fix.services.GPSService;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public final int REQUEST_PERMISSION = 100;
    private BroadcastReceiver broadcastReceiver;

    private FragmentManager fragmentManager;
    private FloatingActionButton fab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fragmentManager = getSupportFragmentManager();
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAlertFragment();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if (!verifyRuntimePermissions() ) {
            Intent i = new Intent(getApplicationContext(), GPSService.class);
            startService(i);
        }

        openMapsFragment();
    }

    private boolean verifyRuntimePermissions() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_PERMISSION);
            return true;
        }
        return false;
    }

    private void initViews() {

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQUEST_PERMISSION) {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                //asfadsf
            } else {
                verifyRuntimePermissions();
            }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(broadcastReceiver == null) {
            broadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                //Fazer algo
                }
            };
        }
        registerReceiver(broadcastReceiver, new IntentFilter("location_update"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(broadcastReceiver != null ) {
            unregisterReceiver(broadcastReceiver);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id) {
            case R.id.nav_profile:
                openAccountFragment();
                break;
            case R.id.nav_alert:
                openAlertFragment();
                break;
            case R.id.nav_notices:
                break;
            case R.id.nav_map:
                openMapsFragment();
                break;
            case R.id.nav_statistics:
                break;
            case R.id.nav_settings:
                openSettingsFragment();
                break;
            case R.id.userImageView:
                openAccountFragment();
                break;
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void openMapsFragment() {
        MapsFragment mapsFragment = new MapsFragment();
        fragmentManager.beginTransaction().replace(R.id.mainLayout, mapsFragment).commit();
        fab.setVisibility(View.VISIBLE);
        setTitle(R.string.fragment_map_title);
    }

    private void openAlertFragment() {
        AlertFragment alertFragment = new AlertFragment();
        fragmentManager.beginTransaction().replace(R.id.mainLayout, alertFragment).commit();
        fab.setVisibility(View.GONE);
        setTitle(R.string.fragment_alert_title);
    }

    private void openSettingsFragment() {
        SettingsFragment settingsFragment = new SettingsFragment();
        fragmentManager.beginTransaction().replace(R.id.mainLayout, settingsFragment).commit();
        setTitle(R.string.fragment_settings_title);
    }

    private void openAccountFragment() {
        AccountFragment accountFragment = new AccountFragment();
        fragmentManager.beginTransaction().replace(R.id.mainLayout, accountFragment).commit();
        fab.setVisibility(View.GONE);
        setTitle(R.string.fragment_account_title);
    }


}
