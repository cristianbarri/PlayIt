package com.playit.playit.UtilsHTTP;

import android.content.Intent;
import android.os.AsyncTask;

import com.playit.playit.Profile1;
import com.playit.playit.ProfileSwipe;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;

/**
 * Created by marcfernandezantolin on 29/4/15.
 */
public class AsyncTaskLogin extends AsyncTask<String, Void, String> {
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
            String veredict = jObject.getString("login");
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

           // Intent i = new Intent(getAsyncTask, Profile1.class);
            //startActivity(i);

        }
        else{
            //txt_Error.setText("Sorry!! Incorrect Username or Password");
        }
    }//close onPostExecute
}// close validateUserTask


