package com.example.stocktracker;

import android.app.Activity;
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
import java.net.URL;
import java.util.ArrayList;

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
        // get the button view
        Button button = (Button) view.findViewById(R.id.button);
        // set a click listener for the button
        button.setOnClickListener(new View.OnClickListener() {
            // the code in this method will be executed when the numbers category is clicked on.
            @Override
            public void onClick(View view) {
                // get the EditText and its text
                EditText editText = (EditText) containerActivity.findViewById(R.id.editText);
                String text = editText.getText().toString();
                if (text.length() == 0) {
                    return;
                }
                // make an API call using a thread
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // make the API call
                        String result = "";
                        try {
                            URL url = new URL("https://financialmodelingprep.com/api/v3/search?query=" + text + "&limit=10&exchange=NASDAQ&apikey=a73dd0c1285faf8c77480af5d48ef364");
                            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
                            for (String line; (line = reader.readLine()) != null;) {
                                result += line;
                            }
                            reader.close();
                            System.out.println(result);
                            JSONArray jsonArray = new JSONArray(result);
                            String resultArr [] = new String[jsonArray.length()];
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String symbol = jsonObject.getString("symbol");
                                String name = jsonObject.getString("name");
                                resultArr[i] = symbol + " " + name;
                            }
                            // update the UI
                            containerActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ListView listView = (ListView) containerActivity.findViewById(R.id.listView);
                                    listView.setAdapter(new ArrayAdapter<String>(containerActivity, android.R.layout.simple_list_item_1, resultArr));
                                    // set a listener for listView items
                                    listView.setOnItemClickListener(new ListView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                            TextView textView = (TextView) view;
                                            String text = textView.getText().toString();
                                            String symbol = text.substring(0, text.indexOf(" "));
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
                        }
                    });
                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

            }
        });
        return view;
    }
    public void setContainerActivity(Activity containerActivity){
        this.containerActivity = containerActivity;
    }
}