package com.playit.playit;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import com.playit.playit.tabswipe.*;

public class ProfileSwipe extends ActionBarActivity {

    SlidingTabLayout mSlidingTabLayout;
    ViewPager pager;
    String name;
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_swipe);

        Bundle b = getIntent().getExtras();
        name = b.getString("name");
        id = b.getInt("id");
        pager = (ViewPager) findViewById(R.id.pagerProfileSwipe);
        pager.setAdapter(new TabsPagerAdapter(getSupportFragmentManager()));
        /*PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        tabs.setViewPager(pager);*/
        mSlidingTabLayout = (SlidingTabLayout) findViewById(R.id.tabsProfileSwipe);
        mSlidingTabLayout.setViewPager(pager);

    }

    @Override public void onBackPressed() {

        Log.i("oh", " si");
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose wisely!");
        builder.setMessage("Do you wish to log out?");
// Add the buttons
        builder.setPositiveButton("Only Exit", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
            }
        });
        builder.setNegativeButton("Log out", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });
// Set other dialog properties


// Create the AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    String getName(){
        return name;
    }

    int getId(){
        return id;
    }

}