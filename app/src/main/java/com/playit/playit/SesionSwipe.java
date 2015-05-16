package com.playit.playit;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.DialogInterface;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.playit.playit.tabswipe.SlidingTabLayout;
import com.playit.playit.tabswipe.TabsPagerAdapter;
import com.playit.playit.tabswipe.TabsPagerAdapter2;


public class SesionSwipe extends ActionBarActivity {

    SlidingTabLayout mSlidingTabLayout;
    ViewPager pager;
    String info;
    int id_user;
    String tag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sesion_swipe);

        Bundle b = getIntent().getExtras();
        info = b.getString("infoSongs");
        id_user = b.getInt("id_user");
        tag = b.getString("tag");

        pager = (ViewPager) findViewById(R.id.pagerSesionSwipe);
        pager.setAdapter(new TabsPagerAdapter2(getSupportFragmentManager()));

        mSlidingTabLayout = (SlidingTabLayout) findViewById(R.id.tabsSesionSwipe);
        mSlidingTabLayout.setViewPager(pager);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sesion_swipe, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public String getInfoSongs(){
        return info;
    }

    public String getIdUSer(){
        return String.valueOf(id_user);
    }
    public String getTag(){
        return tag;
    }

}
