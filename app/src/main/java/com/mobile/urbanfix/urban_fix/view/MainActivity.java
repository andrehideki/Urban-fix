package com.mobile.urbanfix.urban_fix.view;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.mobile.urbanfix.urban_fix.factory.ConnectionFactory;
import com.mobile.urbanfix.urban_fix.R;
import com.mobile.urbanfix.urban_fix.view.fragments.AccountFragment;
import com.mobile.urbanfix.urban_fix.view.fragments.AlertFragment;
import com.mobile.urbanfix.urban_fix.view.fragments.MapsFragment;
import com.mobile.urbanfix.urban_fix.view.fragments.NoticyFragment;
import com.mobile.urbanfix.urban_fix.view.fragments.SettingsFragment;
import com.mobile.urbanfix.urban_fix.model.User;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public final int REQUEST_PERMISSION = 100;

    private FragmentManager fragmentManager;
    private static FloatingActionButton fab;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    public static User user;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private FirebaseUser firebaseUser;
    private boolean canBeFinished = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        fragmentManager = getSupportFragmentManager();

        initFirebase();
        disableFAB();
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
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar( toolbar );
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAlertFragment();
            }
        });
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener( toggle );
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }





    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQUEST_PERMISSION) {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {

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
        } else if ( canBeFinished ) {
            super.onBackPressed();
        }else {
            openMapsFragment();
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            openSettingsFragment();
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
                openNoticyFragment();
                break;
            case R.id.nav_map:
                openMapsFragment();
                break;
            case R.id.nav_statistics:
                Toast.makeText(MainActivity.this, "Funcionalidade ainda não desenvolvida", Toast.LENGTH_LONG).show();
                break;
            case R.id.nav_logout:
                showLogoffDialog();
                break;
            case R.id.userImageView:
                openAccountFragment();
                break;
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void openNoticyFragment() {
        NoticyFragment noticyFragment = new NoticyFragment();
        fragmentManager.beginTransaction().replace(R.id.mainLayout, noticyFragment ).commit();
        setTitle(R.string.fragment_noticy_title);
        disableFAB();
        this.canBeFinished = false;
    }

    public void openMapsFragment() {
        MapsFragment mapsFragment = new MapsFragment();
        fragmentManager.beginTransaction().replace(R.id.mainLayout, mapsFragment).commit();
        setTitle(R.string.fragment_map_title);
        this.canBeFinished = true;
    }

    private void openAlertFragment() {
        AlertFragment alertFragment = new AlertFragment();
        Bundle b = new Bundle();
        LatLng latLng = MapsFragment.getCurrentLocationlatLng();

        b.putDouble("Lat", latLng.latitude );
        b.putDouble("Long", latLng.longitude);
        alertFragment.setArguments(b);


        fragmentManager.beginTransaction().replace(R.id.mainLayout, alertFragment).commit();
        disableFAB();
        setTitle(R.string.fragment_alert_title);
        this.canBeFinished = false;
    }

    private void openSettingsFragment() {
        SettingsFragment settingsFragment = new SettingsFragment();
        fragmentManager.beginTransaction().replace(R.id.mainLayout, settingsFragment).commit();
        setTitle(R.string.fragment_settings_title);
        disableFAB();
        this.canBeFinished = true;
    }

    private void openAccountFragment() {
        AccountFragment accountFragment = new AccountFragment();
        fragmentManager.beginTransaction().replace(R.id.mainLayout, accountFragment).commit();
        disableFAB();
        setTitle(R.string.fragment_account_title);
        this.canBeFinished = false;
    }

    public static void enableFAB() {
        fab.setVisibility(View.VISIBLE);
    }

    public static void disableFAB() {
        fab.setVisibility(View.GONE);
    }





    private void initFirebase() {
        firebaseAuth = ConnectionFactory.getFirebaseAuth();
        databaseReference = ConnectionFactory.getDatabaseReference();
        firebaseUser = ConnectionFactory.getFirebaseUser();
        databaseReference.child("User").child( firebaseUser.getUid() ).
                addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        user = new User();
                        user = dataSnapshot.getValue(User.class);
                        user.setUUID(firebaseUser.getUid());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

    }

    public static User getUser() {
        return user;
    }


    private void showLogoffDialog() {
        AlertDialog.Builder builder;

        builder = new AlertDialog.Builder(MainActivity.this);

        builder.setTitle( getString( R.string.main_loggof_alert_title))
                .setMessage( getString( R.string.main_loggof_alert_message ) )
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        firebaseAuth.signOut();
                        startActivity( new Intent( MainActivity.this, LoginActivity.class ));
                        finish();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        builder.setIcon(R.drawable.ic_warning_black_24dp);
        builder.show();
    }


}
