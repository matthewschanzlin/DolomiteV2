package utils;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Adapter item assoc. with CustomStockAdapter
 */
public class StockAdapterItem extends AppCompatActivity {

    String ticker;
    String companyName;
    String value;
    String upDown;
    int stockId;

    public StockAdapterItem(String ticker, String companyName, String value, String upDown, int stockId) {
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

    public String getValue () { return value; }

    public String getUpDown () { return upDown; }

    public int getStockId() {
        return stockId;
    }
}
