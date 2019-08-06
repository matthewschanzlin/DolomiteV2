package utils;

import android.graphics.Point;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dolomitev2.R;

import java.util.ArrayList;

public class PortfolioCardAdapterItem extends AppCompatActivity {

    String percentChange;
    String value;
    String name;
    ArrayList<Point> chart;


    public PortfolioCardAdapterItem(String percentChange, String value, String name, ArrayList<Point> chart) {
        this.percentChange = percentChange;
        this.value = value;
        this.name = name;
        this.chart = chart;
    }

    public String getPercentChange() {
        return percentChange;
    }

    public String getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Point> getChart() {
        return chart;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_portfolio_card_adapter_item);

    }

}
