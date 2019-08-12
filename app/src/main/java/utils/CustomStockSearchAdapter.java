package utils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.room.Room;

import com.daimajia.swipe.SwipeLayout;
import com.example.dolomitev2.PortfolioDetailActivity;
import com.example.dolomitev2.PortfoliosActivity;
import com.example.dolomitev2.R;

import java.util.ArrayList;

import entities.AdminDAO;
import entities.AppDatabase;

/**
 * Adapter for stock items that have been searched for.
 */
public class CustomStockSearchAdapter extends BaseAdapter {
    Context context;
    private  Context mContext;
    ArrayList<stock_search_adapter_item> stocks;
    boolean viewClicked;

    public CustomStockSearchAdapter(Context context, ArrayList<stock_search_adapter_item> stocks) {
        this.context = context;
        this.mContext = context;
        this.stocks = stocks;
        viewClicked = false;
    }

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

            view = View.inflate(context, R.layout.activity_stock_search_adapter_item, null);

            viewHolder = new ViewHolder();
            viewHolder.upDown = view.findViewById(R.id.search_StockAdapterMetaUpDown);
            viewHolder.value = view.findViewById(R.id.search_StockAdapterMetaValue);
            viewHolder.ticker = view.findViewById(R.id.search_ticker_adapter_item);
            viewHolder.companyName = view.findViewById(R.id.search_company_name_adapter_item);
            viewHolder.swipeLayout = view.findViewById(R.id.SearchSwipeLayoutId);
            viewHolder.addStockButton = view.findViewById(R.id.AddStockSearchButton);
            viewHolder.viewPortfolioAffectButton = view.findViewById(R.id.ViewPortfolioButton);
            viewHolder.currentPos = i;

            viewHolder.addStockButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    StockAdapterItem stockToView = new StockAdapterItem(
                            stocks.get(viewHolder.currentPos).getTicker(),
                            stocks.get(viewHolder.currentPos).getCompanyName(),
                            stocks.get(viewHolder.currentPos).getValue(),
                            stocks.get(viewHolder.currentPos).getUpDown(),
                            -1);

                    if (mContext instanceof PortfolioDetailActivity) {
                        ((PortfolioDetailActivity) mContext).addStock(stockToView);
                        ((PortfolioDetailActivity) mContext).addStockToDb(stockToView);
                    }
                    viewHolder.viewPortfolioAffectButton.setText("Stock Added!");
                }
            });

            viewHolder.viewPortfolioAffectButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    StockAdapterItem stockToView = new StockAdapterItem(
                            stocks.get(viewHolder.currentPos).getTicker(),
                            stocks.get(viewHolder.currentPos).getCompanyName(),
                            stocks.get(viewHolder.currentPos).getValue(),
                            stocks.get(viewHolder.currentPos).getUpDown(),
                            -1);

                    if (!viewClicked) {
                        if (mContext instanceof PortfolioDetailActivity) {
                            ((PortfolioDetailActivity) mContext).addStock(stockToView);
                        }
                        viewHolder.viewPortfolioAffectButton.setText("Viewing With");
                        viewClicked = true;
                    } else {
                        if (mContext instanceof PortfolioDetailActivity) {
                            ((PortfolioDetailActivity)mContext).removeStock(stockToView);
                            viewHolder.viewPortfolioAffectButton.setText("Viewing Without");
                        }
                        viewClicked = false;
                    }




                }
            });


            viewHolder.swipeLayout .setShowMode(SwipeLayout.ShowMode.PullOut);
            viewHolder.swipeLayout.addDrag(SwipeLayout.DragEdge.Right,
                    viewHolder.swipeLayout.findViewById(R.id.StockSearchSwipeContainer));

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
        Button addStockButton, viewPortfolioAffectButton;
        int currentPos;

    }
}
