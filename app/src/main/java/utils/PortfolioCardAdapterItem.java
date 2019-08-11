package utils;

import android.graphics.Point;
import android.graphics.PointF;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dolomitev2.R;

import java.util.ArrayList;

/**
 * AdapterItem for CustomPortfolioCardAdapter
 */
public class PortfolioCardAdapterItem extends AppCompatActivity {

    String percentChange;
    String value;
    String name;
    ArrayList<PointF> chart;
    boolean deleteMode;
    int id; // FROM DATABASE


    public PortfolioCardAdapterItem(String percentChange, String value, String name, ArrayList<PointF> chart, int id) {
        this.percentChange = percentChange;
        this.value = value;
        this.name = name;
        this.chart = chart;
        this.deleteMode = false;
        this.id = id;
    }

    // NOT operation for deletemode
    public boolean switchDeleteMode() {
        deleteMode = ! deleteMode;
        return getDeleteMode();
    }

    public boolean getDeleteMode() {
        return deleteMode;
    }

    public int getId() {
        return id;
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

    public ArrayList<PointF> getChart() {
        return chart;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_portfolio_card_adapter_item);

    }


}
