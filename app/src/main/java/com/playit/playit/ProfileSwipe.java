package com.playit.playit;

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

    String getName(){
        return name;
    }

    int getId(){
        return id;
    }

}