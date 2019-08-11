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

import java.util.ArrayList;

import utils.CustomStockAdapter;
import utils.StockAdapterItem;
import utils.stock_search_adapter_item;
import utils.CustomStockSearchAdapter;

public class SearchFragment extends Fragment {
    LinearLayout LinearSearchContainer;
    ListView stockList;
    EditText searchBar;
    String searchTerm;

    ArrayList<stock_search_adapter_item> searchedStocks;
    CustomStockSearchAdapter searchedCustomStockAdapter;

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

        searchedStocks = new ArrayList<>();
        searchedCustomStockAdapter = new CustomStockSearchAdapter(getContext(), searchedStocks);
        stockList.setAdapter(searchedCustomStockAdapter);

        searchBar.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) &&
                        (i == KeyEvent.KEYCODE_ENTER)) {
                    searchTerm = searchBar.getText().toString();
                    Log.d("searchTerm", searchTerm);
                    hideKeyboard(view);
                    populateSearchedStocks();
                    searchedCustomStockAdapter.notifyDataSetChanged();
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

    /**
     * test data
     */
    private void populateSearchedStocks() {

        // different implementation necessary after API integration
        searchedStocks.add(new stock_search_adapter_item("AAPL", "Apple", "$208.42", "+2.4%"));
        searchedStocks.add(new stock_search_adapter_item("AMZN", "Amazon", "$1232.32", "-0.4%"));
        searchedStocks.add(new stock_search_adapter_item("GOOG", "Google", "$485.25", "-8.5%"));
        searchedStocks.add(new stock_search_adapter_item("SNAP", "Snapchat", "$28.53", "+5.4%"));
        searchedStocks.add(new stock_search_adapter_item("WALL", "Walmart", "$99.01", "-0.8%"));
        searchedStocks.add(new stock_search_adapter_item("REBK", "Reebok", "$42.69", "+1.5%"));
    }

    /**
     * pulls the keyboard down when necessary
     * @param view
     */
    private void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}
