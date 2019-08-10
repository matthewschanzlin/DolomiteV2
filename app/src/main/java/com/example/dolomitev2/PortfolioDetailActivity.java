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
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
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
            singlePortfolioEditButton, searchButton;
    Button[] timeFrameButtons;
    Button selectedTimeFrameButton;
    String portfolioName;
    int portfolioId;
    TextView singlePortfolioTitle;
    ListView stockList;
    ArrayList<StockAdapterItem> stocks;
    CustomStockAdapter customStockAdapter;
    FrameLayout SearchContainer;
    LinearLayout LinearSearchContainer;
    boolean inSearch;
    Button editButtonPencil;
    EditText titleEdit;

    AppDatabase db;
    AdminDAO dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_portfolio_detail);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        timeFrameButtons = new Button[8];
        timeFrameButtons[0] = findViewById(R.id.button1d);
        timeFrameButtons[1] = findViewById(R.id.button5d);
        timeFrameButtons[2] = findViewById(R.id.button1m);
        timeFrameButtons[3] = findViewById(R.id.button3m);
        timeFrameButtons[4] = findViewById(R.id.button6m);
        timeFrameButtons[5] = findViewById(R.id.button1y);
        timeFrameButtons[6] = findViewById(R.id.button5y);
        timeFrameButtons[7] = findViewById(R.id.buttonAll);
        selectedTimeFrameButton = timeFrameButtons[0];
        singlePortfolioEditButton = findViewById(R.id.singlePortfolioEditButton);
        singlePortfolioTitle = findViewById(R.id.singlePortfolioTitle);
        SearchContainer = findViewById(R.id.SearchContainer);
        LinearSearchContainer = findViewById(R.id.LinearSearchContainer);
        searchButton = findViewById(R.id.singlePortfolioSearchButton);
        editButtonPencil = findViewById(R.id.editButtonPencil);
        titleEdit = findViewById(R.id.singlePortfolioTitleEdit);


        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "database-name").fallbackToDestructiveMigration().allowMainThreadQueries().build();
        // NOTE: MIGHT LOCK UP THREAD. SWITCH TO STATIC NESTED CLASS WHEN POSSIBLE
        dao = db.userDao();

        for (int i = 0; i<timeFrameButtons.length; i++) {
            timeFrameButtons[i].setTag(timeFrameButtons[i].getId(),Integer.toString(i));
            timeFrameButtons[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    switch(view.getTag(view.getId()).toString()) {
                        case "0":
                        case "1":
                        case "2":
                        case "3":
                        case "4":
                        case "5":
                        case "6":
                        case "7":
                    }
                    view.setBackgroundResource(R.drawable.timeframe_button_selected);
                    ((Button)view).setTextColor(getResources().getColor(R.color.lightLilac, getTheme()));
                    selectedTimeFrameButton.setBackgroundColor(getResources().getColor(R.color.lightLilac, getTheme()));
                    selectedTimeFrameButton.setTextColor(getResources().getColor(R.color.midnightBlue, getTheme()));
                    selectedTimeFrameButton = (Button)view;
                }
            });
        }

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                LinearSearchContainer.setVisibility(View.VISIBLE);
                inSearch = true;
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.setCustomAnimations(R.anim.slide_in_up, R.anim.slide_out_up,
                        R.anim.slide_in_up, R.anim.slide_out_up);
                fragmentTransaction.replace(R.id.SearchContainer, new SearchFragment());

                fragmentTransaction.commit();
            }
        });


        portfolioName = getIntent().getStringExtra("Portfolio name");
        portfolioId = getIntent().getIntExtra("Portfolio id", -1);
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
        nameChangeListener();
        portfolioEditListener();
        titleEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });
    }

    void unselectTimeFrameButton(Button timeFrameButton) {
        timeFrameButton.setBackgroundResource(R.color.lightLilac);
    }

    private void populateStocks() {
        stocks.add(new StockAdapterItem("AAPL", "Apple", "$208.42", "+2.4%"));
        stocks.add(new StockAdapterItem("AMZN", "Amazon", "$1232.32", "-0.4%"));
        stocks.add(new StockAdapterItem("GOOG", "Google", "$485.25", "-8.5%"));
        stocks.add(new StockAdapterItem("SNAP", "Snapchat", "$28.53", "+5.4%"));
        stocks.add(new StockAdapterItem("WALL", "Walmart", "$99.01", "-0.8%"));
        stocks.add(new StockAdapterItem("REBK", "Reebok", "$42.69", "+1.5%"));
    }

    @Override
    public void onBackPressed() {
        if (inSearch) {

            Fragment searchFragment = getSupportFragmentManager().findFragmentById(R.id.SearchContainer);
            inSearch = false;


          //  fragmentTransaction.setCustomAnimations(R.animator.slide_up, R.animator.slide_down);


            if (searchFragment != null) {
                Animation animation = AnimationUtils.loadAnimation(searchFragment.getActivity(), R.anim.slide_out_up);
                animation.setDuration(getResources().getInteger(android.R.integer.config_shortAnimTime));

                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.remove(getSupportFragmentManager().findFragmentById(R.id.SearchContainer)).commit();
                        LinearSearchContainer.setVisibility(View.GONE);



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

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
