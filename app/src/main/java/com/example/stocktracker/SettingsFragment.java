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
        //Read our current settings to set the buttons in the proper way
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        //set up for first option in settings
        boolean state = sharedPref.getBoolean(getString(R.string.saved_color_setting), false);
        ToggleButton firstSetting = (ToggleButton) view.findViewById(R.id.firstSettingToggle);
        firstSetting.setChecked(state);
        firstSetting.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                boolean ans = firstSetting.isChecked();
                editor.putBoolean(getString(R.string.saved_color_setting), ans);
                editor.apply();
            }
        });

        //set up for second option in settings
        boolean lengthy = sharedPref.getBoolean(getString(R.string.length_news), false);
        ToggleButton secondSetting = (ToggleButton) view.findViewById(R.id.lengthSettingToggle);
        secondSetting.setChecked(lengthy);
        secondSetting.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                boolean ans = secondSetting.isChecked();
                editor.putBoolean(getString(R.string.length_news), ans);
                editor.apply();
            }
        });



        return view;
    }
    public void setContainerActivity(Activity containerActivity){
        this.containerActivity = containerActivity;
    }
}