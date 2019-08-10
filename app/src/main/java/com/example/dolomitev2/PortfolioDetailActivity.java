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
import android.os.Bundle;
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
import utils.CustomStockAdapter;
import utils.StockAdapterItem;

public class PortfolioDetailActivity extends AppCompatActivity {

    Button button1d, button5d, button1m, button3m, button6m, button1y, button5y, buttonAll,
            singlePortfolioEditButton, searchButton, editButtonPencil;
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
    FrameLayout SearchContainer;
    LinearLayout LinearSearchContainer;

    //Database
    AppDatabase db;
    AdminDAO dao;

    int portfolioId;
    boolean inSearch;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_portfolio_detail);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        //Initializing View
        initData();
        populatePortfolio();

        //Listeners
        onAddStockClicked();
        nameChangeListener();
        portfolioEditListener();

        //Populate Stocks
        populateStocks();

        //Set Custom Adapter
        setAdapter();

        //Keep the view stable when soft keyboard is launched
        stableKeyBoardViews();
    }

    /**
     * This method declares all of our data. Created to minimize confusion / clean up our onCreate.
     */
    private void initData() {
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
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
        editButtonPencil = findViewById(R.id.editButtonPencil);
        titleEdit = findViewById(R.id.singlePortfolioTitleEdit);
        relativeLayout = findViewById(R.id.listViewHolder);
        stockList = findViewById(R.id.singlePortfolioListView);
        stocks = new ArrayList<>();


        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "database-name").fallbackToDestructiveMigration().allowMainThreadQueries().build();
        // NOTE: MIGHT LOCK UP THREAD. SWITCH TO STATIC NESTED CLASS WHEN POSSIBLE
        dao = db.userDao();


    }


    /**
     * This method populates our PortfolioDetail with the stocks for a given portfolio.
     */
    private void populateStocks() {
        stocks.add(new StockAdapterItem("AAPL", "Apple", "$208.42", "+2.4%"));
        stocks.add(new StockAdapterItem("AMZN", "Amazon", "$1232.32", "-0.4%"));
        stocks.add(new StockAdapterItem("GOOG", "Google", "$485.25", "-8.5%"));
        stocks.add(new StockAdapterItem("SNAP", "Snapchat", "$28.53", "+5.4%"));
        stocks.add(new StockAdapterItem("WALL", "Walmart", "$99.01", "-0.8%"));
        stocks.add(new StockAdapterItem("REBK", "Reebok", "$42.69", "+1.5%"));
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
        editButtonPencil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                singlePortfolioTitle.setVisibility(View.GONE);
                titleEdit.setVisibility(View.VISIBLE);
                titleEdit.setText(singlePortfolioTitle.getText());
                editButtonPencil.setVisibility(View.GONE);

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
                    editButtonPencil.setVisibility(View.VISIBLE);
                    dao.renamePortfolio(portfolioId, newPortfolioName);
                    portfolioName = newPortfolioName;


                    return true;
                }
                return false;
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
     * The stocklist view dissapears and the search fragment view appears with our custom animation.
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

                }




            }
        });


    }

    /**
     * set the adapter for the stock list
     */
    private void setAdapter() {
        customStockAdapter = new CustomStockAdapter(this, stocks);
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
}
