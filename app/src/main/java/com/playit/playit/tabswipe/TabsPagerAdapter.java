package com.playit.playit.tabswipe;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.playit.playit.Profile1;
import com.playit.playit.Stats1;
import com.playit.playit.Stats2;

/**
 * Created by marcfernandezantolin on 21/4/15.
 */
/*
//import com.playit.playit.Profile;
import com.playit.playit.Profile1;
import com.playit.playit.Stats1;
import com.playit.playit.Stats2;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;

public class TabsPagerAdapter extends FragmentPagerAdapter {

    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int arg0) {
        // TODO Auto-generated method stub
        switch (arg0) {
            case 0:
                return new Profile1();//Pestanya Profile
            case 1:
                return new Stats1();//Pestanya Stats1
            case 2:
                return new Stats2();//Pestanya Stats2
        }
        return null;
    }


    @Override
    public int getCount() {
        return 3;
    }
}*/

public class TabsPagerAdapter extends FragmentStatePagerAdapter {

    private String[] titles = {"Profile", "Stats1", "Stats2" };

    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        switch(i){
            case 0:
                return new Profile1();
            case 1:
                return new Stats1();
            case 2:
                return new Stats2();
        }
        return null;
    }

    @Override
    public int getCount() {
        return titles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
