package com.example.dolomitev2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class PortfolioDetailActivity extends AppCompatActivity {

    Button button1d, button5d, button1m, button3m, button6m, button1y, button5y, buttonAll, singlePortfolioEditButton;
    String portfolioName;
    TextView singlePortfolioTitle;

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

        portfolioName = getIntent().getStringExtra("Portfolio name");
        singlePortfolioTitle.setText(portfolioName);

        singlePortfolioEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}
