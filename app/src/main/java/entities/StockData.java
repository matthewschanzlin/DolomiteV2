package entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Ticker for a stock.
 */
@Entity
public class StockData {

    @PrimaryKey
    @NonNull
    public String ticker;
}
