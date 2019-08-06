package com.example.dolomitev2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import utils.CustomStockAdapter;
import utils.StockAdapterItem;

public class PortfolioDetailActivity extends AppCompatActivity {

    Button button1d, button5d, button1m, button3m, button6m, button1y, button5y, buttonAll;
    String portfolioName;
    TextView singlePortfolioTitle;
    ListView stockList;
    ArrayList<StockAdapterItem> stocks;
    CustomStockAdapter customStockAdapter;

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
        singlePortfolioTitle = findViewById(R.id.singlePortfolioTitle);

        portfolioName = getIntent().getStringExtra("Portfolio name");
        singlePortfolioTitle.setText(portfolioName);

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
}
