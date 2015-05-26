package com.playit.playit;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import com.playit.playit.tabswipe.*;

public class ProfileSwipe extends ActionBarActivity {

    SlidingTabLayout mSlidingTabLayout;
    ViewPager pager;
    String name;
    int id;
    String img_path;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_swipe);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        Bundle b = getIntent().getExtras();
        name = b.getString("name");
        id = b.getInt("id");
        img_path = b.getString("img_path");
        pager = (ViewPager) findViewById(R.id.pagerProfileSwipe);
        pager.setAdapter(new TabsPagerAdapter(getSupportFragmentManager()));
        /*PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        tabs.setViewPager(pager);*/
        mSlidingTabLayout = (SlidingTabLayout) findViewById(R.id.tabsProfileSwipe);
        mSlidingTabLayout.setViewPager(pager);



    }

    @Override public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose wisely!");
        builder.setMessage("Do you wish to log out?");
// Add the buttons
        builder.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
                System.exit(0);

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog

            }
        });

        builder.setNeutralButton("Log out", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);

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

    String getImgPath(){
        if (img_path.equals("NULL")) return "none.png";
        else return img_path;
    }






}