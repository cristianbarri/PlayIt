package com.playit.playit;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.playit.playit.CustomAdapter.MyCustomAdapter;
import com.playit.playit.CustomAdapter.SongsWithVotes;
import com.playit.playit.UtilsHTTP.CustomHttpClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Sesion1.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Sesion1#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Sesion1 extends android.support.v4.app.Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String info;
    private String id_user;
    private String tag;
    public int numvotesuser;

    boolean primer = true;

    private View view;

    private ListView lv;

    private ArrayList<SongsWithVotes> songs;// = new ArrayList<SongsWithVotes>();

    private MyCustomAdapter myCustomAdapter;

    private TextView remaining_votes;
    private TextView textVotes;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //private TextView mTextView;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Sesion1.
     */
    // TODO: Rename and change types and number of parameters
    public static Sesion1 newInstance(String param1, String param2) {
        Sesion1 fragment = new Sesion1();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public Sesion1() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_sesion1, container, false);
        SesionSwipe activity = (SesionSwipe) getActivity();

        info = activity.getInfoSongs();
        id_user = activity.getIdUSer();
        tag = activity.getTag();



        //Actualiza datos
        lv = (ListView) view.findViewById(R.id.listView);
        songs = new ArrayList<SongsWithVotes>();
        refresh();
        myCustomAdapter = new MyCustomAdapter(this.getActivity(), songs);


        if (primer == true) {
            lv.setAdapter(myCustomAdapter);
            primer = false;
        }

        final SwipeRefreshLayout swipeView = (SwipeRefreshLayout) view.findViewById(R.id.swipe);

        //swipeView.setColorScheme(android.R.color.holo_blue_dark, android.R.color.holo_blue_light, android.R.color.holo_green_light, android.R.color.holo_green_dark);
       // if (listIsAtTop()){
        swipeView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                // TODO Auto-generated method stub
         //       if (listIsAtTop()){
                swipeView.setRefreshing(true);
                Log.i("Swipe", "Refreshing List");
                refresh();
                ( new Handler()).postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        swipeView.setRefreshing(false);
                        //refresh();
                    }
                }, 1000);
            }//}
        });//}

        lv.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                boolean enable = false;
                if(lv != null && lv.getChildCount() > 0){
                    // check if the first item of the list is visible
                    boolean firstItemVisible = lv.getFirstVisiblePosition() == 0;
                    // check if the top of the first item is visible
                    boolean topOfFirstItemVisible = lv.getChildAt(0).getTop() == 0;
                    // enabling or disabling the refresh layout
                    enable = firstItemVisible && topOfFirstItemVisible;
                }
                swipeView.setEnabled(enable);
            }
        });



        return view;
    }

    public void refresh(){
        String response = null;
        try {
                String url = "http://46.101.139.161/android/song_list?tag="+tag+"&id_user="+String.valueOf(id_user);
                response = CustomHttpClient.executeHttpGet(url);
            } catch (Exception e) {
                e.printStackTrace();
            }


        JSONObject jObject = null;
        try {
            jObject = new JSONObject(response);
            Iterator<String> iter = jObject.keys();
            int i = 0;
            songs.clear();
            numvotesuser = 10;
            while (iter.hasNext()) {
                String key = iter.next();
                JSONObject jar = jObject.getJSONObject(key);
                String nom = jar.getString("nom");
                int numvots = jar.getInt("num_vots");
                String snumvots = String.valueOf(numvots);
                int havotat = jar.getInt("ha_votat");
                if (havotat > 0) --numvotesuser;

                SongsWithVotes s = new SongsWithVotes(nom, snumvots, havotat, id_user, tag, Integer.valueOf(key));
                songs.add(i, s);


                ++i;
            }
            if (!primer) myCustomAdapter.notifyDataSetChanged();

            textVotes = (TextView) view.findViewById(R.id.textView9);
            textVotes.setText(Integer.toString(numvotesuser));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Set The Adapter


        //myCustomAdapter.notifyDataSetChanged();
        //lv.invalidateViews();


    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    /*@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }*/

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
