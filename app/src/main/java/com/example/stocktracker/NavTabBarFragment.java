package com.example.stocktracker;

import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NavTabBarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NavTabBarFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public Activity containerActivity = null;

    public NavTabBarFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NavTabBarFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NavTabBarFragment newInstance(String param1, String param2) {
        NavTabBarFragment fragment = new NavTabBarFragment();
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
        View v = inflater.inflate(R.layout.fragment_nav_tab_bar, container, false);
        Button chartButton = (Button) v.findViewById(R.id.chartNavButton);
        Button searchButton = (Button) v.findViewById(R.id.searchNavButton);
        Button newsButton = (Button) v.findViewById(R.id.newsNavButton);
        Button settingsButton = (Button) v.findViewById(R.id.settingsNavButton);
        Button manualButton = (Button) v.findViewById(R.id.helpNavButton);

        int width = chartButton.getWidth();
        System.out.println(width); // read width to set up buttons height.
        return v;
    }

    public void setContainerActivity(Activity containerActivity){
        this.containerActivity = containerActivity;
    }



}