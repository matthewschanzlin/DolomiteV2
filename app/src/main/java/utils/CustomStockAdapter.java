package utils;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.dolomitev2.R;

import java.util.ArrayList;

public class CustomStockAdapter extends BaseAdapter {

    public CustomStockAdapter(Context context, ArrayList<StockAdapterItem> stocks) {
        this.context = context;
        this.stocks = stocks;
    }

    Context context;
    ArrayList<StockAdapterItem> stocks;

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
        ViewHolder viewHolder;

        if (view == null) {

            view = View.inflate(context, R.layout.stock_adapter_item, null);

            viewHolder = new ViewHolder();
            viewHolder.upDown = view.findViewById(R.id.StockAdapterMetaUpDown);
            viewHolder.value = view.findViewById(R.id.StockAdapterMetaValue);
            viewHolder.ticker = view.findViewById(R.id.ticker_adapter_item);
            viewHolder.companyName = view.findViewById(R.id.company_name_adapter_item);

            view.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder)view.getTag();
        }

        viewHolder.ticker.setText(stocks.get(i).getTicker());
        viewHolder.companyName.setText(stocks.get(i).getCompanyName());
        viewHolder.value.setText(stocks.get(i).getValue());
        viewHolder.upDown.setText(stocks.get(i).getUpDown());

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
    }
}
