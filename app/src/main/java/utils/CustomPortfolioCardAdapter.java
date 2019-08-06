package utils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.dolomitev2.R;

import java.util.ArrayList;

public class CustomPortfolioCardAdapter extends BaseAdapter {

    Context context;
    ArrayList<PortfolioCardAdapterItem> portfolios;

    public CustomPortfolioCardAdapter(Context context, ArrayList<PortfolioCardAdapterItem> portfolios) {
        this.context = context;
        this.portfolios = portfolios;
    }


    @Override
    public int getCount() {
        return portfolios.size();
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

            view = View.inflate(context, R.layout.activity_portfolio_card_adapter_item, null);

            viewHolder = new ViewHolder();
            viewHolder.percentChange = view.findViewById(R.id.portfolioChangePercentage);
            viewHolder.value = view.findViewById(R.id.portfolioCardValue);
            viewHolder.name = view.findViewById(R.id.portfolioCardTitle);
            viewHolder.chart = view.findViewById(R.id.portfolioCardGraphContainer);

            view.setTag(viewHolder);
        }
        else {
            viewHolder = (CustomPortfolioCardAdapter.ViewHolder)view.getTag();
        }

        viewHolder.name.setText(portfolios.get(i).getName());
        viewHolder.percentChange.setText(portfolios.get(i).getPercentChange());
        viewHolder.value.setText(portfolios.get(i).getValue());
        DrawView chartView = new DrawView(context, portfolios.get(i).getChart());
        viewHolder.chart.addView(chartView);

        return view;
    }

    static class ViewHolder {
        TextView percentChange;
        TextView value;
        TextView name;
        RelativeLayout chart;
    }
}
