package com.playit.playit.tabswipe;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.View;

import com.playit.playit.Sesion1;
import com.playit.playit.Sesion2;
import com.playit.playit.Sesion3;
import com.playit.playit.Stats1;
import com.playit.playit.Stats2;



public class TabsPagerAdapter2 extends FragmentStatePagerAdapter {

    private String[] titles = {"Session", "DJ Profile", "Session3" };

    public TabsPagerAdapter2(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        switch(i){
            case 0:
                return new Sesion1();
            case 1:
                return new Sesion2();
            case 2:
                return new Sesion3();
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
