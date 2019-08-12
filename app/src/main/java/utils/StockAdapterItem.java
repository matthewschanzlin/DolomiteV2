package utils;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

/**
 * Adapter item assoc. with CustomStockAdapter
 */
public class StockAdapterItem extends AppCompatActivity {

    String ticker;
    String companyName;
    float value;
    float upDown;
    int stockId;

    public StockAdapterItem(String ticker, String companyName, float value, float upDown, int stockId) {
        this.ticker = ticker;
        this.companyName = companyName;
        this.value = value;
        this.upDown = upDown;
        this.stockId = stockId;
    }

    public String getTicker() {
        return ticker;
    }

    public String getCompanyName() {
        return companyName;
    }

    public float getValue () { return value; }

    public String getValueText() {
        return "$" + String.format(Locale.US, "%.2f",value);
    }

    public float getUpDown () { return upDown; }

    public String getUpDownText() {
        if (upDown > 0) {
            return "+" + String.format(Locale.US, "%.2f",upDown);
        }
        return "-" + String.format(Locale.US, "%.2f",upDown);
    }

    public int getStockId() {
        return stockId;
    }

    public void setStockId(int stockId) {
        this.stockId = stockId;
    }
}
