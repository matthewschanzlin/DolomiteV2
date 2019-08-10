package utils;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.dolomitev2.R;

public class stock_search_adapter_item extends AppCompatActivity {

    public String getCompanyName() {
        return companyName;
    }

    public String getValue() {
        return value;
    }

    public String getUpDown() {
        return upDown;
    }

    public stock_search_adapter_item(String ticker, String companyName, String value, String upDown) {
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

    public void setValue(String value) {
        this.value = value;
    }

    public void setUpDown(String upDown) {
        this.upDown = upDown;
    }

    String ticker;
    String companyName;
    String value;
    String upDown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_search_adapter_item);
    }
}
