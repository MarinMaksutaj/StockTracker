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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HelpFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HelpFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public Activity containerActivity = null;
    private ConstraintLayout layout;

    public HelpFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HelpFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HelpFragment newInstance(String param1, String param2) {
        HelpFragment fragment = new HelpFragment();
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
        View view = inflater.inflate(R.layout.fragment_help, container, false);
        layout = view.findViewById(R.id.helpFragment);
        System.out.println("help created");
        return view;
    }
    @Override
    public void onResume() {
        super.onResume();
        //exiting animation
        //if not remained in the same one
        animateEntrance();
    }

    @Override
    public void onPause() {
        super.onPause();
        //exiting animation
        //if not remained in the same one
        //animateExit();
    }
    public void setContainerActivity(Activity containerActivity){
        this.containerActivity = containerActivity;
    }

    private void animateEntrance() {
        SharedViewModel model = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        System.out.println("help start animation");
        int from = model.getFrom().getValue();
        int to = 4;
        if(from == to ) return;
        DisplayMetrics displayMetrics = new DisplayMetrics();
        containerActivity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        ObjectAnimator animatorX = ObjectAnimator.ofFloat(layout, "translationX", width);
        animatorX.setDuration(0); // Milliseconds
        animatorX.start();
        animatorX = ObjectAnimator.ofFloat(layout, "translationX", 0);
        animatorX.setDuration(300); // Milliseconds
        animatorX.start();
        model.setFrom(4);
        System.out.println("entry layout related "+ layout.toString());
    }

    public void animateExit() {
        SharedViewModel model = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        int from = 4;
        int to = model.getTo().getValue();
        if( from == to ) return;
        DisplayMetrics displayMetrics = new DisplayMetrics();
        containerActivity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        ObjectAnimator animatorX = ObjectAnimator.ofFloat(layout, "translationX", 0);
        animatorX.setDuration(300); // Milliseconds
        animatorX.start();
        System.out.println("exit layout related "+ layout.toString());
    }

}