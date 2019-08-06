package Entities;
/*
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface AdminDAO {

    @Insert
    public void insertStock(Stock stock);

    @Insert
    public void insertPortfolio(Portfolio portfolio);

    @Query("select * from portfolio where Portfolio.portfolio_name = :portfolio_name")
    public Portfolio[] loadPortfolioByPortfolioName(String portfolio_name);

    @Query("select * from  Stock")
    public Stock[] loadAllStocks();

    @Query("select * from  Portfolio")
    public Portfolio[] loadAllPortfolios();

    @Query("select * from Stock inner join portfolio on stock.portfolio_id = portfolio.portfolio_id where Portfolio.portfolio_name = :portfolio_name")
    public Stock[] loadStockByPortfolioName(String portfolio_name);

    @Delete
    public void deletePortfolio(Portfolio portfolio);

    @Delete
    public void deleteStock(Stock stock);
}
*/