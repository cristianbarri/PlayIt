package com.playit.playit;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.AnimationDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.nfc.NfcAdapter;
import android.nfc.tech.IsoDep;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.MifareUltralight;
import android.nfc.tech.Ndef;
import android.nfc.tech.NfcA;
import android.nfc.tech.NfcB;
import android.nfc.tech.NfcF;
import android.nfc.tech.NfcV;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.playit.playit.UtilsHTTP.CustomHttpClient;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class NFC extends ActionBarActivity {

    public static final String MIME_TEXT_PLAIN = "text/plain";
    public static final String TAG = "NfcDemo";

    private TextView mTextView;
    private NfcAdapter mNfcAdapter;
    //private String tagID = "049CDE62393780"; //ID del tag con el que permitimos entrar en session
    private int id_user;
    private ImageView img;
    private AnimationDrawable frameAnimation;

    //PRUEBO NFC@@@@@@@@@@@@

    // list of NFC technologies detected:
    private final String[][] techList = new String[][] {
            new String[] {
                    NfcA.class.getName(),
                    NfcB.class.getName(),
                    NfcF.class.getName(),
                    NfcV.class.getName(),
                    IsoDep.class.getName(),
                    MifareClassic.class.getName(),
                    MifareUltralight.class.getName(), Ndef.class.getName()
            }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        Bundle bundle = getIntent().getExtras();
        id_user = bundle.getInt("id_user");

        mTextView = (TextView) findViewById(R.id.textView_explanation);
        mTextView.setVisibility(mTextView.GONE);

        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);

        if (mNfcAdapter == null) {
            // Stop here, we definitely need NFC
            Toast.makeText(this, "This device doesn't support NFC.", Toast.LENGTH_LONG).show();
            finish();
            return;

        }

        if (!mNfcAdapter.isEnabled()) {
            mTextView.setText("NFC is disabled");
            mTextView.setVisibility(mTextView.VISIBLE);

            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Choose wisely!");
            builder.setMessage("Do you wish to activate NFC?");
// Add the buttons
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User clicked OK button
                    dialog.dismiss();
                    startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));

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

            img = (ImageView)findViewById(R.id.nfc);
            img.setBackgroundResource(R.drawable.animation);

            // Get the background, which has been compiled to an AnimationDrawable object.
            frameAnimation = (AnimationDrawable) img.getBackground();
            // Start the animation (looped playback by default).
            frameAnimation.stop();

        } else {
            mTextView.setText("NFC is enabled");
            mTextView.setVisibility(mTextView.GONE);

            // Load the ImageView that will host the animation and
            // set its background to our AnimationDrawable XML resource.
            img = (ImageView)findViewById(R.id.nfc);
            img.setBackgroundResource(R.drawable.animation);

            // Get the background, which has been compiled to an AnimationDrawable object.
            frameAnimation = (AnimationDrawable) img.getBackground();
            // Start the animation (looped playback by default).
            frameAnimation.start();
        }

        //handleIntent(getIntent());


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_nfc, menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);

        mTextView.setVisibility(mTextView.GONE);


        if (!mNfcAdapter.isEnabled()) {
            mTextView.setText("NFC is disabled");
            mTextView.setVisibility(mTextView.VISIBLE);


            img = (ImageView)findViewById(R.id.nfc);
            img.setBackgroundResource(R.drawable.animation);

            // Get the background, which has been compiled to an AnimationDrawable object.
            frameAnimation = (AnimationDrawable) img.getBackground();
            // Start the animation (looped playback by default).
            frameAnimation.stop();

        } else {
            mTextView.setText("NFC is enabled");
            mTextView.setVisibility(mTextView.GONE);

            // Load the ImageView that will host the animation and
            // set its background to our AnimationDrawable XML resource.
            img = (ImageView)findViewById(R.id.nfc);
            img.setBackgroundResource(R.drawable.animation);

            // Get the background, which has been compiled to an AnimationDrawable object.
            frameAnimation = (AnimationDrawable) img.getBackground();
            // Start the animation (looped playback by default).
            frameAnimation.start();
        }
        // creating pending intent:
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        // creating intent receiver for NFC events:
        IntentFilter filter = new IntentFilter();
        filter.addAction(NfcAdapter.ACTION_TAG_DISCOVERED);
        filter.addAction(NfcAdapter.ACTION_NDEF_DISCOVERED);
        filter.addAction(NfcAdapter.ACTION_TECH_DISCOVERED);
        // enabling foreground dispatch for getting intent from NFC event:
        NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        nfcAdapter.enableForegroundDispatch(this, pendingIntent, new IntentFilter[]{filter}, this.techList);

    }

    @Override
    protected void onPause() {
        super.onPause();
        // disabling foreground dispatch:
        NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        nfcAdapter.disableForegroundDispatch(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        if (intent.getAction().equals(NfcAdapter.ACTION_TAG_DISCOVERED)) {

            //((TextView)findViewById(R.id.showNFCtext)).setText(ByteArrayToHexString(intent.getByteArrayExtra(NfcAdapter.EXTRA_ID)));
            ConnectivityManager connMgr = (ConnectivityManager)
                    getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            String response = null;
            if (networkInfo == null || !networkInfo.isConnected()) {
                Log.i("info", "noNetwork");
            } else {
                //ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
                //postParameters.add(new BasicNameValuePair("tag", ByteArrayToHexString(intent.getByteArrayExtra(NfcAdapter.EXTRA_ID))));
                //postParameters.add(new BasicNameValuePair("id_user",String.valueOf(id_user)));
                String tag = ByteArrayToHexString(intent.getByteArrayExtra(NfcAdapter.EXTRA_ID));
                try {
                    String url = "http://46.101.139.161/android/song_list?tag="+tag+"&id_user="+String.valueOf(id_user);
                    response = CustomHttpClient.executeHttpGet(url);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (response.contains("this tag isn't valid")) {
                    Toast.makeText(this, "This NFC TAG isn't valid", Toast.LENGTH_SHORT).show();
                }else if (response.equals(null)){
                //otro toast
                }else {
                    Intent i = new Intent(this, SesionSwipe.class);
                    i.putExtra("infoSongs",response);
                    i.putExtra("id_user", id_user);
                    i.putExtra("tag", tag);
                    startActivity(i);
                    finish();
                }
            }
        }
    }

    private String ByteArrayToHexString(byte [] inarray) {
        int i, j, in;
        String [] hex = {"0","1","2","3","4","5","6","7","8","9","A","B","C","D","E","F"};
        String out= "";

        for(j = 0 ; j < inarray.length ; ++j)
        {
            in = (int) inarray[j] & 0xff;
            i = (in >> 4) & 0x0f;
            out += hex[i];
            i = in & 0x0f;
            out += hex[i];
        }
        return out;
    }

    //ACABO DE PROBAR NFC


/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_nfc, menu);
        return true;
    }*/

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

}
