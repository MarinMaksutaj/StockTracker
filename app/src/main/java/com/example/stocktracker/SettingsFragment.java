package com.example.stocktracker;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;

/*
 * @author: Hector Beltran & Marin Maksutaj
 * @description: This is the SettingsFragment class. It is used to display the settings
 *             screen. It provides the users of the app with a list of settings.
 */
public class SettingsFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    public Activity containerActivity = null;
    private ConstraintLayout layout;
    private TextView tickerText;

    /*
    * This is the constructor for the class.
    */
    public SettingsFragment() {
        // Required empty public constructor
    }

    /*
    * This is the newInstance method for the class.
    * It is used to create a new instance of the
    * class with the given parameters.
    */
    public static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    /*
    * This is the onCreate method for the class. It is used to initialize the class.
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
    * This is the oncreateView method for the class. It is used to create the view for the
    * fragment.
    */
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        //Animate entrance
        layout = view.findViewById(R.id.settingsFragment);
        //tickers Text
        tickerText = view.findViewById(R.id.tickersPerChartText);
        //we read all settings from same sharedPrefences variable
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        //set up for first option in settings
        //first we read current saved setting from shared pref
        boolean state = sharedPref.getBoolean(getString(R.string.line_graph), false);
        ToggleButton firstSetting = (ToggleButton)
                view.findViewById(R.id.linegraphSettingToggle);
        firstSetting.setChecked(state);
        firstSetting.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //we then change the setting once toggle is clicked
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putBoolean(getString(R.string.line_graph), firstSetting.isChecked());
                editor.apply();
            }
        });

        //set up for second option in settings
        //first we read current saved setting from shared pref
        boolean lengthy = sharedPref.getBoolean(getString(R.string.length_news), false);
        ToggleButton secondSetting = (ToggleButton)
                view.findViewById(R.id.lengthSettingToggle);
        secondSetting.setChecked(lengthy);
        secondSetting.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //we then change the setting once toggle is clicked
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putBoolean(getString(R.string.length_news), secondSetting.isChecked());
                editor.apply();
            }
        });

        //set up for third option in settings
        //first we read current saved setting from shared pref
        boolean trend = sharedPref.getBoolean(getString(R.string.trend_toggle), false);
        ToggleButton thirdSetting = (ToggleButton)
                view.findViewById(R.id.trendSettingsToggle);
        thirdSetting.setChecked(trend);
        thirdSetting.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //we then change the setting once toggle is clicked
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putBoolean(getString(R.string.trend_toggle), thirdSetting.isChecked());
                editor.apply();
            }
        });

        //set up for fourth option in settings
        //first we read current saved setting from shared pref
        boolean hourly = sharedPref.getBoolean(getString(R.string.hourly_setting), false);
        ToggleButton fourthSetting = (ToggleButton)
                view.findViewById(R.id.hourlySettingToggle);
        fourthSetting.setChecked(hourly);
        fourthSetting.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //we then change the setting once toggle is clicked
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putBoolean(getString(R.string.hourly_setting), fourthSetting.isChecked());
                editor.apply();
            }
        });

        //set up for fifth option in settings
        //Settings for refreshign or not the graphs.
        boolean refreshes = sharedPref.getBoolean(getString(R.string.refreshes_toggle), false);
        ToggleButton fifthToggle = (ToggleButton)
                view.findViewById(R.id.refreshSettingToggle);
        fifthToggle.setChecked(refreshes);
        fifthToggle.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //we then change the setting once toggle is clicked
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putBoolean(getString(R.string.refreshes_toggle), fifthToggle.isChecked());
                editor.apply();
            }
        });

        //set up for sixth option in settings
        //first we read current saved setting from shared pref
        int tickersPerChart = sharedPref.getInt(getString(R.string.tickers_setting), 10);
        SeekBar seekBar = (SeekBar) view.findViewById(R.id.tickersPerChartSeekBar);
        seekBar.setProgress( tickersPerChart );
        tickerText.setText( getResources().getString(R.string.tickersPerChart)+tickersPerChart);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress , boolean fromUser) {
                //we change the new value to display
                tickerText.setText( getResources().getString(R.string.tickersPerChart)+progress);
                //we then change the stored value for tickers to display
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putInt(getString(R.string.tickers_setting), progress);
                editor.apply();

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        return view;
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
    * The onPause method for the class. It is triggered when the fragment is paused.
    */
    public void onPause() {
        super.onPause();
        animateExit();
    }
    
    /*
    * The animateEntrance method for the class.
    * It is used to animate the entrance of the fragment.
    */
    private void animateEntrance() {
        SharedViewModel model = new ViewModelProvider(requireActivity()).
                get(SharedViewModel.class);
        int from = model.getFrom().getValue();
        int to = 3;
        if(from == to ) return;
        DisplayMetrics displayMetrics = new DisplayMetrics();
        containerActivity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        if(from > 3 ) width = width*(-1);
        ObjectAnimator animatorX = ObjectAnimator.ofFloat(layout,
                "translationX", width);
        animatorX.setDuration(0); // Milliseconds
        animatorX.start();
        animatorX = ObjectAnimator.ofFloat(layout, "translationX", 0);
        animatorX.setDuration(300); // Milliseconds
        animatorX.start();
        model.setFrom(3);
    }

    /*
    * The animateExit method for the class. It is used to animate the exit of the fragment.
    */
    public void animateExit() {
        SharedViewModel model = new ViewModelProvider(requireActivity()).
                get(SharedViewModel.class);
        int from = 3;
        int to = model.getTo().getValue();
        if( to == 3 ) return;
        DisplayMetrics displayMetrics = new DisplayMetrics();
        containerActivity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        if(to > 3 ) width = width*(-1);
        ObjectAnimator animatorX = ObjectAnimator.ofFloat(layout,
                "translationX", width);
        animatorX.setDuration(500); // Milliseconds
        animatorX.start();

    }

    /*
    * The setContainerActivity method for the class. It is used to set the container activity.
    */
    public void setContainerActivity(Activity containerActivity){
        this.containerActivity = containerActivity;
    }
}