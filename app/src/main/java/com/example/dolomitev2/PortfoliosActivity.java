package com.example.dolomitev2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import utils.CustomPortfolioCardAdapter;
import utils.DrawView;
import utils.PortfolioCardAdapterItem;

public class PortfoliosActivity extends AppCompatActivity {

    Button editButton;
    GridView portfoliosGrid;
    ArrayList<PortfolioCardAdapterItem> portfolios;
    CustomPortfolioCardAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_portfolios);
        initData();

        portfoliosGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(PortfoliosActivity.this, PortfolioDetailActivity.class);
                intent.putExtra("Portfolio name", portfolios.get(i).getName());
                startActivity(intent);
            }
        });
    }

    void initData() {
        editButton = findViewById(R.id.portfoliosEditButton);
        portfoliosGrid = findViewById(R.id.portfoliosGridView);

        portfolios = new ArrayList<>();
        populatePortfolios();
        adapter = new CustomPortfolioCardAdapter(this, portfolios);
        portfoliosGrid.setAdapter(adapter);

    }

    void populatePortfolios() {

        ArrayList<Point> points = new ArrayList<>();

        points.add(new Point(10, 350));
        points.add(new Point(40, 310));
        points.add(new Point(190, 400));

        portfolios.add(new PortfolioCardAdapterItem(" ", "", "New Portfolio", points
                ));
        portfolios.add(new PortfolioCardAdapterItem("-0.2%", "$10,235", "Stay Woke",
                points));
        portfolios.add(new PortfolioCardAdapterItem("-0.2%", "$10,235", "Stay Woke",
                points));

    }
}