package entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity

public class Portfolio {

    @PrimaryKey(autoGenerate = true)
    public int portfolio_id;

    public String portfolio_name;

    public Portfolio(String portfolio_name) {
        this.portfolio_name = portfolio_name;
    }
}
