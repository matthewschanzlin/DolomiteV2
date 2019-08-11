package utils;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.dolomitev2.R;

import java.util.Locale;

/**
 * Adapter item associated w/ CustomStockSearchAdapter
 */
public class stock_search_adapter_item extends AppCompatActivity {

    public stock_search_adapter_item() {
        // required empty constructor
    }

    public String getCompanyName() {
        return companyName;
    }

    public float getValue() {
        return value;
    }

    public float getUpDown() {
        return upDown;
    }

    public String getValueText() {
        return "$" + String.format(Locale.US, "%.2f",value);
    }

    public String getUpDownText() {
        if (upDown > 0) {
            return "+" + String.format(Locale.US, "%.2f",upDown);
        }
        return "-" + String.format(Locale.US, "%.2f",upDown);
    }

    public stock_search_adapter_item(String ticker, String companyName, float value, float upDown) {
        this.ticker = ticker;
        this.companyName = companyName;
        this.value = value;
        this.upDown = upDown;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public void setUpDown(float upDown) {
        this.upDown = upDown;
    }

    String ticker;
    String companyName;
    float value;
    float upDown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_search_adapter_item);
    }
}
