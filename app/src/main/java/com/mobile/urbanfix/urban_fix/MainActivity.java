package com.mobile.urbanfix.urban_fix;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;

import com.mobile.urbanfix.urban_fix.fragments.AccountFragment;
import com.mobile.urbanfix.urban_fix.fragments.GMapsFragment;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout activityMainDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private Toolbar toolbar;

    private ActionBarDrawerToggle drawerToggle;
    private NavigationView navigationView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        activityMainDrawerLayout = (DrawerLayout) findViewById(R.id.activityMainDrawerLayout);
        mToggle = new ActionBarDrawerToggle(this, activityMainDrawerLayout, R.string.open, R.string.close);

        activityMainDrawerLayout.addDrawerListener(mToggle);

        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        navigationView = (NavigationView) findViewById(R.id.navigaionView);
        setupDrawerContent(navigationView);



    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                activityMainDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }

        if(mToggle.onOptionsItemSelected(item)) return true;
        return super.onOptionsItemSelected(item);
    }


    public void setupDrawerContent(NavigationView upDrawerContent) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        selectDrawerItem(item);
                        return true;
                    }
                }
        );
    }

    private void selectDrawerItem(MenuItem item) {
        Fragment fragment = null;
        Class fragmentClass = null;
        FragmentManager fragmentManager = getSupportFragmentManager();

        switch (item.getItemId()) {
            case R.id.accountItem:
                fragmentClass = AccountFragment.class;
                break;
            case R.id.mapItem:
                fragmentClass = GMapsFragment.class;
                break;
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }


        fragmentManager.beginTransaction().replace(R.id.content_main, fragment).commit();

        item.setChecked(true);
        setTitle(item.getTitle());
        activityMainDrawerLayout.closeDrawers();
    }


}
