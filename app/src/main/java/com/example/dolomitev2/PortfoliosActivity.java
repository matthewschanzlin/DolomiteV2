package com.example.dolomitev2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import entities.AdminDAO;
import entities.AppDatabase;
import entities.Portfolio;
import utils.CustomPortfolioCardAdapter;
import utils.DrawView;
import utils.PortfolioCardAdapterItem;

public class PortfoliosActivity extends AppCompatActivity {

    Button editButton;
    GridView portfoliosGrid;
    ArrayList<PortfolioCardAdapterItem> portfolios;
    CustomPortfolioCardAdapter adapter;

    AppDatabase db;
    AdminDAO dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_portfolios);
        initData();

        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "database-name").fallbackToDestructiveMigration().allowMainThreadQueries().build();
        // NOTE: MIGHT LOCK UP THREAD. SWITCH TO STATIC NESTED CLASS WHEN POSSIBLE
        dao = db.userDao();

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

        portfolios.add(new PortfolioCardAdapterItem(" ", "", "New Portfolio",
                points));
        Portfolio[] portfolioObjects = dao.loadAllPortfolios();
        for(int i=0; i<portfolioObjects.length; i++) {
            portfolios.add(new PortfolioCardAdapterItem("-2.6%", "46000", portfolioObjects[i].portfolio_name, points));
        }
    }
}
