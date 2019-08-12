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
    ArrayList<String> searchResults;
    AsyncTaskComplete listener;
    RequestQueue queue;

    public ApiManager(Context context, AsyncTaskComplete listener) {
        this.context = context;
        this.listener = listener;
        dataPoints = new ArrayList<>();
        searchResults = new ArrayList<>();
        queue = Volley.newRequestQueue(context);
    }

    /**
     * Fetches the list of historical datapoints for the given ticker over the given timeframe
     * @param timeframe
     * @param ticker
     */
    public void getHistoricalStockData(final String timeframe, final String ticker) {
        Log.d("yee", "yeeet4");
        dataPoints.removeAll(dataPoints);
        final String url = "https://cloud.iexapis.com/stable/stock/" + ticker + "/chart/" + timeframe + "?token=pk_ef064cdb978c441586aec2daa723c376";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        try{
                            for(int i=0; i < response.length(); i++) {
                                JSONObject stockPrice = response.getJSONObject(i);
                                float price;

                                if (stockPrice.getString("close") != "null") {
                                    price = (float)stockPrice.getDouble("close");
                                    dataPoints.add(new PointF(i, price));
                                    Log.d("POINT", new Integer(i).toString() +" , "+new Float(price).toString());
                                }
                            }
                        }catch (JSONException e){
                            Log.d("error", "error");
                        }
                        String s = new Integer(dataPoints.size()).toString();
                        listener.OnPointsComplete(dataPoints);
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        Log.d("debugging", "apimanager");
                    }
                }
        );
        // add it to the RequestQueue
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

    public void getPrices(ArrayList<String> symbols) {
        for (int i = 0; i < symbols.size(); i++) {
            if (i % 2 == 0) {
                getPrice(symbols.get(i));
            }
        }
    }

    public void getPrice(final String symbol) {
        final String url = "https://cloud.iexapis.com/stable/stock/"+symbol+"/price?token=pk_ef064cdb978c441586aec2daa723c376";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        ArrayList<String> result = new ArrayList<>();
                        result.add(symbol);
                        result.add(response);
                        listener.OnPriceComplete(result);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "ERROR GETTING PRICE", Toast.LENGTH_SHORT).show();
                    }
        });

        queue.add(stringRequest);

    }

    public void getSearchResults(final String searchTerm) {
        searchResults.removeAll(searchResults);
        final String url = "https://cloud.iexapis.com/stable/search/" + searchTerm + "?token=pk_ef064cdb978c441586aec2daa723c376";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try{
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject obj = response.getJSONObject(i);
                                String symbol = obj.getString("symbol");
                                String securityName = obj.getString("securityName");
                                searchResults.add(symbol);
                                searchResults.add(securityName);
                            }
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                        listener.OnSearchComplete(searchResults);
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        Toast.makeText(context, "Search Error, retry", Toast.LENGTH_SHORT).show();
                    }
                }
        );
        // add it to the RequestQueue
        queue.add(jsonArrayRequest);
    }
}
