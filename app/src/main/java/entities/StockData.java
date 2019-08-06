package Entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class StockData {

    @PrimaryKey
    @NonNull
    public String ticker;
}
