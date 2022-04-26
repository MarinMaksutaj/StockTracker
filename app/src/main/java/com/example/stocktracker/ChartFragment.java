package com.example.stocktracker;

import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChartFragment extends Fragment {

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        
        View view = inflater.inflate(R.layout.fragment_chart, container, false);
        WebView webView = view.findViewById(R.id.webview);
        String html = "<html>\n" +
                "  <head>\n" +
                "    <script type=\"text/javascript\" src=\"https://www.gstatic.com/charts/loader.js\"></script>\n" +
                "    <script type=\"text/javascript\">\n" +
                "      google.charts.load('current', {'packages':['corechart']});\n" +
                "      google.charts.setOnLoadCallback(drawChart);\n" +
                "\n" +
                "  function drawChart() {\n" +
                "    var data = google.visualization.arrayToDataTable([\n" +
                "      ['Mon', 20, 28, 38, 45],\n" +
                "      ['Tue', 31, 38, 55, 66],\n" +
                "      ['Wed', 50, 55, 77, 80],\n" +
                "      ['Thu', 77, 77, 66, 50],\n" +
                "      ['Fri', 68, 66, 22, 15]\n" +
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
        webView.getSettings().setJavaScriptEnabled(true);
        // enable javascript
        // allow the webview to load html
        webView.loadData(html, "text/html", "UTF-8");
        
        return view;
    }

    public void setContainerActivity(Activity containerActivity){
        this.containerActivity = containerActivity;
    }
}