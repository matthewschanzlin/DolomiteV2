package entities;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {entities.Portfolio.class, entities.Stock.class, entities.StockData.class}, version = 3)
@TypeConverters({TiviTypeConverters.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract AdminDAO userDao();
}
