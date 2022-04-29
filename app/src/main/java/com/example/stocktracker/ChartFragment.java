package com.example.stocktracker;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import java.util.Random;
import java.util.stream.Collectors;


import android.app.Activity;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import android.os.IBinder;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.google.type.DateTime;

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
    private String tickerGraphed;
    public Activity containerActivity = null;
    private WebView webView;
    private TextView titleTextView;
    private GraphAPIManager grapher;
    private SharedViewModel model;
    private TextView chartTitle;


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
        // create
        model = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

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

        // create a custom adapter
        listView.setAdapter(new ListAdapter(containerActivity, stockSymbol));

    }

    public String[] ArrayListToStringArray(ArrayList<String> stockSymbol) {
        String [] stockSymbolArray = new String[stockSymbol.size()];
        for (int i = 0; i < stockSymbol.size(); i++) {
            stockSymbolArray[i] = stockSymbol.get(i);
        }
        return stockSymbolArray;

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        System.out.println("CREATED");
        view = inflater.inflate(R.layout.fragment_chart, container, false);
        webView = (WebView) view.findViewById(R.id.webview);
        titleTextView = (TextView) view.findViewById(R.id.textView6);
        grapher = new GraphAPIManager();

        //get the correct stock to graph
        System.out.println("Model value onCreateView: "+ model.getStock().getValue());
        File file = new File(containerActivity.getFilesDir(), "stocks.txt");
        tickerGraphed = "";
        if (!file.exists()) {
            tickerGraphed = "AAPL";
        } else {
            //Read first stock in list if we run for the first time
            if(model.getStock().getValue() == null) {
                FileReader reader = null;
                try {
                    reader = new FileReader(file);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                BufferedReader br = new BufferedReader(reader);

                try {
                    String nonEmpty = br.readLine();
                    if (nonEmpty != null)
                        tickerGraphed = nonEmpty.trim(); // list existed but deleted
                    else tickerGraphed = "AAPL";
                } catch (IOException e) {
                    e.printStackTrace();
                }
                alertViewModel(tickerGraphed);
            //We have a nun null ViewModel from where we can read from
            } else tickerGraphed = model.getStock().getValue();
        }

        
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

        notifyNewsOfStockChange(tickerGraphed);
        grapher.execute();
        return view;
    }

    public void setContainerActivity(Activity containerActivity){
        this.containerActivity = containerActivity;
    }

    private void alertViewModel(String stock){
        model.setStock(stock);
    }

    @Override
    public void onPause() {
        super.onPause();
        grapher.cancel(true);
    }

    public void notifyNewsOfStockChange(String ticker){

        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.stock_graphed), ticker);
        editor.apply();
    }

    private class GraphAPIManager extends AsyncTask<String, String, String> {


        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected String doInBackground(String... params) {
            //set proper title
            titleTextView.setText(tickerGraphed + " Chart");

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Calendar cal = Calendar.getInstance();

            //remove 4 days to pool information from at least the last 4 days
            cal.add(Calendar.DATE, -5);
            String date1 = dateFormat.format(cal.getTime());
            String date2 = dateFormat.format(new Date());
            System.out.println(date1 + " vs " + date2);
            SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
            String tickerDuration = "minute";//TODO: this has to be adjusted to work properly once api_key is "paid"
            if(sharedPref.getBoolean(getString(R.string.hourly_setting), false)) tickerDuration = "hour";
            while (true) {
                //we need the proper api_key access to really test the url, we fetch end of the day data.
                String apiCall = "https://api.polygon.io/v2/aggs/ticker/" + tickerGraphed +
                        "/range/1/"+tickerDuration+"/" + date1 + "/" + date2 + "?sort=asc&limit=50&apiKey=" +
                        getResources().getString(R.string.API_KEY);
                System.out.println("Background was run");
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

                    boolean isLined = sharedPref.getBoolean(getString(R.string.line_graph), false);
                    boolean hasTrend = sharedPref.getBoolean(getString(R.string.trend_toggle), false);
                    String html="";
                    if (isLined) html = lineGraphGenerator(data, hasTrend);
                    else html = candleGraphGenerator(data, hasTrend);

                    // update the webview on the UI thread
                    String finalHtml = html;
                    containerActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            webView.getSettings().setJavaScriptEnabled(true);
                            webView.loadData(finalHtml, "text/html", "UTF-8");
                        }
                    });


                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }

                try {
                    //we wait some time before next call
                    Thread.sleep(2000);
                    System.out.println("Sleept");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    System.out.println("NOT - Sleept");
                }

            }
        }
        @RequiresApi(api = Build.VERSION_CODES.O)
        private String candleGraphGenerator(JSONArray data, boolean hasTrend) throws JSONException {
            String stockDataString = "";
            int fetchSize = data.length() - 30; //prevents we have least values than what we are trying to graph
            if(fetchSize<0) fetchSize = 0;
            for (int i = fetchSize; i < data.length(); i++) {
                stockDataString += "[ new Date(";
                JSONObject stockData = data.getJSONObject(i);
                String time = stockData.getString("t");
                stockDataString += time + "), ";
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
            String trend = "";
            if (hasTrend) trend = "trendlines: { 0: {} } ";

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
                    "      legend:'none',\n" +trend+
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
            return html;

        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        private String lineGraphGenerator(JSONArray data, boolean hasTrend) throws JSONException {
            String stockDataString = "";
            int fetchSize = data.length() - 30; //prevents we have least values than what we are trying to graph
            if(fetchSize<0) fetchSize = 0;
            for (int i = fetchSize; i < data.length(); i++) {
                stockDataString += "[ new Date(";
                JSONObject stockData = data.getJSONObject(i);
                String time = stockData.getString("t");
                stockDataString += time + "), ";
                String close = stockData.getString("c");
                stockDataString += close ;
                stockDataString += "],\n";
            }
            System.out.println(stockDataString);
            String trend = "";
            if (hasTrend) trend = "trendlines: { 0: {} } ";

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
                    "      legend:'none',\n" +trend+
                    "    };\n" +
                    "\n" +
                    "    var chart = new google.visualization.LineChart(document.getElementById('chart_div'));\n" +
                    "\n" +
                    "    chart.draw(data, options);\n" +
                    "  }\n" +
                    "    </script>\n" +
                    "  </head>\n" +
                    "  <body>\n" +
                    "    <div id=\"chart_div\" style=\"width: 400px; height: 200px;\"></div>\n" +
                    "  </body>\n" +
                    "</html>";
            return html;

        }

        @Override
        protected void onPostExecute(String result){
        }
    }


    public class ListAdapter extends BaseAdapter {
        private Context context;
        private ArrayList<String> stocks;

        public ListAdapter(Context context, ArrayList<String> stocks) {
            this.context = context;
            this.stocks = stocks;
        }

        @Override
        public int getCount() {
            return stocks.size();
        }

        @Override
        public Object getItem(int position) {
            return stocks.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            if (view == null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.simple_list_item_2, null);
            }
            TextView textView = view.findViewById(R.id.stockSymbol);
            textView.setText(stocks.get(position));
            ImageView imageView = view.findViewById(R.id.trashCanImageId);
            imageView.setImageResource(R.drawable.trash_can);
            textView.setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View v) {
                         // get the stock symbol
                         String stockSymbol = textView.getText().toString();
                         tickerGraphed = stockSymbol;
                         titleTextView.setText(tickerGraphed+" Chart");
                         notifyNewsOfStockChange(stockSymbol);
                         alertViewModel(tickerGraphed);
                     }
                 });
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // get the textview
                    // get the text
                    String stockSymbol = textView.getText().toString();
                    // remove the stock from the list
                    stocks.remove(stockSymbol);
                    // update the list
                    notifyDataSetChanged();
                    File file = new File(containerActivity.getFilesDir(), "stocks.txt");
                    ArrayList <String> currentStocks = new ArrayList<String>();
                    try {
                        BufferedReader reader = new BufferedReader(new FileReader(file));
                        for (String line; (line = reader.readLine()) != null;) {
                            // check if element is the deleted one
                            if (!line.trim().equals(stockSymbol)) {
                                currentStocks.add(line.trim());
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    try {
                        PrintWriter writer = new PrintWriter(file);
                        writer.print("");
                        writer.close();
                        BufferedWriter br = new BufferedWriter(new FileWriter(file, true));
                        System.out.println("testingnngngnng");
                        for (int i = 0 ; i < currentStocks.size() ;i++) {
                            br.write(currentStocks.get(i) + "\n");
                        }
                        br.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            });
            return view;
        }
    }

}