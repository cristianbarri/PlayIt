package com.playit.playit.CustomAdapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.parse.codec.digest.DigestUtils;
import com.playit.playit.R;
import com.playit.playit.Sesion1;
import com.playit.playit.SesionSwipe;
import com.playit.playit.UtilsHTTP.CustomHttpClient;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by marcfernandezantolin on 12/5/15.
 */
public class MyCustomAdapter extends ArrayAdapter {

    private List<SongsWithVotes> mSongs;
    private Context mContext;
    private int mResources;


    public MyCustomAdapter(Context context, ArrayList<SongsWithVotes> data) {
        super(context, R.layout.custom_item, data);
        mContext = context;
        mSongs = data;
        mResources = R.layout.custom_item;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = layoutInflater.inflate(mResources, parent, false);

        TextView name = (TextView) rowView.findViewById(R.id.text1);
        final Button button_vote = (Button) rowView.findViewById(R.id.button2);

        name.setText(mSongs.get(position).song);
        button_vote.setText(mSongs.get(position).votes);
        if(mSongs.get(position).havotat == 1) {
            button_vote.setBackgroundColor(Color.parseColor("#FF0000"));
            button_vote.setEnabled(false);
        }

        button_vote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Your code that you want to execute on this button click
                ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
                postParameters.add(new BasicNameValuePair("id_tag", mSongs.get(position).tag));
                postParameters.add(new BasicNameValuePair("id_user", mSongs.get(position).id_user));
                postParameters.add(new BasicNameValuePair("id_song", String.valueOf(mSongs.get(position).id_song)));

                String response;
                try {
                    response = CustomHttpClient.executeHttpPost("http://46.101.139.161/bdapi/vote.php", postParameters);
                    if (response.contains("massa vots")){
                        Log.i("massa", response);
                    } else {
                        int inc_votes = Integer.parseInt(mSongs.get(position).votes) + 1;
                        mSongs.get(position).votes = Integer.toString(inc_votes);
                        mSongs.get(position).havotat = 1;
                        button_vote.setText(mSongs.get(position).votes);
                        button_vote.setBackgroundColor(Color.parseColor("#FF0000"));
                        button_vote.setEnabled(false);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        });

        return rowView;
    }

    public void setmSongs(List<SongsWithVotes> mSongs) {
        this.mSongs = mSongs;
    }

}

