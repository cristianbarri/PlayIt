package com.playit.playit;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.playit.playit.UtilsHTTP.CustomHttpClient;
import com.playit.playit.tabswipe.SlidingTabLayout;
import com.playit.playit.tabswipe.TabsPagerAdapter;
import com.playit.playit.tabswipe.TabsPagerAdapter2;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;


public class SesionSwipe extends ActionBarActivity {

    SlidingTabLayout mSlidingTabLayout;
    ViewPager pager;
    String info;

    private MenuItem itemM;

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
        //getMenuInflater().inflate(R.menu.menu_sesion_swipe, menu);
        CreaMenu(menu);
        return true;
    }

    private void CreaMenu(Menu menu)
    {
        MenuItem item1 = menu.add(0,0,0,"Exit this session");
        {
            item1.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
            itemM =item1;
        }
    }

    public boolean onOptionsItemSelected(MenuItem item)
    {
        return MenuSelecciona(item);
    }

    private boolean MenuSelecciona(MenuItem item)
    {
        switch(item.getItemId())
        {
            case 0:
                ConnectivityManager connMgr = (ConnectivityManager)
                        getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                if (networkInfo == null || !networkInfo.isConnected()) {
                    Log.i("info", "noNetwork");
                } else {
                    HttpClient httpclient = new DefaultHttpClient();
                    ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
                    String response;
                    int id = 0;
                    try {
                        response = CustomHttpClient.executeHttpGet("http://46.101.139.161/bdapi/leaveRoom.php?id_user=" + id_user);
                        if (response.contains("algun error") || response.contains("todo ok")){
                            AlertDialog.Builder builder = new AlertDialog.Builder(this);
                            builder.setTitle("Choose wisely!");
                            builder.setMessage("If you leave this session, you will need to scan the tag again to vote");
// Add the buttons
                            builder.setPositiveButton("Exit session", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // User clicked OK button
                                    finish();
                                }
                            });
                            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // User cancelled the dialog
                                }
                            });
// Set other dialog properties


// Create the AlertDialog
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Something went wrong, please try again later", Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "Something went wrong, please try again later", Toast.LENGTH_LONG).show();
                    }
                }
                itemM = item;
                return true;
        }
        return false;
    }

    /*@Override
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
    }*/

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
