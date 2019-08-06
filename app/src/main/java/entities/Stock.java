package entities;

import androidx.room.Entity;
import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;

import org.threeten.bp.OffsetDateTime;


@Entity
public class Stock {

    static int stock_id_counter;

    @PrimaryKey(autoGenerate = true)
    public int stock_id;

    public int portfolio_id;

    public OffsetDateTime bought_datetime;

    public Stock(int portfolio_id, OffsetDateTime bought_datetime, String ticker) {
        this.portfolio_id = portfolio_id;
        this.bought_datetime = bought_datetime;
        this.ticker = ticker;
    }

    @Nullable
    public OffsetDateTime sold_datetime;

    public String ticker;

}
