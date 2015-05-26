package com.playit.playit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.StrictMode;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.codec.digest.DigestUtils;
import com.playit.playit.UtilsHTTP.BCrypt;

import com.playit.playit.UtilsHTTP.BCryptTest;
import com.playit.playit.UtilsHTTP.CustomHttpClient;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


//importar para parse.com


public class MainActivity extends ActionBarActivity {


    //TextView t;
    private Button b, b1;
    private EditText user, pass, edit_passconf, edit_email;
    private boolean showSignUp = false;
    private TextView text_passconf, text_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
/*
        try {
            BCryptTest.multiThreadReproductibleHash();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
*/

        StrictMode.setThreadPolicy(policy);
        setContentView(R.layout.activity_main);

        user = (EditText)findViewById(R.id.editText1);
        pass = (EditText)findViewById(R.id.editText2);
        edit_passconf = (EditText)findViewById(R.id.editText);
        edit_email = (EditText)findViewById(R.id.editText3);
        text_email = (TextView)findViewById(R.id.textView6);
        text_passconf = (TextView)findViewById(R.id.textView5);
        user.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus && !pass.hasFocus() && !edit_passconf.hasFocus() && !edit_email.hasFocus()) {
                    hideKeyboard(v);
                }
            }
        });

        pass.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus && !user.hasFocus() && !edit_passconf.hasFocus() && !edit_email.hasFocus()) {
                    hideKeyboard(v);
                }
            }
        });

        edit_passconf.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus && !pass.hasFocus() && !user.hasFocus() && !edit_email.hasFocus()) {
                    hideKeyboard(v);
                }
            }
        });

        edit_email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus && !pass.hasFocus() && !edit_passconf.hasFocus() && !user.hasFocus()) {
                    hideKeyboard(v);
                }
            }
        });

        //Invisibles las cosas para SIGNUP
        edit_passconf.setVisibility(View.GONE);
        edit_email.setVisibility(View.GONE);
        text_passconf.setVisibility(View.GONE);
        text_email.setVisibility(View.GONE);


        b = (Button)findViewById(R.id.button1);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!showSignUp) {
                    doLogin(user.getText().toString(), pass.getText().toString());
                    //Log.i("password in clar", pass.getText().toString());
                }
                else {
                    //Oculto los campos
                    edit_passconf.setVisibility(View.GONE);
                    edit_email.setVisibility(View.GONE);
                    text_passconf.setVisibility(View.GONE);
                    text_email.setVisibility(View.GONE);
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)b1.getLayoutParams();
                    params.addRule(RelativeLayout.ALIGN_TOP, R.id.editText2);

                    b1.setLayoutParams(params); //causes layout update
                    showSignUp = false;
                }
            }
        });

        b1 = (Button)findViewById(R.id.button);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (showSignUp) {
                    if (android.util.Patterns.EMAIL_ADDRESS.matcher(edit_email.getText().toString()).matches())
                        doSignup(user.getText().toString(), edit_email.getText().toString(), pass.getText().toString(), edit_passconf.getText().toString());
                    else Toast.makeText(getApplicationContext(), "This isn't a valid email", Toast.LENGTH_LONG).show();
                }else {
                    //Enseño los campos
                    edit_passconf.setVisibility(View.VISIBLE);
                    edit_email.setVisibility(View.VISIBLE);
                    text_passconf.setVisibility(View.VISIBLE);
                    text_email.setVisibility(View.VISIBLE);
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)b1.getLayoutParams();
                    params.addRule(RelativeLayout.ALIGN_TOP, R.id.editText3);

                    b1.setLayoutParams(params); //causes layout update
                    showSignUp = true;
                }
            }
        });

    }

    protected void doLogin(String user, String pass) {
        ConnectivityManager connMgr = (ConnectivityManager)
        getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo == null || !networkInfo.isConnected()) {
            Log.i("info", "noNetwork");
        } else {
            HttpClient httpclient = new DefaultHttpClient();
            ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
            postParameters.add(new BasicNameValuePair("name", user ));
            postParameters.add(new BasicNameValuePair("password", pass));
            String response;
            int id = 0;
            try {
                response = CustomHttpClient.executeHttpGet("http://46.101.139.161/android/validate_user?name=" + user + "&password=" + pass);
                if (response.contains("no user")){
                    Toast.makeText(getApplicationContext(), "Invalid user", Toast.LENGTH_LONG).show();
                }else if (response.contains("wrong password")){
                    Toast.makeText(getApplicationContext(), "Invalid password", Toast.LENGTH_LONG).show();
                }else {
                    JSONObject jObject = new JSONObject(response);
                    id = jObject.getInt("id");
                    String img_path = jObject.getString("img_path");
                    if (id > 0) {
                        Intent i = new Intent(this, ProfileSwipe.class);
                        i.putExtra("name", user);
                        i.putExtra("id", id);
                        i.putExtra("img_path", img_path);
                        startActivity(i);
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "Something went wrong, please try again later", Toast.LENGTH_LONG).show();
                    }
                }
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Something went wrong, please try again later", Toast.LENGTH_LONG).show();
            }
        }
    }




    protected void doSignup(String user, String email, String pass, String pass2) {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo == null || !networkInfo.isConnected()) {
            Log.i("info", "noNetwork");
        } else {
            if (user.equals("") || email.equals("") || pass.equals("") || pass2.equals("")){
                Toast.makeText(getApplicationContext(), "Please fill in all the fields", Toast.LENGTH_LONG).show();
            } else {


                /*ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
                postParameters.add(new BasicNameValuePair("name", user));
                postParameters.add(new BasicNameValuePair("email", email));*/

                if (pass.equals(pass2)) {


                    String response;
                    try {
                        response = CustomHttpClient.executeHttpGet("http://46.101.139.161/android/store_user?name="+user+"&password="+pass+"&email="+email);

                        if (!response.contains("user already exists") && !response.contains("0")) {
                            //Ya estoy totalmente registrado pues escondo los campos
                            edit_passconf.setVisibility(View.GONE);
                            edit_email.setVisibility(View.GONE);
                            text_passconf.setVisibility(View.GONE);
                            text_email.setVisibility(View.GONE);
                            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) b1.getLayoutParams();
                            params.addRule(RelativeLayout.ALIGN_TOP, R.id.editText2);

                            b1.setLayoutParams(params); //causes layout update
                            showSignUp = false;
                        } else if (response.contains("user already exists")){
                            Toast.makeText(getApplicationContext(), "This user already exists", Toast.LENGTH_LONG).show();
                        }else {
                            Toast.makeText(getApplicationContext(), "There was an error signing up", Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception e) {
                        Log.i("peta", "merdaca");
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Passwords don't match", Toast.LENGTH_LONG).show();
                }
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}



