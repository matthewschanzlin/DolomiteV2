package com.example.dolomitev2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import entities.AdminDAO;
import entities.AppDatabase;
import utils.CustomStockAdapter;
import utils.StockAdapterItem;

public class PortfolioDetailActivity extends AppCompatActivity {

    Button button1d, button5d, button1m, button3m, button6m, button1y, button5y, buttonAll,
            singlePortfolioEditButton, searchButton;
    String portfolioName;
    TextView singlePortfolioTitle;
    ListView stockList;
    ArrayList<StockAdapterItem> stocks;
    CustomStockAdapter customStockAdapter;
    FrameLayout SearchContainer;
    LinearLayout LinearSearchContainer;
    boolean inSearch;

    AppDatabase db;
    AdminDAO dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_portfolio_detail);

        button1d = findViewById(R.id.button1d);
        button5d = findViewById(R.id.button5d);
        button1m = findViewById(R.id.button1m);
        button3m = findViewById(R.id.button3m);
        button6m = findViewById(R.id.button6m);
        button1y = findViewById(R.id.button1y);
        button5y = findViewById(R.id.button5y);
        buttonAll = findViewById(R.id.buttonAll);
        singlePortfolioEditButton = findViewById(R.id.singlePortfolioEditButton);
        singlePortfolioTitle = findViewById(R.id.singlePortfolioTitle);
        SearchContainer = findViewById(R.id.SearchContainer);
        LinearSearchContainer = findViewById(R.id.LinearSearchContainer);
        searchButton = findViewById(R.id.singlePortfolioSearchButton);


        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "database-name").fallbackToDestructiveMigration().allowMainThreadQueries().build();
        // NOTE: MIGHT LOCK UP THREAD. SWITCH TO STATIC NESTED CLASS WHEN POSSIBLE
        dao = db.userDao();







        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                LinearSearchContainer.setVisibility(View.VISIBLE);
                inSearch = true;
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.setCustomAnimations(R.anim.slide_in_up, R.anim.slide_out_up);
                fragmentTransaction.replace(R.id.SearchContainer, new SearchFragment());

                fragmentTransaction.commit();
            }
        });


        portfolioName = getIntent().getStringExtra("Portfolio name");
        singlePortfolioTitle.setText(portfolioName);

        singlePortfolioEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });
        stockList = findViewById(R.id.singlePortfolioListView);

        stocks = new ArrayList<>();
        populateStocks();
        customStockAdapter = new CustomStockAdapter(this, stocks);
        stockList.setAdapter(customStockAdapter);
    }


    private void populateStocks() {
        stocks.add(new StockAdapterItem("AAPL", "Apple", "$208.42", "+2.4%"));
        stocks.add(new StockAdapterItem("AMZN", "Amazon", "$1232.32", "-0.4%"));
        stocks.add(new StockAdapterItem("GOOG", "Google", "$485.25", "-8.5%"));
        stocks.add(new StockAdapterItem("SNAP", "Snapchat", "$28.53", "+5.4%"));
        stocks.add(new StockAdapterItem("WALL", "Walmart", "$99.01", "-0.8%"));
        stocks.add(new StockAdapterItem("REBK", "Rebok", "$42.69", "+1.5%"));
    }

    @Override
    public void onBackPressed() {
        if (inSearch) {
            LinearSearchContainer.setVisibility(View.GONE);
            inSearch = false;

        } else {
            Intent intent = new Intent(PortfolioDetailActivity.this, PortfoliosActivity.class);
            startActivity(intent);
        }
    }

}
