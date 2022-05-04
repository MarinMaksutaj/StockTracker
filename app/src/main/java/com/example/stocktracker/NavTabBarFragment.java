package com.example.stocktracker;
import android.app.Activity;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageButton;

/*
 * @author: Hector Beltran & Marin Maksutaj
 * @description: This is the NavTabBarFragment class. It is used to display the
 *              navigation bar at the bottom of the screen.
 */
public class NavTabBarFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    public Activity containerActivity = null;

    /*
    * The constructor for the class.
    */
    public NavTabBarFragment() {
        // Required empty public constructor
    }

    /*
    * The newInstance method for the class. It is used to create a new instance of the
    * class with the given parameters.
    */
    public static NavTabBarFragment newInstance(String param1, String param2) {
        NavTabBarFragment fragment = new NavTabBarFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    /*
    * The onCreate method for the class. It is used to initialize the class.
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
    * The onCreateView method for the class. It is used to create the view for the
    * class.
    */
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_nav_tab_bar, container, false);
        return v;

    }

    /*
    * setContainerActivity method for the class. It is used to set the container activity
    * for the class.
    */
    public void setContainerActivity(Activity containerActivity){
        this.containerActivity = containerActivity;
    }
}