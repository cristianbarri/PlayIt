package com.playit.playit;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.playit.playit.UtilsHTTP.AsyncTaskLogin;
import com.playit.playit.UtilsHTTP.CustomHttpClient;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {


    //TextView t;
    private Button b;
    private EditText user, pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);
        setContentView(R.layout.activity_main);

        user = (EditText)findViewById(R.id.editText1);
        pass = (EditText)findViewById(R.id.editText2);

        b = (Button)findViewById(R.id.button1);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uname = user.getText().toString();
                String pwd = pass.getText().toString();

                //AsyncTaskLogin task = new AsyncTaskLogin();
                //task.execute(new String[]{uname, pwd});
                doLogin(user.getText().toString(), pass.getText().toString());
            }
        });
    }

    private class validateUserTask extends AsyncTask<String, Void, String> {
        String response = null;
        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
            postParameters.add(new BasicNameValuePair("username", params[0] ));
            postParameters.add(new BasicNameValuePair("password", params[1] ));
            String res = null;
            try {
                response = CustomHttpClient.executeHttpPost("http://www.marcfernandezantolin.com/test.php", postParameters);
                res=response.toString();
                JSONObject jObject = new JSONObject(res);
                res = jObject.getString("login");

            }
            catch (Exception e) {
                //txt_Error.setText(e.toString());
            }
            return res;
        }//close doInBackground

        @Override
        protected void onPostExecute(String result) {
            if(result.equals("1")){
                //navigate to Main Menu
                Intent i = new Intent(MainActivity.this, ProfileSwipe.class);
                startActivity(i);
            }
            else{
                //txt_Error.setText("Sorry!! Incorrect Username or Password");
            }
        }//close onPostExecute
    }// close validateUserTask
//close LoginActivity


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
            postParameters.add(new BasicNameValuePair("password", pass ));
            String response;
            try {
                response = CustomHttpClient.executeHttpPost("http://www.marcfernandezantolin.com/test.php", postParameters);
                Log.i("response","feta");
                /*JSONObject jObject = new JSONObject(response);
                Log.i("json","creat");
                response = jObject.getString("login");
                Log.i("json","parsejat");
                */Log.i("response",response);
                if (response.contains("1")/*.equals("1")*/) {
                    Intent i = new Intent(this, ProfileSwipe.class);
                    startActivity(i);
                }
            } catch (Exception e) {
                Log.i("peta", "merdaca");
            }

            // Execute the request

            /*try {
                response = httpclient.execute(httpget);
                // Examine the response status
                Log.i("Praeda", response.getStatusLine().toString());

                // Get hold of the response entity
                HttpEntity entity = response.getEntity();
                // If the response does not enclose an entity, there is no need
                // to worry about connection release

                if (entity != null) {

                    // A Simple JSON Response Read
                    InputStream instream = entity.getContent();
                    String result = convertStreamToString(instream);
                    // now you have the string representation of the HTML request
                    instream.close();
                    JSONObject jObject = new JSONObject(result);
                    String veredict = jObject.getString("login");
                    Toast.makeText(getApplicationContext(), veredict,
                            Toast.LENGTH_LONG).show();
                    if (veredict.equals("1")) {
                        Intent i = new Intent(this, ProfileSwipe.class);
                        startActivity(i);
                    }
                }


            } catch (Exception e) {
                Log.i("peta", "merdaca");
            }*/

        }
    }

    private static String convertStreamToString(InputStream is) {
    /*
     * To convert the InputStream to String we use the BufferedReader.readLine()
     * method. We iterate until the BufferedReader return null which means
     * there's no more data to read. Each line will appended to a StringBuilder
     * and returned as String.
     */
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
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



