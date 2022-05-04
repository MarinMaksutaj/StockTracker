package com.example.stocktracker;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_10;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.ToggleButton;
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

/*
 * @author: Hector Beltran & Marin Maksutaj
 * @description: This is the NewsFragment class. It is used to display the news
 *             screen. It provides the users of the app with a list of news articles.
 *            It also spawns a webView to display the news article.
 * 
 */
public class NewsFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public ListView newsLV = null;
    private String mParam1;
    private String mParam2;
    public Activity containerActivity = null;
    private ConstraintLayout layout = null;
    List<HashMap<String, String>> aList;
    SimpleAdapter simpleAdapter;
    String titles[] = {};
    String dates[] = {};
    String author[] = {};
    String urls[] = {};

    /*
    * This is the constructor for the class.
    */
    public NewsFragment() {
        // Required empty public constructor
    }

    /*
    * This is the newInstance method for the class.
    * It is used to create a new instance of the
    * class with the given parameters.
    */
    public static NewsFragment newInstance(String param1, String param2) {
        NewsFragment fragment = new NewsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    /*
    * The onCreate method for the class. It is used to initialize the class.
    */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    /*
    * The onResume method for the class. It is triggered when the fragment is resumed.
    */
    public void onResume() {
        super.onResume();
        animateEntrance();
    }

    @Override
    /*
    * This is the onCreateView method for the class.
    * It is used to create the view for the fragment.
    */
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_news, container, false);
        newsLV = v.findViewById(R.id.newsListView);
        layout = v.findViewById(R.id.newsFragment);
        TextView title = v.findViewById(R.id.newsTextView);
        //we read what stock is currently graph, to fetch its news
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        String ticker = sharedPref.getString(getString(R.string.stock_graphed), "AAPL");
        SharedViewModel model = new ViewModelProvider(requireActivity())
                .get(SharedViewModel.class);
        boolean lengthy = sharedPref.getBoolean(getString(R.string.length_news), false);
        String lengthNews = "10";
        if( lengthy ) lengthNews = "20";

        title.setText(getResources().getString(R.string.news_title)+ ticker);

        new NewsAPICallManager().execute(ticker,lengthNews);



        return v;
    }
    /*
    * The setContainerActivity method for the class.
    * It is used to set the container activity.
    */
    public void setContainerActivity(Activity containerActivity){
        this.containerActivity = containerActivity;
    }

    /*
    * The anitmateEntrance method for the class.
    * It is used to animate the entrance of the fragment.
    */
    private void animateEntrance() {
        SharedViewModel model = new ViewModelProvider(requireActivity()).
                get(SharedViewModel.class);
        int from = model.getFrom().getValue();
        int to = 2;
        if(from == to ) return;
        DisplayMetrics displayMetrics = new DisplayMetrics();
        containerActivity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        if(from > 2 ) width = width*(-1);
        ObjectAnimator animatorX = ObjectAnimator.ofFloat(layout,
                "translationX", width);
        animatorX.setDuration(0); // Milliseconds
        animatorX.start();
        animatorX = ObjectAnimator.ofFloat(layout, "translationX", 0);
        animatorX.setDuration(300); // Milliseconds
        animatorX.start();
        model.setFrom(2);
    }

    /*
    * The NewsAPICallManager class. It is used to fetch the news from the API.
    */
    private class NewsAPICallManager extends AsyncTask<String, String, String> {


        @Override
        /*
         * The doInBackground method for the class. It is used to fetch the news from the API.
        */
        protected String doInBackground(String... params) {
            String urlString= "https://api.polygon.io/v2/reference/news?ticker="+params[0]+
            "&limit="+params[1]+"&apiKey="+getResources().getString(R.string.API_KEY);
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
        /*
        * The onPostExecute method for the class. It is used to set the news in the list view.
         It uses a simple adapter to set the news.
        */
        protected void onPostExecute(String result){

            for(int i = 0 ; i < author.length; i++){
                HashMap<String, String> hm = new HashMap<String, String>();
                hm.put("titleText", titles[i]);
                hm.put("authorText", author[i]);
                aList.add(hm);
            }
            String[] from = {"titleText","authorText"};
            int[] to = {R.id.titleText, R.id.authorText};
            simpleAdapter = new SimpleAdapter(containerActivity,
                    aList, R.layout.news_row_layout, from , to);
            newsLV.setAdapter(simpleAdapter);
            newsLV.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(urls[position]));
                    startActivity(intent);
                }
            });
            simpleAdapter.notifyDataSetChanged();

        }
    }
}