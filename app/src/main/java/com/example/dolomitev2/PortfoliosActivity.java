package com.example.dolomitev2;

import androidx.appcompat.app.AppCompatActivity;

import androidx.room.Room;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;

//import com.daimajia.swipe.SwipeLayout;

import java.util.ArrayList;

import entities.AdminDAO;
import entities.AppDatabase;
import entities.Portfolio;
import utils.CustomPortfolioCardAdapter;
import utils.PortfolioCardAdapterItem;

public class PortfoliosActivity extends AppCompatActivity {

    Button editButton;
    GridView portfoliosGrid;
    ArrayList<PortfolioCardAdapterItem> portfolios;
    CustomPortfolioCardAdapter adapter;
    boolean inEditMode;

    AppDatabase db;
    AdminDAO dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_portfolios);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "database-name").fallbackToDestructiveMigration().allowMainThreadQueries().build();
        // NOTE: MIGHT LOCK UP THREAD. SWITCH TO STATIC NESTED CLASS WHEN POSSIBLE
        dao = db.userDao();

        initData();



        inEditMode = false;


        portfoliosGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("Gridclick", "" + i + ", " + inEditMode);
                if (i == 0) {
                    if (inEditMode) {
                        switchEditMode();
                        return;
                    }
                    else {
                        // add new portfolio
                        dao.insertPortfolio(new Portfolio("Untitled Portfolio"));
                        PortfolioCardAdapterItem untitledCard =
                                new PortfolioCardAdapterItem("-2.6%", "4600", "Untitled Portfolio", getPoints());
                        portfolios.add(untitledCard);
                        adapter.notifyDataSetChanged();
                        Intent intent = new Intent(PortfoliosActivity.this, PortfolioDetailActivity.class);
                        intent.putExtra("Portfolio name", portfolios.get(portfolios.size()-1).getName());
                        startActivity(intent);
                        return;
                    }
                }
                else if (inEditMode) {
                    adapter.getItem(i).switchDeleteMode();
                    adapter.notifyDataSetChanged();
                    return;
                }
                // Start single portfolio activity
                Intent intent = new Intent(PortfoliosActivity.this, PortfolioDetailActivity.class);
                intent.putExtra("Portfolio name", portfolios.get(i).getName());
                startActivity(intent);
            }
        });


        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (inEditMode) {
                    // Delete
                    for (int i = 1; i < portfolios.size(); i++) {
                        if (portfolios.get(i).getDeleteMode()) {
                            dao.deletePortfolio(dao.loadPortfolioByPortfolioName(portfolios.get(i).getName())[0]);
                            portfolios.remove(i);
                            i -= 1;
                        }
                    }
                }
                switchEditMode();
            }
        });
    }

    void switchEditMode() {
        inEditMode = !inEditMode;
        if (inEditMode) {
            editButton.setText("Delete");
        }
        else {
            editButton.setText(R.string.edit_button);
        }
        adapter.switchEditMode();
        adapter.notifyDataSetChanged();
    }

    void initData() {
        editButton = findViewById(R.id.portfoliosEditButton);
        portfoliosGrid = findViewById(R.id.portfoliosGridView);

        portfolios = new ArrayList<>();
        populatePortfolios();
        adapter = new CustomPortfolioCardAdapter(this, portfolios);
        portfoliosGrid.setAdapter(adapter);

    }

    ArrayList<Point> getPoints() {
        ArrayList<Point> points = new ArrayList<>();

        points.add(new Point(10, 350));
        points.add(new Point(40, 310));
        points.add(new Point(190, 400));

        return points;
    }

    void populatePortfolios() {

        ArrayList<Point> points = getPoints();

        portfolios.add(new PortfolioCardAdapterItem(" ", "", "Create Portfolio", points
        ));

        Portfolio[] portfolioObjects = dao.loadAllPortfolios();
        for(int i=0; i<portfolioObjects.length; i++) {
            portfolios.add(new PortfolioCardAdapterItem("-2.6%", "46000", portfolioObjects[i].portfolio_name, points));
        }
    }
}
