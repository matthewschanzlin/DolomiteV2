package utils;

import android.content.Context;
import android.graphics.Point;
import android.graphics.PointF;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Pulls data from the API (IEX)
 */
public class ApiManager {
    Context context;
    ArrayList<PointF> dataPoints;
    AsyncTaskComplete listener;

    public ApiManager(Context context, AsyncTaskComplete listener) {
        this.context = context;
        this.listener = listener;
        dataPoints = new ArrayList<>();
    }

    /**
     * Fetches the list of historical datapoints for the given ticker over the given timeframe
     * @param timeframe
     * @param ticker
     */
    public void getHistoricalStockData(final String timeframe, final String ticker) {

        final String url = "https://cloud.iexapis.com/stable/stock/" + ticker + "/chart/" + timeframe + "?token=pk_ef064cdb978c441586aec2daa723c376";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try{
                            dataPoints.removeAll(dataPoints);
                            for(int i=0; i < response.length(); i++){
                                JSONObject stockPrice = response.getJSONObject(i);
                                float price;
                                if (timeframe.equals("1d")) {
                                    price = (float)stockPrice.getDouble("marketClose");
                                }else {
                                    price = (float)stockPrice.getDouble("uClose");
                                }
                                dataPoints.add(new PointF(i, price));
                            }
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                        String s = new Integer(dataPoints.size()).toString();
                        listener.OnComplete(dataPoints);
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        //Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d("debugging", "apimanager");
                    }
                }
        );
        // add it to the RequestQueue
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(jsonArrayRequest);

    }

    /**
     * Loops through all the stocks
     * @param timeframe
     * @param stocks
     */
    public void getHistoricalPortfolioData(String timeframe, ArrayList<StockAdapterItem> stocks) {
        for (StockAdapterItem s: stocks) {
            getHistoricalStockData(timeframe, s.getTicker());
        }

    }
}
