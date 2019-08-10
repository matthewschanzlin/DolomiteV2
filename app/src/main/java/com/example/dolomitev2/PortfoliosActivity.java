package com.example.dolomitev2;

import androidx.appcompat.app.AppCompatActivity;

import androidx.room.Room;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import android.content.Intent;
import android.graphics.Point;
import android.graphics.PointF;
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

        //Database initialization
        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "database-name").fallbackToDestructiveMigration().allowMainThreadQueries().build();
        // NOTE: MIGHT LOCK UP THREAD. SWITCH TO STATIC NESTED CLASS WHEN POSSIBLE
        dao = db.userDao();

        //Data initialization
        initData();

        //Listeners
        portfolioGridListener();
        editModeListener();


        inEditMode = false;

    }

    /**
     * Switch Button to Delete when in Edit Mode, and call helpers
     */
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

    /**
     * Initialize data.
     */
    void initData() {
        editButton = findViewById(R.id.portfoliosEditButton);
        portfoliosGrid = findViewById(R.id.portfoliosGridView);

        portfolios = new ArrayList<>();
        populatePortfolios();
        adapter = new CustomPortfolioCardAdapter(this, portfolios);
        portfoliosGrid.setAdapter(adapter);




    }

    ArrayList<PointF> getPoints() {
        ArrayList<PointF> points = new ArrayList<>();

        points.add(new PointF(10, 350));
        points.add(new PointF(40, 310));
        points.add(new PointF(190, 400));

        return points;
    }

    /**
     * Populate portfolios from the database.
     */
    void populatePortfolios() {

        ArrayList<PointF> points = getPoints();

        portfolios.add(new PortfolioCardAdapterItem(" ", "", "Create Portfolio", points, -1));

        Portfolio[] portfolioObjects = dao.loadAllPortfolios();
        for(int i=0; i<portfolioObjects.length; i++) {
            portfolios.add(new PortfolioCardAdapterItem("-2.6%", "46000", portfolioObjects[i].portfolio_name, points, portfolioObjects[i].portfolio_id));
        }
    }

    /**
     * Grab information about portfolio to send to the activity when clicked.
     * If in edit mode, highlight all portfolios, and then highlight in red when clicked,
     * add to an array for when its meant to be deleted.
     */
    void portfolioGridListener() {
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
                        String untitledPortfolioName = "Untitled";
                        dao.insertPortfolio(new Portfolio(untitledPortfolioName));
                        // get auto-gen id of portfolio just inserted (for cardAdapterItem constructor)
                        Portfolio[] tempAllPortfolios = dao.loadAllPortfolios();
                        PortfolioCardAdapterItem untitledCard =
                                new PortfolioCardAdapterItem("-2.6%", "4600", untitledPortfolioName,
                                        getPoints(), tempAllPortfolios[tempAllPortfolios.length-1].portfolio_id);
                        portfolios.add(untitledCard);
                        adapter.notifyDataSetChanged();
                        Intent intent = new Intent(PortfoliosActivity.this, PortfolioDetailActivity.class);
                        intent.putExtra("Portfolio name", untitledPortfolioName);
                        intent.putExtra("Portfolio id", untitledCard.getId());
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
                intent.putExtra("Portfolio id", portfolios.get(i).getId());
                startActivity(intent);
            }
        });
    }

    /**
     * Deal with removing portfolio database logic.
     */
    void editModeListener() {
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
}
