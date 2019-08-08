package entities;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface AdminDAO {

    @Insert
    public void insertStock(Stock stock);

    @Insert
    public void insertPortfolio(Portfolio portfolio);

    @Query("select * from portfolio where Portfolio.portfolio_name = :portfolio_name")
    public Portfolio[] loadPortfolioByPortfolioName(String portfolio_name);

    @Query("select * from portfolio where Portfolio.portfolio_id = :portfolio_id")
    public Portfolio loadPortfolioByPortfolioId(int portfolio_id);

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

    @Query(" update Portfolio set portfolio_name = :name where portfolio_id = :portfolio_id")
    public int renamePortfolio(int portfolio_id, String name);
}
