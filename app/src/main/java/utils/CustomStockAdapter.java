package utils;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.room.Room;

import com.daimajia.swipe.SimpleSwipeListener;
import com.daimajia.swipe.SwipeLayout;
import com.example.dolomitev2.PortfolioDetailActivity;
import com.example.dolomitev2.R;

import java.util.ArrayList;

import entities.AdminDAO;
import entities.AppDatabase;

/**
 * Adapter for stocks associated with a portfolio
 */
public class CustomStockAdapter extends BaseAdapter {

    public CustomStockAdapter(Context context, ArrayList<StockAdapterItem> stocks, int portfolioId) {
        this.context = context;
        this.stocks = stocks;
        this.portfolioId = portfolioId;
    }

    Context context;
    ArrayList<StockAdapterItem> stocks;
    int portfolioId;

    AppDatabase db;
    AdminDAO dao;

    @Override
    public int getCount() {
        return stocks.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final ViewHolder viewHolder;

        if (view == null) {

            view = View.inflate(context, R.layout.stock_adapter_item, null);

            viewHolder = new ViewHolder();
            viewHolder.upDown = view.findViewById(R.id.StockAdapterMetaUpDown);
            viewHolder.value = view.findViewById(R.id.StockAdapterMetaValue);
            viewHolder.ticker = view.findViewById(R.id.ticker_adapter_item);
            viewHolder.companyName = view.findViewById(R.id.company_name_adapter_item);
            viewHolder.swipeLayout = view.findViewById(R.id.SwipeLayoutId);
            viewHolder.deleteButton = view.findViewById(R.id.DELETEBUTTON);
            viewHolder.stockId = stocks.get(i).getStockId();
            viewHolder.currentPos = i;

            viewHolder.deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    stocks.remove(viewHolder.currentPos);
                    // Init database
                    if (db == null) {
                        db = Room.databaseBuilder(context,
                                AppDatabase.class, "database-name").fallbackToDestructiveMigration().allowMainThreadQueries().build();
                        // NOTE: MIGHT LOCK UP THREAD. SWITCH TO STATIC NESTED CLASS WHEN POSSIBLE
                        dao = db.userDao();
                    }
                    if (viewHolder.stockId != -1) {
                        dao.deleteStock(dao.loadStockByStockId(viewHolder.stockId));
                    }
                    notifyDataSetChanged();
                }
            });



            viewHolder.swipeLayout .setShowMode(SwipeLayout.ShowMode.PullOut);
            viewHolder.swipeLayout.addDrag(SwipeLayout.DragEdge.Right,
                    viewHolder.swipeLayout.findViewById(R.id.DELETEBUTTONContainer));


            viewHolder.swipeLayout.addSwipeListener(new SwipeLayout.SwipeListener() {
                @Override
                public void onStartOpen(SwipeLayout layout) {

                }

                @Override
                public void onOpen(SwipeLayout layout) {



                }

                @Override
                public void onStartClose(SwipeLayout layout) {

                }

                @Override
                public void onClose(SwipeLayout layout) {

                }

                @Override
                public void onUpdate(SwipeLayout layout, int leftOffset, int topOffset) {


                }

                @Override
                public void onHandRelease(SwipeLayout layout, float xvel, float yvel) {



                }
            });



            view.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder)view.getTag();
        }

        viewHolder.ticker.setText(stocks.get(i).getTicker());
        viewHolder.companyName.setText(stocks.get(i).getCompanyName());
        viewHolder.value.setText(stocks.get(i).getValueText());
        viewHolder.upDown.setText(stocks.get(i).getUpDownText());

        if (((viewHolder.upDown.getText()).toString().substring(0,1)).equals("+")) {
            viewHolder.upDown.setTextColor(Color.GREEN);
        } else {
            viewHolder.upDown.setTextColor(Color.RED);
        }

        return view;
    }

    static class ViewHolder {
        TextView ticker;
        TextView companyName;
        TextView upDown;
        TextView value;
        SwipeLayout swipeLayout;
        Button deleteButton;
        int currentPos;
        int stockId;

    }
}
