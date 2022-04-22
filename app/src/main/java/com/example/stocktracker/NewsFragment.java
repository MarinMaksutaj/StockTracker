package com.example.stocktracker;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_10;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public Activity containerActivity = null;

    public NewsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NewsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NewsFragment newInstance(String param1, String param2) {
        NewsFragment fragment = new NewsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
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
        View v = inflater.inflate(R.layout.fragment_news, container, false);
        //new NewsAPICallManager().execute("ticker to be passed here");


        return v;
    }
    public void setContainerActivity(Activity containerActivity){
        this.containerActivity = containerActivity;
    }

    private class NewsAPICallManager extends AsyncTask<String, String, String> {


        @Override
        protected String doInBackground(String... params) {
            //TODO: failed here on doing API call, this is sample code from documentation
            String uriString = "";
            URI uri = null;
            try {
                uri = new URI("wss://delayed.polygon.io/stocks");
            } catch (URISyntaxException e) {
                System.out.println("error with URI");
                e.printStackTrace();
            }
            WebSocketClient mWs = new WebSocketClient( uri, new Draft_10() ){
                @Override
                public void onMessage( String message ) {
                    System.out.println( message );
                    }
                @Override
                public void onOpen( ServerHandshake handshake ) {
                    System.out.println( "opened connection" );
                    }
                @Override
                public void onClose( int code, String reason, boolean remote ) {
                    System.out.println( "closed connection" );
                    }
                @Override
                public void onError( Exception ex ) {
                    ex.printStackTrace();
                    }
            };

            //open websocket
            mWs.connect();
            JSONObject auther = new JSONObject();
            try {
                auther.put("action", "auth");
                auther.put("params", R.string.API_KEY);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            String message = auther.toString();
            mWs.send(message); //TODO: add check for mWs != null before trying  this, to avoid crashing on new devices

            JSONObject suber = new JSONObject();
            try {
                suber.put("action", "subscribe");
                suber.put("params", "T.AAPL");

            } catch (JSONException e) {
                e.printStackTrace();
            }
            message = suber.toString();
            mWs.send(message);

            return null;
        }
    }
}