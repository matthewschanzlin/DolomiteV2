package utils;

import android.graphics.PointF;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Implemented by PortfolioDetailActivity; handles async task completion.
 */
public interface AsyncTaskComplete {
    void OnPointsComplete(ArrayList<PointF> result);
    void OnSearchComplete(ArrayList<String> result);
    void OnPriceComplete(ArrayList<String> result);
}
