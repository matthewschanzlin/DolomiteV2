package com.example.dolomitev2;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;


public class SearchFragment extends Fragment {
    LinearLayout LinearSearchContainer;
    ListView stockList;
    EditText searchBar;
    String searchTerm;

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
        searchBar = view.findViewById(R.id.searchSearchBar);
        stockList = view.findViewById(R.id.searchStockList);
        searchBar.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) &&
                        (i == KeyEvent.KEYCODE_ENTER)) {
                    searchTerm = searchBar.getText().toString();
                    Log.d("searchTerm", searchTerm);
                    hideKeyboard(view);
                    return true;
                }
                return false;
            }
        }
        );

        searchBar.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (! hasFocus) {
                    hideKeyboard(view);
                }
            }
        });

        stockList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });

        return view;
    }

    private void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
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
