package com.example.stocktracker;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.StrictMode;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChartFragment extends Fragment {

    View view = null;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public Activity containerActivity = null;

    public ChartFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChartFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChartFragment newInstance(String param1, String param2) {
        ChartFragment fragment = new ChartFragment();
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
        // get the webview

    }

    @Override
    public void onResume() {
        // grab the stock symbol from the file
        // read the file
        super.onResume();
        File file = new File(containerActivity.getFilesDir(), "stocks.txt");
        if (!file.exists()) {
            return;
        }
        FileReader reader = null;
        try {
            reader = new FileReader(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        BufferedReader br = new BufferedReader(reader);
        ArrayList stockSymbol = new ArrayList();
        while (true) {
            try {
                String line = br.readLine();
                if (line == null) {
                    break;
                }
                stockSymbol.add(line);
                // update listView with the stock symbol

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        ListView listView = (ListView) containerActivity.findViewById(R.id.stockListView);
        listView.setAdapter(new ArrayAdapter<String>(containerActivity, android.R.layout.simple_list_item_1, stockSymbol));
        // add listView item click listener
        listView.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // get the stock symbol
                String stockSymbol = (String) adapterView.getItemAtPosition(i);
                updateWebView(stockSymbol);
            }
        });
    }


    public void updateWebView(String stockSymbol) {
        // update the webview
        WebView webView = view.findViewById(R.id.webview);
        // do an API call to polygon api to get the stock data
        String apiCall = "https://api.polygon.io/v2/aggs/ticker/" + stockSymbol + "/range/1/minute/2021-07-22/2021-07-22?adjusted=true&sort=asc&limit=50&apiKey=KzgmVQZuf5ay0ucp_wnIkgNqQ1h_UKMF";
        // use a thread to make the api call
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(apiCall);
                    System.out.println("test");
                    BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
                    StringBuilder sb = new StringBuilder();
                    String line = null;
                    while (true) {
                        try {
                            if (!((line = br.readLine()) != null)) break;
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        sb.append(line);
                    }
                    br.close();
                    String response = sb.toString();
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray data = jsonObject.getJSONArray("results");
                    String stockDataString = "";
                    for (int i = data.length() - 5; i < data.length(); i++) {
                        stockDataString += "[ '";
                        JSONObject stockData = data.getJSONObject(i);
                        String time = stockData.getString("t");
                        // convert time from UNIX time to readable time
                        time = time.substring(0, time.length() - 3);
                        stockDataString += time + "', ";
                        String low = stockData.getString("l");
                        stockDataString += low + ", ";
                        String open = stockData.getString("o");
                        stockDataString += open + ", ";
                        String close = stockData.getString("c");
                        stockDataString += close + ", ";
                        String high = stockData.getString("h");
                        stockDataString += high;
                        String volume = stockData.getString("v");
                        stockDataString += "],\n";
                    }
                    System.out.println(stockDataString);

                    String html = "<html>\n" +
                            "  <head>\n" +
                            "    <script type=\"text/javascript\" src=\"https://www.gstatic.com/charts/loader.js\"></script>\n" +
                            "    <script type=\"text/javascript\">\n" +
                            "      google.charts.load('current', {'packages':['corechart']});\n" +
                            "      google.charts.setOnLoadCallback(drawChart);\n" +
                            "\n" +
                            "  function drawChart() {\n" +
                            "    var data = google.visualization.arrayToDataTable([\n " +
                             stockDataString +
                            "      // Treat first row as data as well.\n" +
                            "    ], true);\n" +
                            "\n" +
                            "    var options = {\n" +
                            "      legend:'none'\n" +
                            "    };\n" +
                            "\n" +
                            "    var chart = new google.visualization.CandlestickChart(document.getElementById('chart_div'));\n" +
                            "\n" +
                            "    chart.draw(data, options);\n" +
                            "  }\n" +
                            "    </script>\n" +
                            "  </head>\n" +
                            "  <body>\n" +
                            "    <div id=\"chart_div\" style=\"width: 400px; height: 200px;\"></div>\n" +
                            "  </body>\n" +
                            "</html>";

                    // update the webview on the UI thread
                    containerActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            webView.getSettings().setJavaScriptEnabled(true);
                            webView.loadData(html, "text/html", "UTF-8");
                        }
                    });

                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
                // get the response

                // update the webview


            }
        }).start();
        

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        
        view = inflater.inflate(R.layout.fragment_chart, container, false);
        File file = new File(containerActivity.getFilesDir(), "stocks.txt");
        String stockSymbol = "";
        if (!file.exists()) {
            stockSymbol = "AAPL";
        } else {
            FileReader reader = null;
        try {
            reader = new FileReader(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        BufferedReader br = new BufferedReader(reader);
        try {
            stockSymbol = br.readLine().trim();
        } catch (IOException e) {
            e.printStackTrace();
        }
        }
        
        updateWebView(stockSymbol);
        // get the button
        Button button = view.findViewById(R.id.shareChartButton);
        // add button click listener
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get the webview
                System.out.println("clicked");
                WebView webView = view.findViewById(R.id.webview);
                // get the bitmap
                // save the bitmap
                StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                StrictMode.setVmPolicy(builder.build());
                Bitmap bitmap = Bitmap.createBitmap(
                        webView.getWidth(), webView.getHeight(), Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(bitmap);
                webView.draw(canvas);
                // get the uri of the canvas
                Uri uri = Uri.parse(MediaStore.Images.Media.
                        insertImage(containerActivity.getContentResolver(),
                                bitmap, "Title", null));
                // STEP 3: spawn a new fragment to display a list of contacts from the phone
                ContactsFragment contactsFragment = new ContactsFragment();
                Bundle args = new Bundle();
                args.putString("uri", uri.toString());
                contactsFragment.setContainerActivity(containerActivity);
                contactsFragment.setArguments(args);
                getFragmentManager().beginTransaction()
                        .replace(R.id.mainContainer, contactsFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        return view;
    }

    public void setContainerActivity(Activity containerActivity){
        this.containerActivity = containerActivity;
    }
}