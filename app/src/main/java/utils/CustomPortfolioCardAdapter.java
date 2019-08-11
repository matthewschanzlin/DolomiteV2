package utils;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.dolomitev2.PortfoliosActivity;
import com.example.dolomitev2.R;

import java.util.ArrayList;

/**
 * Adapter for cards in PortfoliosActivitys
 */
public class CustomPortfolioCardAdapter extends BaseAdapter {

    Context context;
    ArrayList<PortfolioCardAdapterItem> portfolios;
    boolean editmode;

    public CustomPortfolioCardAdapter(PortfoliosActivity context, ArrayList<PortfolioCardAdapterItem> portfolios) {
        this.context = context;
        this.portfolios = portfolios;
        this.editmode = false;
    }


    @Override
    public int getCount() {
        return portfolios.size();
    }

    /**
     * Changes into and out of editmode, and changes editmode for each adapter item
     */
    public void switchEditMode() {
        editmode = ! editmode;
        if (! editmode){
            for (PortfolioCardAdapterItem portfolio : portfolios) {
                portfolio.deleteMode = false;
            }
        }
    }

    @Override
    public PortfolioCardAdapterItem getItem(int i) {
        return portfolios.get(i);
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
            viewHolder.background = view.findViewById(R.id.portfolioCardViewBackground);

            view.setTag(viewHolder);
        }
        else {
            viewHolder = (CustomPortfolioCardAdapter.ViewHolder)view.getTag();
        }

        viewHolder.name.setText(portfolios.get(i).getName());
        viewHolder.percentChange.setText(portfolios.get(i).getPercentChange());
        viewHolder.value.setText(portfolios.get(i).getValue());
        GraphView chartView = new GraphView(context, portfolios.get(i).getChart(), new ViewDimension((float)396.00, (float)171.0));
        viewHolder.chart.addView(chartView);
        if (editmode && i!=0) { // change background to show editmode
            if (portfolios.get(i).getDeleteMode()) {
                viewHolder.background.setBackground(context.getDrawable(R.drawable.twilight_blue_gradient_selected));
            } else {
                viewHolder.background.setBackground(context.getDrawable(R.drawable.twilight_blue_gradient_edit));
            }
        }
        else {
            viewHolder.background.setBackground(context.getDrawable(R.drawable.twilight_blue_gradient));
        }
        // green if the portfolio's gained value, red if it lost value
        if (((viewHolder.percentChange.getText()).toString().substring(0,1)).equals("+")) {
            viewHolder.percentChange.setTextColor(Color.GREEN);
        } else {
            viewHolder.percentChange.setTextColor(Color.RED);
        }
        return view;
    }

    static class ViewHolder {
        TextView percentChange;
        TextView value;
        TextView name;
        RelativeLayout chart;
        LinearLayout background;
    }

}
