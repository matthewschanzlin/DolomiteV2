package utils;

import android.graphics.PointF;
import android.util.Log;

import java.util.ArrayList;

/**
 * Logic for combining lists of datapoints.
 */
public class DataPointsManager {

    public DataPointsManager() {

    }

    public ArrayList<PointF> combinePoints(ArrayList<PointF> list1, ArrayList<PointF> list2) {
        ArrayList<PointF> combinedList = new ArrayList<>();
        int combinedSize = 0;
        float combinedY = (float)0.00;


        if (list1.size() == 0 && list2.size() == 0) {
            Log.d("error", "bad data");
        } else if (list1.size() == 0 || list2.size() == 0){
            combinedList = list1.size() > list2.size() ? list1 : list2;
        } else {
            if (list1.size() == list2.size()) {
                combinedSize = list1.size();
            } else {
                combinedSize = list1.size() > list2.size() ? list2.size() : list1.size();
            }
            for (int j = 0; j < combinedSize; j++) {
                combinedY = (list1.get(j).y + list2.get(j).y);
                combinedList.add(new PointF((float)j, combinedY));
            }
        }
        return combinedList;
    }
}
