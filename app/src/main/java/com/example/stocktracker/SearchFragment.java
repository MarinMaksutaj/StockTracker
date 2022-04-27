package com.example.stocktracker;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.FileUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public Activity containerActivity = null;
    public ListView searchLV = null;
    public EditText searchET = null;
    List<HashMap<String, String>> aList;
    SimpleAdapter simpleAdapter;
    String ticker[] = {};
    String name[] = {};
    String market[] = {};

    public SearchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        Button searchButton = view.findViewById(R.id.searchButton);
        searchLV = view.findViewById(R.id.searchListView);
        searchET = view.findViewById(R.id.searchEditText);
        searchButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String text = String.valueOf(searchET.getText());
                if (text.length() == 0) {
                    return;
                }
                new SearchAPIManager().execute(text);
            }
        });

        return view;
    }
    public void setContainerActivity(Activity containerActivity){
        this.containerActivity = containerActivity;
    }

    private class SearchAPIManager extends AsyncTask<String, String, String> {


        @Override
        protected String doInBackground(String... params) {
            String searchTerm = params[0];
            System.out.println(searchTerm);
            String key = getResources().getString(R.string.API_KEY);
            String urlString= "https://api.polygon.io/v3/reference/tickers?search="+searchTerm+"&active=true&sort=ticker&order=asc&limit=30&apiKey="+key;
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
                System.out.println(obj);
                JSONArray r = (JSONArray) obj.get("results");
                market = new String[r.length()];
                ticker = new String[r.length()];
                name = new String[r.length()];
                for( int i = 0 ; i < r.length() ; i++){
                    JSONObject tempNews = (JSONObject) r.get(i);
                    ticker[i] = tempNews.getString("ticker");
                    name[i] = tempNews.getString("name");
                    market[i] = tempNews.getString("primary_exchange");
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

            for(int i = 0 ; i < ticker.length; i++){
                HashMap<String, String> hm = new HashMap<String, String>();
                hm.put("tickerText", "Ticker:\n"+ticker[i]);
                hm.put("nameText", name[i]);
                hm.put("marketText", "Exchange:\n"+market[i]);
                aList.add(hm);
            }
            String[] from = {"tickerText","nameText", "marketText"};
            int[] to = {R.id.tickerText, R.id.nameText, R.id.marketText};
            simpleAdapter = new SimpleAdapter(containerActivity, aList, R.layout.search_row_layout, from , to);
            searchLV.setAdapter(simpleAdapter);
            searchLV.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                    String symbol = ticker[position];
                    // add the stock to the file
                    File file = new File(containerActivity.getFilesDir(), "stocks.txt");
                    ArrayList <String> currentStocks = new ArrayList<String>();
                    try {
                        BufferedReader reader = new BufferedReader(new FileReader(file));
                        for (String line; (line = reader.readLine()) != null;) {
                            currentStocks.add(line.trim());
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));
                        if (currentStocks.contains(symbol)) {
                            containerActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(containerActivity);
                                    builder.setMessage("Stock already exists!");
                                    builder.setPositiveButton("OK", null);
                                    builder.show();
                                }
                            });
                            return;
                        }
                        writer.write(symbol + "\n");
                        writer.close();
                        System.out.println("write successful");
                        // create a pop up to show the user that the stock was added
                        containerActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(containerActivity);
                                builder.setMessage("Stock added!");
                                builder.setPositiveButton("OK", null);
                                builder.show();
                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            simpleAdapter.notifyDataSetChanged();

        }
    }
}