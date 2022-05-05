package com.example.stocktracker;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/*
 * @author: Hector Beltran & Marin Maksutaj
 * @description: This is the main fragment that will be 
 *               used to display the chart and the favorite stocks list.
 * 
 */
public class ChartFragment extends Fragment {

    View view = null;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private String tickerGraphed;
    public Activity containerActivity = null;
    private WebView webView;
    private WebView webView2;
    private WebView tempWebView;
    private TextView titleTextView;
    private GraphAPIManager grapher;
    private SharedViewModel model;
    private ConstraintLayout layout;
    private boolean refreshes;
    private boolean firstWebView;


    /*
    * This is the constructor for the fragment
     */
    public ChartFragment() {
        // Required empty public constructor
    }

    /**
     * This factory method is used to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChartFragment.
     */
    public static ChartFragment newInstance(String param1, String param2) {
        ChartFragment fragment = new ChartFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    /*
    * This method is called when the fragment is created. It will
    * create the view and set the view model.
    */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        model = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

    }

    @Override
    /*
    * The onResume method is called when the fragment is resumed. It will refresh the
    * graph if the user has selected to do so. It will also set the ticker to the ticker
    * that is currently selected. It will also set the title to the ticker that is currently
    * selected.
    */
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
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        ListView listView = (ListView) containerActivity.findViewById(R.id.stockListView);
        // create a custom adapter
        listView.setAdapter(new ListAdapter(containerActivity, stockSymbol));
        //restart grapher if we stopped it to save resources
        if( grapher.isCancelled() || grapher.getStatus() == AsyncTask.Status.PENDING){
            grapher = new GraphAPIManager();
            grapher.execute();
        }
        animateEntrance();

    }

    /*
    * This method converts an arraylist of strings to a string array.
    */
    public String[] ArrayListToStringArray(ArrayList<String> stockSymbol) {
        String [] stockSymbolArray = new String[stockSymbol.size()];
        for (int i = 0; i < stockSymbol.size(); i++) {
            stockSymbolArray[i] = stockSymbol.get(i);
        }
        return stockSymbolArray;

    }


    @Override
    /*
    * Method called whenever the fragment view is first created. It will
    * create the view and set the view model.
    */
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_chart, container, false);
        webView = (WebView) view.findViewById(R.id.webview);
        webView2 = (WebView) view.findViewById(R.id.webview2);
        titleTextView = (TextView) view.findViewById(R.id.textView6);
        layout = view.findViewById(R.id.chartFragment);
        grapher = new GraphAPIManager();
        firstWebView = false;

        //get the correct stock to graph
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
        if (grapher.isCancelled()||grapher.getStatus() == AsyncTask.Status.PENDING  ) {
            grapher.execute();
        }
        return view;
    }

    /*
    * This method is used to set the containerActivity.
    */
    public void setContainerActivity(Activity containerActivity){
        this.containerActivity = containerActivity;
    }

    /*
    * This method is used to alert the user that the stock they are graphing has changed.
    */
    private void alertViewModel(String stock){
        model.setStock(stock);
    }

    @Override
    /*
    * This method is called whenever the fragment is stopped.
    */
    public void onStop() {
        super.onStop();
        //pause grapher to save resources while we share chart or screen is minimized
        grapher.cancel(true);
    }

    /*
    * This method is used to animate the change of the fragments.
    */
    private void animateEntrance() {
        SharedViewModel model = new ViewModelProvider(requireActivity()).
                get(SharedViewModel.class);
        int from = model.getFrom().getValue();
        int to = 0;
        if(from == to ) return;
        DisplayMetrics displayMetrics = new DisplayMetrics();
        containerActivity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        if(from > 0 ) width = width*(-1);
        ObjectAnimator animatorX = ObjectAnimator.ofFloat(layout,
                "translationX", width);
        animatorX.setDuration(0); // Milliseconds
        animatorX.start();
        animatorX = ObjectAnimator.ofFloat(layout, "translationX", 0);
        animatorX.setDuration(300); // Milliseconds
        animatorX.start();
        model.setFrom(0);
    }

    /*
    * This method is used to notify the news fragment of the stock change.
    */
    public void notifyNewsOfStockChange(String ticker){

        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.stock_graphed), ticker);
        editor.apply();
    }

    /*
    * This is the async task that will get the stock data and graph it.
    */
    private class GraphAPIManager extends AsyncTask<String, String, String> {
        String html = "";


        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        /*
        * The do in background method.
        */
        protected String doInBackground(String... params) {
            //set proper title
            titleTextView.setText(tickerGraphed + " Chart");

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Calendar cal = Calendar.getInstance();

            //remove 4 days to pool information from at least the last 4 days
            cal.add(Calendar.DATE, -7);
            String date1 = dateFormat.format(cal.getTime());
            String date2 = dateFormat.format(new Date());
            SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
            String tickerDuration = "minute";
            int fetchNumber = sharedPref.getInt(getString(R.string.tickers_setting), 10);
            if(sharedPref.getBoolean(getString(R.string.hourly_setting), false)){
                tickerDuration = "hour";
                fetchNumber = fetchNumber *60;
            }
            refreshes = sharedPref.getBoolean(getString(R.string.refreshes_toggle), false);
            while (true) {
                if(grapher.isCancelled()) return null;
                //we need the proper api_key access to really test the url,
                // we fetch end of the day data.
                String apiCall = "https://api.polygon.io/v2/aggs/ticker/" + tickerGraphed +
                        "/range/1/"+tickerDuration+"/" + date1 + "/" +
                        date2 + "?sort=desc&limit="+fetchNumber+"&apiKey=" +
                        getResources().getString(R.string.API_KEY);
                try {
                    URL url = new URL(apiCall);
                    BufferedReader br = new BufferedReader(new InputStreamReader
                            (url.openStream(), "UTF-8"));
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
                    html="";
                    if (isLined) html = lineGraphGenerator(data, hasTrend);
                    else html = candleGraphGenerator(data, hasTrend);

                    // update the webview on the UI thread
                    containerActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if ( firstWebView){
                                webView2.getSettings().setJavaScriptEnabled(true);
                                webView2.loadData(html, "text/html", "UTF-8");
                            }else {
                                webView.getSettings().setJavaScriptEnabled(true);
                                webView.loadData(html, "text/html", "UTF-8");
                            }
                        }
                    });
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
                if(!refreshes){
                    //grapher.cancel(true);
                    return null;
                }

                try {
                    //we wait some time before next call
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                //alternate alphas to show proper webview already loaded
                if ( firstWebView){
                    webView2.setAlpha(1.0f);
                    webView.setAlpha(0.0f);
                }else {
                    webView.setAlpha(1.0f);
                    webView2.setAlpha(0.0f);
                }
                firstWebView = !firstWebView;

            }
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        /*
        * This method generates the candle graph.
        */
        private String candleGraphGenerator(JSONArray data,
                                            boolean hasTrend) throws JSONException {
            String stockDataString = "";

            for (int i = 0; i < data.length(); i++) {
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
            String trend = "";
            if (hasTrend) trend = "trendlines: { 0: {} } ";

            String html = "<html>\n" +
                    "  <head>\n" +
                    "    <script type=\"text/javascript\" src=\"" +
                    "https://www.gstatic.com/charts/loader.js\"></script>\n" +
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
                    "      'chartArea': {'width': '80%', 'height': '80%'},"+
                    "      legend:'none',\n" +trend+
                    "    };\n" +
                    "\n" +
                    "    var chart = new google.visualization." +
                    "CandlestickChart(document.getElementById('chart_div'));\n" +
                    "\n" +
                    "    chart.draw(data, options);\n" +
                    "  }\n" +
                    "    </script>\n" +
                    "  </head>\n" +
                    "  <body>\n" +
                    "    <div id=\"chart_div\" style=\"width: 100%; height: 100%;\"></div>\n" +
                    "  </body>\n" +
                    "</html>";
            return html;

        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        /*
        * This method generates the line graph.
        */
        private String lineGraphGenerator(JSONArray data, boolean hasTrend)
                throws JSONException {
            String stockDataString = "";


            for (int i = 0; i < data.length(); i++) {
                stockDataString += "[ new Date(";
                JSONObject stockData = data.getJSONObject(i);
                String time = stockData.getString("t");
                stockDataString += time + "), ";
                String close = stockData.getString("c");
                stockDataString += close ;
                stockDataString += "],\n";
            }
            String trend = "";
            if (hasTrend) trend = "trendlines: { 0: {} } ";

            String html = "<html>\n" +
                    "  <head>\n" +
                    "    <script type=\"text/javascript\" src=\"https://www.gstatic." +
                    "com/charts/loader.js\"></script>\n" +
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
                    "      'chartArea': {'width': '80%', 'height': '80%'},"+
                    "      legend:'none',\n" +trend+
                    "    };\n" +
                    "\n" +
                    "    var chart = new google.visualization.LineChart" +
                    "(document.getElementById('chart_div'));\n" +
                    "\n" +
                    "    chart.draw(data, options);\n" +
                    "  }\n" +
                    "    </script>\n" +
                    "  </head>\n" +
                    "  <body>\n" +
                    "    <div id=\"chart_div\" style=\"width: 100%; height: 100%;\"></div>\n" +
                    "  </body>\n" +
                    "</html>";
            return html;
        }

    }

    /*
    * This class is used to display the favorite stocks list.
    */
    public class ListAdapter extends BaseAdapter {
        private Context context;
        private ArrayList<String> stocks;

        /*
        * This is the constructor for the ListAdapter class.
        */
        public ListAdapter(Context context, ArrayList<String> stocks) {
            this.context = context;
            this.stocks = stocks;
        }

        @Override
        /*
        * This is the method that returns the number of items in the list.
        */
        public int getCount() {
            return stocks.size();
        }

        @Override
        /*
        * This is the method that returns the item at the specified position.
        */
        public Object getItem(int position) {
            return stocks.get(position);
        }

        @Override
        /*
        * This is the method that returns the ID of the item at the specified position.
        */
        public long getItemId(int position) {
            return position;
        }

        @Override
        /*
        * This is the method that returns the view for the item at the specified position.
        */
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            if (view == null) {
                LayoutInflater inflater = (LayoutInflater) context.
                        getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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

                         titleTextView.setText(tickerGraphed + " Chart");

                         notifyNewsOfStockChange(stockSymbol);
                         alertViewModel(tickerGraphed);
                         if(!refreshes) {
                             grapher = new GraphAPIManager();
                             grapher.execute();
                         }
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
                        BufferedWriter br = new BufferedWriter
                                (new FileWriter(file, true));
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
