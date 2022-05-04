package com.example.stocktracker;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.os.Bundle;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/*
 * @author: Hector Beltran & Marin Maksutaj
 * @description: This is the HelpFragment class. It is used to display the help
 *              screen. It provides the users of the app with a simple user manual.
 */
public class HelpFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    public Activity containerActivity = null;
    private ConstraintLayout layout;

    /*
     * The constructor for the class.
    */
    public HelpFragment() {
        // Required empty public constructor
    }

    /*
    * newInstance method for the class. It is used to create a new instance of the
    * class with the given parameters.
    */
    public static HelpFragment newInstance(String param1, String param2) {
        HelpFragment fragment = new HelpFragment();
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
    * The oncreateView method for the class. It is used to create the view for the
    * fragment.
    */
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_help, container, false);
        layout = view.findViewById(R.id.helpFragment);
        return view;
    }
    @Override
    /*
    * onResume method for the class. It is triggered whenever the fragment is resumed.
    */
    public void onResume() {
        super.onResume();
        animateEntrance();
    }

    @Override
    /*
    * onPause method for the class. It is triggered whenever the fragment is paused.
    */
    public void onPause() {
        super.onPause();
    }
    /*
    * This method sets the containerActivity to the given activity.
    */
    public void setContainerActivity(Activity containerActivity){
        this.containerActivity = containerActivity;
    }

    /*
    * The animateEntrance method for the class. It is used to animate
    * the entrance of the fragment.
    */
    private void animateEntrance() {
        SharedViewModel model = new ViewModelProvider(requireActivity())
                .get(SharedViewModel.class);
        int from = model.getFrom().getValue();
        int to = 4;
        if(from == to ) return;
        DisplayMetrics displayMetrics = new DisplayMetrics();
        containerActivity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        ObjectAnimator animatorX = ObjectAnimator.ofFloat(layout,
                "translationX", width);
        animatorX.setDuration(0); // Milliseconds
        animatorX.start();
        animatorX = ObjectAnimator.ofFloat(layout, "translationX", 0);
        animatorX.setDuration(300); // Milliseconds
        animatorX.start();
        model.setFrom(4);
    }
    
    /*
    * The animateExit method for the class. It is used to animate the exit of the fragment.
    */
    public void animateExit() {
        SharedViewModel model = new ViewModelProvider(requireActivity()).
                get(SharedViewModel.class);
        int from = 4;
        int to = model.getTo().getValue();
        if( from == to ) return;
        DisplayMetrics displayMetrics = new DisplayMetrics();
        containerActivity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        ObjectAnimator animatorX = ObjectAnimator.ofFloat(layout,
                "translationX", 0);
        animatorX.setDuration(300); // Milliseconds
        animatorX.start();
    }

}