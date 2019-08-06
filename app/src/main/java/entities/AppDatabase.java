package Entities;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {Portfolio.class, Stock.class, StockData.class}, version = 2)
@TypeConverters({TiviTypeConverters.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract AdminDAO userDao();
}
