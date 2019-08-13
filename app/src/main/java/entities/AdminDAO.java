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

    @Query("select * from  Portfolio")
    public Portfolio[] loadAllPortfolios();

    @Query("select * from Stock inner join portfolio on stock.portfolio_id = :portfolio_id")
    public Stock[] loadStockByPortfolioId(int portfolio_id);

    @Query("select * from Stock where stock_id = :stock_id")
    public Stock loadStockByStockId(int stock_id);

    @Query("update Stock set stock_price = :stockPrice where stock_id = :stockId")
    public void setStockPriceByStockId(int stockId, float stockPrice);

    @Delete
    public void deletePortfolio(Portfolio portfolio);

    @Delete
    public void deleteStock(Stock stock);

    @Query(" update Portfolio set portfolio_name = :name where portfolio_id = :portfolio_id")
    public void renamePortfolio(int portfolio_id, String name);
}
