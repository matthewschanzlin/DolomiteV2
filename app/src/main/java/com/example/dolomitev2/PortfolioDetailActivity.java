package com.example.dolomitev2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.room.Room;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.graphics.PointF;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import entities.AdminDAO;
import entities.AppDatabase;
import entities.Portfolio;
import entities.Stock;
import utils.ApiManager;
import utils.AsyncTaskComplete;
import utils.CustomStockAdapter;
import utils.DataPointsManager;
import utils.GraphView;
import utils.StockAdapterItem;
import utils.ViewDimension;

public class PortfolioDetailActivity extends AppCompatActivity implements AsyncTaskComplete {


    Button singlePortfolioEditButton, searchButton;
    Button[] timeFrameButtons;
    Button selectedTimeFrameButton;
    String selectedTimeFrame;
    String portfolioName;

    //View Components
    TextView singlePortfolioTitle;
    ListView stockList;
    EditText titleEdit;

    //Adapter
    ArrayList<StockAdapterItem> stocks;
    CustomStockAdapter customStockAdapter;

    //Containers
    RelativeLayout relativeLayout;
    RelativeLayout graphViewContainer;
    FrameLayout SearchContainer;
    LinearLayout LinearSearchContainer;

    //Database
    AppDatabase db;
    AdminDAO dao;

    int portfolioId;
    boolean inSearch;

    ArrayList<PointF> points;
    ArrayList<PointF> newPoints;
    DataPointsManager pointsManager;
    int counter;
    ApiManager manager;
    ViewDimension dim;
    Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_portfolio_detail);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        // Init database
        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "database-name").fallbackToDestructiveMigration().allowMainThreadQueries().build();
        // NOTE: MIGHT LOCK UP THREAD. SWITCH TO STATIC NESTED CLASS WHEN POSSIBLE
        dao = db.userDao();

        //Initializing View
        initData();
        populatePortfolio();

        //Listeners
        onAddStockClicked();
        nameChangeListener();
        portfolioEditListener();
        myStocksButtonListener();

        //Populate Stocks
        populateStocks();
        drawGraph("1d");

        //Set Custom Adapter
        setAdapter();

        //Keep the view stable when soft keyboard is launched
        stableKeyBoardViews();

        customStockAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();

                drawGraph(selectedTimeFrame);
            }
        });
    }

    /**
     * This method declares all of our data. Created to minimize confusion / clean up our onCreate.
     */
    private void initData() {
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        context = getApplicationContext();
        points = new ArrayList<>();
        newPoints = new ArrayList<>();
        counter = 0;
        manager = new ApiManager(context, this);
        pointsManager = new DataPointsManager();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        final Float h = (float)((float)displayMetrics.heightPixels*0.25);
        final Float w = (float)displayMetrics.widthPixels;
        dim = new ViewDimension(w,h);

        timeFrameButtons = new Button[6];
        timeFrameButtons[0] = findViewById(R.id.button1d);
        //timeFrameButtons[1] = findViewById(R.id.button5d);
        timeFrameButtons[1] = findViewById(R.id.button1m);
        timeFrameButtons[2] = findViewById(R.id.button3m);
        timeFrameButtons[3] = findViewById(R.id.button6m);
        timeFrameButtons[4] = findViewById(R.id.button1y);
        timeFrameButtons[5] = findViewById(R.id.button5y);
        //timeFrameButtons[7] = findViewById(R.id.buttonAll);
        selectedTimeFrameButton = timeFrameButtons[0];
        singlePortfolioEditButton = findViewById(R.id.singlePortfolioEditButton);
        singlePortfolioTitle = findViewById(R.id.singlePortfolioTitle);
        SearchContainer = findViewById(R.id.SearchContainer);
        LinearSearchContainer = findViewById(R.id.LinearSearchContainer);
        searchButton = findViewById(R.id.singlePortfolioSearchButton);
        titleEdit = findViewById(R.id.singlePortfolioTitleEdit);
        relativeLayout = findViewById(R.id.listViewHolder);
        stockList = findViewById(R.id.singlePortfolioListView);
        graphViewContainer = findViewById(R.id.LineGraph);
        stocks = new ArrayList<>();

        for (int i = 0; i<timeFrameButtons.length; i++) {
            timeFrameButtons[i].setTag(timeFrameButtons[i].getId(),Integer.toString(i));
            timeFrameButtons[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (view.equals(selectedTimeFrameButton)) {
                        return;
                    }
                    switch(view.getTag(view.getId()).toString()) {
                        case "0":
                            drawGraph("1d");
                            break;
                        case "1":
                            drawGraph("1m");
                            break;
                            //drawGraph("5d");
                        case "2":
                            drawGraph("3m");
                            break;
                        case "3":
                            drawGraph("6m");
                            break;
                        case "4":
                            drawGraph("1y");
                            break;
                        case "5":
                        //case "6":
                            drawGraph("5y");
                        //case "7":
                            //drawGraph("All");
                    }
                    view.setBackgroundResource(R.drawable.timeframe_button_selected);
                    ((Button)view).setTextColor(getResources().getColor(R.color.lightLilac, getTheme()));
                    selectedTimeFrameButton.setBackgroundColor(getResources().getColor(R.color.lightLilac, getTheme()));
                    selectedTimeFrameButton.setTextColor(getResources().getColor(R.color.black, getTheme()));
                    selectedTimeFrameButton = (Button)view;
                }
            });
        }

    }

    /**
     * Switches styles of top buttons to indicate viewing MY STOCKS // ADD STOCKS
     * when inSearchMode is changed
     * NOTE: should be called AFTER inSearch
     */
    void switchSearchUI() {
        if (inSearch) {
            // deselect My Stocks button (Edit button), select Search button
            singlePortfolioEditButton.setBackgroundResource(R.drawable.edit_button_unfocused);
            singlePortfolioEditButton.setTextColor(getResources().getColor(R.color.midnightBlue, getTheme()));
            searchButton.setBackgroundResource(R.drawable.edit_button_focused);
            searchButton.setTextColor(getResources().getColor(R.color.lightLilac, getTheme()));
        }
        else {
            // select My Stocks button (Edit button), deselect Search button
            singlePortfolioEditButton.setBackgroundResource(R.drawable.edit_button_focused);
            singlePortfolioEditButton.setTextColor(getResources().getColor(R.color.lightLilac, getTheme()));
            searchButton.setBackgroundResource(R.drawable.edit_button_unfocused);
            searchButton.setTextColor(getResources().getColor(R.color.midnightBlue, getTheme()));
        }
    }

    /**
     * This method populates our PortfolioDetail with the stocks for a given portfolio.
     */
    private void populateStocks() {
        stocks.add(new StockAdapterItem("AAPL", "Apple", (float)208.42, (float)2.4, -1));
        Stock[] loadedStocks = dao.loadStockByPortfolioId(portfolioId);
        for (int i = 0; i<loadedStocks.length; i++) {
            if (loadedStocks[i].sold_datetime == null) {
                stocks.add(new StockAdapterItem(loadedStocks[i].ticker, "TEST",(float)15,(float)1, loadedStocks[i].stock_id));
            }
        }
        //stocks.add(new StockAdapterItem("AMZN", "Amazon", "$1232.32", "-0.4%"));
        //stocks.add(new StockAdapterItem("GOOG", "Google", "$485.25", "-8.5%"));
        //stocks.add(new StockAdapterItem("SNAP", "Snapchat", "$28.53", "+5.4%"));
        //stocks.add(new StockAdapterItem("WMT", "Walmart", "$99.01", "-0.8%"));
    }

    /**
     * This method overrides OnBackPressed so that if in the Search Fragment, the backPress waits for
     * our animation to complete, and then sets the Fragment Container visibilty to gone so that
     * the stock list is visible again. When not in the Search View, the back button takes you back
     * to the launch page (portfolios screen).
     */
    @Override
    public void onBackPressed() {
        if (inSearch) {

            Fragment searchFragment = getSupportFragmentManager().findFragmentById(R.id.SearchContainer);
            inSearch = false;
            switchSearchUI();


            if (searchFragment != null) {
                Animation animation = AnimationUtils.loadAnimation(searchFragment.getActivity(), R.anim.slide_down_stock);
                animation.setDuration(getResources().getInteger(android.R.integer.config_longAnimTime));

                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.remove(getSupportFragmentManager().findFragmentById(R.id.SearchContainer)).commit();
                        LinearSearchContainer.setVisibility(View.GONE);
                        relativeLayout.setVisibility(View.VISIBLE);

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });

                searchFragment.getView().startAnimation(animation);
            }

        } else {
            Intent intent = new Intent(PortfolioDetailActivity.this, PortfoliosActivity.class);
            startActivity(intent);
        }
    }

    /**
     * This method sets the Portfolio Name to an edit text with the text of the portfolio name,
     * and brings the keyboard up.
     */
    void nameChangeListener() {
        singlePortfolioTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                singlePortfolioTitle.setVisibility(View.GONE);
                titleEdit.setVisibility(View.VISIBLE);
                titleEdit.setText(singlePortfolioTitle.getText());
                singlePortfolioTitle.setVisibility(View.GONE);

                titleEdit.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(titleEdit, InputMethodManager.SHOW_IMPLICIT);

            }
        });
    }

    /**
     * This method deals with the edit text. So when the user changes the name in the edit text
     * and completes his entry (presses enter) the text from the edit text is grabbed, fed to the
     * old portfolio Name, and that Name is updated in the database as well.
     * The edittext view is set to GONE, and the textView is visible again.
     */
    void portfolioEditListener() {

        titleEdit.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) &&
                        (i == KeyEvent.KEYCODE_ENTER)) {
                    String newPortfolioName = titleEdit.getText().toString();
                    singlePortfolioTitle.setText(newPortfolioName);
                    titleEdit.setVisibility(View.GONE);
                    singlePortfolioTitle.setVisibility(View.VISIBLE);
                    singlePortfolioTitle.setVisibility(View.VISIBLE);
                    dao.renamePortfolio(portfolioId, newPortfolioName);
                    portfolioName = newPortfolioName;


                    return true;
                }
                return false;
            }
        });

    }

    /**
     * Switches out of search mode if possible.
     */
    void myStocksButtonListener() {
        singlePortfolioEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (inSearch) {
                    onBackPressed();
                }
            }
        });
    }

    /**
     * This populates the PortfolioDetailView with the correct information.
     */
    private void populatePortfolio() {
        portfolioName = getIntent().getStringExtra("Portfolio name");
        portfolioId = getIntent().getIntExtra("Portfolio id", -1);
        singlePortfolioTitle.setText(portfolioName);

    }

    /**
     * The stocklist view disappears and the search fragment view appears with our custom animation.
     * boolean logic was added so that you cannot re-enter the search fragment once you're in searchmode.
     */
    private void onAddStockClicked() {
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                if (inSearch) {

                } else {
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.setCustomAnimations(R.anim.slide_in_up, R.anim.slide_out_up,
                            R.anim.slide_in_up, R.anim.slide_out_up);
                    // for add stock


                    relativeLayout.setVisibility(View.GONE);
                    LinearSearchContainer.setVisibility(View.VISIBLE);

                    fragmentTransaction.replace(R.id.SearchContainer, new SearchFragment());

                    fragmentTransaction.commit();
                    inSearch = true;
                    switchSearchUI();

                }




            }
        });


    }

    /**
     * set the adapter for the stock list
     */
    private void setAdapter() {
        customStockAdapter = new CustomStockAdapter(this, stocks, portfolioId);
        stockList.setAdapter(customStockAdapter);
    }

    /**
     * hide the keyBoard a user presses enter on the edit text.
     */
    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     * this prevents the view from shifting up when the soft keyboard is launched.
     */
    private void stableKeyBoardViews() {
        titleEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

    }

    /**
     * Started by the API manager. Combines all data from that process into an ArrayList
     * and draws it.
     * @param result
     */
    @Override
    public void OnComplete(ArrayList<PointF> result) {
        newPoints = pointsManager.combinePoints(result, points);
        points.removeAll(points);
        for (PointF point : newPoints) {
            points.add(point);
        }
        counter++;
        if (counter == stocks.size()) {
            Log.d("debugging",""+points.size());
            GraphView graph = new GraphView(context, points, dim);
            graphViewContainer.addView(graph);
        }
    }

    /**
     * Starts process for drawing a graph
     * @param timeframe, 1d, 1mm, etc.
     */
    private void drawGraph(String timeframe) {
        selectedTimeFrame = timeframe;
        graphViewContainer.removeAllViews();
        points.removeAll(points);
        counter = 0;
        manager.getHistoricalPortfolioData(timeframe, stocks);
    }
}
