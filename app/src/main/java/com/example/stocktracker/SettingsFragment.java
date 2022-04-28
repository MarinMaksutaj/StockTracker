package com.example.stocktracker;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ToggleButton;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public Activity containerActivity = null;

    public SettingsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();
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
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        //we read all settings from same sharedPrefences variable
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        //set up for first option in settings
        //first we read current saved setting from shared pref
        boolean state = sharedPref.getBoolean(getString(R.string.line_graph), false);
        ToggleButton firstSetting = (ToggleButton) view.findViewById(R.id.linegraphSettingToggle);
        firstSetting.setChecked(state);
        firstSetting.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //we then change the setting once toggle is clicked
                SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putBoolean(getString(R.string.line_graph), firstSetting.isChecked());
                editor.apply();
            }
        });

        //set up for second option in settings
        //first we read current saved setting from shared pref
        boolean lengthy = sharedPref.getBoolean(getString(R.string.length_news), false);
        ToggleButton secondSetting = (ToggleButton) view.findViewById(R.id.lengthSettingToggle);
        secondSetting.setChecked(lengthy);
        secondSetting.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //we then change the setting once toggle is clicked
                SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putBoolean(getString(R.string.length_news), secondSetting.isChecked());
                editor.apply();
            }
        });

        //set up for third option in settings
        //first we read current saved setting from shared pref
        boolean trend = sharedPref.getBoolean(getString(R.string.trend_toggle), false);
        ToggleButton thirdSetting = (ToggleButton) view.findViewById(R.id.trendSettingsToggle);
        thirdSetting.setChecked(trend);
        thirdSetting.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //we then change the setting once toggle is clicked
                SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putBoolean(getString(R.string.trend_toggle), thirdSetting.isChecked());
                editor.apply();
            }
        });

        //set up for fourth option in settings
        //first we read current saved setting from shared pref
        boolean hourly = sharedPref.getBoolean(getString(R.string.hourly_setting), false);
        ToggleButton fourthSetting = (ToggleButton) view.findViewById(R.id.hourlySettingToggle);
        fourthSetting.setChecked(hourly);
        fourthSetting.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //we then change the setting once toggle is clicked
                SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putBoolean(getString(R.string.hourly_setting), fourthSetting.isChecked());
                editor.apply();
            }
        });

        return view;
    }
    public void setContainerActivity(Activity containerActivity){
        this.containerActivity = containerActivity;
    }
}