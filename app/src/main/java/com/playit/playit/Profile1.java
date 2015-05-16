package com.playit.playit;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.playit.playit.UtilsHTTP.CustomHttpClient;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Profile1.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Profile1#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Profile1 extends android.support.v4.app.Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String name = null;
    private int id;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    TextView user = null;
    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Profile1.
     */
    // TODO: Rename and change types and number of parameters
    public static Profile1 newInstance(String param1, String param2) {
        Profile1 fragment = new Profile1();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);


        return fragment;
    }

    public Profile1() {
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
        // Inflate the layout for this fragment


        View view = inflater.inflate(R.layout.fragment_profile1, container, false);
        //Bundle args = this.getArguments();
        //name = args.getString("index");
        Button button = (Button) view.findViewById(R.id.buttonGoNFC);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {

                String response = null;
                try {
                    String url = "http://46.101.139.161/android/last_tag.php?id_user=" + String.valueOf(id);
                    response = CustomHttpClient.executeHttpGet(url);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (response.contains("expired")) {
                    Intent i = new Intent(getActivity(), NFC.class);
                    i.putExtra("id_user", id);
                    startActivity(i);
                } else {
                    String tag = null;
                    JSONObject jObject = null;
                    try {
                        jObject = new JSONObject(response);
                        tag = jObject.getString("tag");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    tag= "049CDE62393780";

                    String response1 = null;
                    try {
                        String url = "http://46.101.139.161/android/song_list?tag=" + tag + "&id_user=" + String.valueOf(id);
                        response1 = CustomHttpClient.executeHttpGet(url);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Intent in = new Intent(getActivity(), SesionSwipe.class);
                    in.putExtra("infoSongs", response1);
                    in.putExtra("id_user", id);
                    in.putExtra("tag", tag);
                    startActivity(in);
                }

            }
        });


        ProfileSwipe activity = (ProfileSwipe) getActivity();
        name = activity.getName();
        id = activity.getId();
        user = (TextView) view.findViewById(R.id.textView7);
        user.setText(name);

        return view;
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
