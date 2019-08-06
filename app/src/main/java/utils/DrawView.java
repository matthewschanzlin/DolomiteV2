package utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;

public class DrawView extends View {

    Paint paint;
    Path path;
    Point startPoint;
    ArrayList<Point> linePoints;

    public DrawView(Context context, ArrayList<Point> points) {
        super(context);

        startPoint = new Point(0, 300);
        linePoints = points;
        init();
    }

    public DrawView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DrawView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init(){
        paint = new Paint();

        paint.setStyle(Paint.Style.STROKE);

    }

    /*public ArrayList<Point> getPoints(StockAdapterItem stock) {
        String ticker = stock.getTicker();

        /*final String url = "https://cloud.iexapis.com/stable/data-points/" + ticker + "/QUOTE-LATESTPRICE?token=pk_ef064cdb978c441586aec2daa723c376";
        final Context cont = getContext();
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        val = response.toString();
                        stocks.add(0, new StockAdapterItem(ticker, ticker,
                                val, "+2.6%"));
                        adapter.notifyDataSetChanged();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //
                    }
                });

        // add it to the RequestQueue
        RequestQueue queue = Volley.newRequestQueue(cont);
        queue.add(stringRequest);

        ArrayList<Point> list = new ArrayList<>();
        return list;
    }*/

    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas);
        path = new Path();
        paint.setColor(Color.DKGRAY);
        paint.setStrokeWidth(5);
        path.moveTo(0, 300);
        for (int i = 0; i < linePoints.size(); i++) {
            path.lineTo(linePoints.get(i).x, linePoints.get(i).y);
        }
        canvas.drawPath(path, paint);
    }
}
