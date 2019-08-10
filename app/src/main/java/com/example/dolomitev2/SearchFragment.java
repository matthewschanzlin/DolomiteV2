package com.example.dolomitev2;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;


public class SearchFragment extends Fragment {
    LinearLayout LinearSearchContainer;

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        LinearSearchContainer = view.findViewById(R.id.LinearSearchContainer);
        return view;
    }

    /*
    @Override
    public Animator onCreateAnimator(int transit, boolean enter, int nextAnim) {

        final int animatorId = (enter) ? R.animator.slide_up : R.animator.slide_down;
        final Animator anim = AnimatorInflater.loadAnimator(getActivity(), animatorId);
        anim.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationStart(Animator animation) {
                Log.d("START", "HELLO");
                Toast.makeText(getContext(), "end", Toast.LENGTH_SHORT);

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                Log.d("END", "HELLO");
                Toast.makeText(getContext(), "start", Toast.LENGTH_SHORT);
               // LinearSearchContainer.setVisibility(View.GONE);
            }
        });

        return anim;


    }
    */
}
