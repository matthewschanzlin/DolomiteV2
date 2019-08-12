package com.example.dolomitev2;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Context;
import android.graphics.PointF;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.JsonReader;
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

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import utils.ApiManager;
import utils.AsyncTaskComplete;
import utils.CustomStockAdapter;
import utils.StockAdapterItem;
import utils.stock_search_adapter_item;
import utils.CustomStockSearchAdapter;

public class SearchFragment extends Fragment implements AsyncTaskComplete {
    LinearLayout LinearSearchContainer;
    ListView stockList;
    EditText searchBar;
    String searchTerm;
    ApiManager manager;
    HashMap<String, String> searchPrices;
    int limit;

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
        manager = new ApiManager(getContext(), this);
        searchPrices = new HashMap<>();
        limit = 0;

        searchedStocks = new ArrayList<>();
        searchedCustomStockAdapter = new CustomStockSearchAdapter(getContext(), searchedStocks);
        stockList.setAdapter(searchedCustomStockAdapter);

        searchBar.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) &&
                        (i == KeyEvent.KEYCODE_ENTER)) {
                    searchTerm = searchBar.getText().toString();

                    hideKeyboard(view);
                    limit = 0;
                    searchPrices.clear();
                    search(searchTerm);
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

    private void search(String searchTerm) {
        searchedStocks.removeAll(searchedStocks);
        manager.getSearchResults(searchTerm);
    }

    public void OnSearchComplete(ArrayList<String> results) {
        for (int i = 0; i < results.size(); i++) {
            if (i % 2 == 0) {
                searchPrices.put(results.get(i), "0");
            }
        }
        getPrices(results);
    }

    public void OnPointsComplete(ArrayList<PointF> results) {

    }

    public void OnPriceComplete(ArrayList<String> result) {
        for (int i = 0; i < result.size(); i++) {
            if (i % 2 == 0) {
                searchPrices.put(result.get(i), result.get(i + 1));
            }
        }
        populateSearchedStocks(searchPrices);
    }

    public void getPrices(ArrayList<String> results) {
        ArrayList<String> symbols = new ArrayList<>();
        for (int i = 0; i < results.size(); i++) {
            if (i % 2 == 0) {
                symbols.add(results.get(i));
            }
        }
        manager.getPrices(symbols);
    }

    private void populateSearchedStocks(Map mp) {

        Iterator it = mp.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            if (new Float(pair.getValue().toString()) > 0.1) {
                searchedStocks.add(new stock_search_adapter_item(pair.getKey().toString(), "Company", new Float(pair.getValue().toString()), (float) 2.4));
            }
            it.remove(); // avoids a ConcurrentModificationException
        }

        searchedCustomStockAdapter.notifyDataSetChanged();
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
