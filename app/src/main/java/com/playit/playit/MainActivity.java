package com.playit.playit;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.codec.digest.DigestUtils;
import com.playit.playit.UtilsHTTP.BCrypt;

import com.playit.playit.UtilsHTTP.CustomHttpClient;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;

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

        StrictMode.setThreadPolicy(policy);
        setContentView(R.layout.activity_main);

        user = (EditText)findViewById(R.id.editText1);
        pass = (EditText)findViewById(R.id.editText2);
        edit_passconf = (EditText)findViewById(R.id.editText);
        edit_email = (EditText)findViewById(R.id.editText3);
        text_email = (TextView)findViewById(R.id.textView6);
        text_passconf = (TextView)findViewById(R.id.textView5);


        //Invisibles las cosas para SIGNUP
        edit_passconf.setVisibility(View.GONE);
        edit_email.setVisibility(View.GONE);
        text_passconf.setVisibility(View.GONE);
        text_email.setVisibility(View.GONE);


        b = (Button)findViewById(R.id.button1);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!showSignUp)
                    doLogin(user.getText().toString(), pass.getText().toString());

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

            // Prepare a request object
            /*String url = "http://www.marcfernandezantolin.com/test.php?name=" + user + "&password=" + pass;
            Log.i("url", url);
            HttpGet httpget = new HttpGet(url);*/


            ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
            postParameters.add(new BasicNameValuePair("name", user ));
            String sha256hex2 = DigestUtils.sha256Hex(pass);

            postParameters.add(new BasicNameValuePair("password", sha256hex2));
            String response;
            try {
                response = CustomHttpClient.executeHttpPost("http://46.101.139.161/bdapi/login.php", postParameters);
                JSONObject jObject = new JSONObject(response);
                //response = jObject.getString("login");
                int id = jObject.getInt("id");
                if (id > 0) {
                    Intent i = new Intent(this, ProfileSwipe.class);
                    i.putExtra("name",user);
                    i.putExtra("id", id);
                    startActivity(i);
                } else {
                    Toast.makeText(getApplicationContext(), "Invalid user or password", Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                Log.i("peta", "merdaca");
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


                ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
                postParameters.add(new BasicNameValuePair("name", user));
                postParameters.add(new BasicNameValuePair("email", email));
                Log.i("passwords:", pass + " " + pass2);
                if (pass.equals(pass2)) {
                    String sha256hex = DigestUtils.sha256Hex(pass2);
                    Toast.makeText(getApplicationContext(),sha256hex , Toast.LENGTH_LONG).show();
                    postParameters.add(new BasicNameValuePair("password", sha256hex));


                    String response;
                    try {
                        response = CustomHttpClient.executeHttpPost("http://46.101.139.161/bdapi/signup.php", postParameters);

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
}



