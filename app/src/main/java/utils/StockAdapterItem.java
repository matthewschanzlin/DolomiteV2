package utils;

public class StockAdapterItem {

    String ticker;
    String companyName;
    String value;
    String upDown;

    public StockAdapterItem(String ticker, String companyName, String value, String upDown) {
        this.ticker = ticker;
        this.companyName = companyName;
        this.value = value;
        this.upDown = upDown;
    }

    public String getTicker() {
        return ticker;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getValue () { return value; }

    public String getUpDown () { return upDown; }
}
