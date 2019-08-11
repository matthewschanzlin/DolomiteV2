package utils;

import android.graphics.PointF;

import java.util.ArrayList;

/**
 * Implemented by PortfolioDetailActivity; handles async task completion.
 */
public interface AsyncTaskComplete {
    void OnComplete(ArrayList<PointF> result);
}
