package com.example.stocktracker;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_10;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONArray;
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
import android.widget.TextView;

//import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.client.HttpClient;
//import org.apache.commons.io.IOUtils;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

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
    public ListView newsLV = null;
    private String mParam1;
    private String mParam2;
    public Activity containerActivity = null;
    List<HashMap<String, String>> aList;
    SimpleAdapter simpleAdapter;
    String titles[] = {};
    String dates[] = {};
    String author[] = {};
    String urls[] = {};

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
        newsLV = v.findViewById(R.id.newsListView);
        TextView title = v.findViewById(R.id.newsTextView);
        //TODO: implement to read something  from saved InstanceState
        String ticker = "AMD";
        title.setText(getResources().getString(R.string.news_title)+ ticker);



        /***
        HttpClient httpclient = new DefaultHttpClient();
        HttpResponse response = httpclient.execute(new HttpGet(urlString));
        StatusLine statusLine = response.getStatusLine();
        if(statusLine.getStatusCode() == HttpStatus.SC_OK){
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            response.getEntity().writeTo(out);
            String responseString = out.toString();
            out.close();
            //..more logic
        } else{
            //Closes the connection.
            response.getEntity().getContent().close();
            throw new IOException(statusLine.getReasonPhrase());
        }
         ***/
        new NewsAPICallManager().execute("ticker to be passed here");



        return v;
    }
    public void setContainerActivity(Activity containerActivity){
        this.containerActivity = containerActivity;
    }

    private class NewsAPICallManager extends AsyncTask<String, String, String> {


        @Override
        protected String doInBackground(String... params) {
            //TODO: failed here on doing API call, this is sample code from documentation
            String urlString= "https://api.polygon.io/v2/reference/news?ticker=AMD&limit=15&apiKey=KzgmVQZuf5ay0ucp_wnIkgNqQ1h_UKMF";
            aList = new ArrayList<HashMap<String, String>>();
            JSONObject result = new JSONObject();
            URL url = null;
            try {
                url = new URL(urlString);
                URLConnection connection = url.openConnection();
                connection.setConnectTimeout(10000); // 10 seconds
                connection.setReadTimeout(10000); // 10 seconds
                connection.connect();
                // Optionally check the status code. Status 200 means everything went OK.
                InputStream stream = connection.getInputStream();
                String text = new BufferedReader(
                        new InputStreamReader(stream, StandardCharsets.UTF_8))
                        .lines()
                        .collect(Collectors.joining("\n"));
                stream.close();
                JSONObject obj = new JSONObject(text);
                //System.out.println(obj);
                JSONArray r = (JSONArray) obj.get("results");
                author = new String[r.length()];
                titles = new String[r.length()];
                dates = new String[r.length()];
                urls = new String[r.length()];
                for( int i = 0 ; i < r.length() ; i++){
                    JSONObject tempNews = (JSONObject) r.get(i);
                    author[i] = tempNews.getString("author");
                    titles[i] = tempNews.getString("title");
                    dates[i] = tempNews.getString("published_utc");
                    urls[i] = tempNews.getString("article_url");
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;

        }

        @Override
        protected void onPostExecute(String result){

            for(int i = 0 ; i < author.length; i++){
                HashMap<String, String> hm = new HashMap<String, String>();
                hm.put("titleText", titles[i]);
                hm.put("authorText", author[i]);
                aList.add(hm);
            }
            String[] from = {"titleText","authorText"};
            int[] to = {R.id.titleText, R.id.authorText};
            simpleAdapter = new SimpleAdapter(containerActivity, aList, R.layout.news_row_layout, from , to);
            newsLV.setAdapter(simpleAdapter);
            newsLV.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                    //TODO:implement this buttons to open up a webview for the URL at index "position"
                    System.out.println("Index clicked " + position);
                }
            });
            simpleAdapter.notifyDataSetChanged();

        }
    }
}