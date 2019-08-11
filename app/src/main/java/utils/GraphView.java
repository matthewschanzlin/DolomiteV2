package utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Logic for drawing a line from ArrayList of datapoints.
 */
public class GraphView extends View {

    Paint paint;
    Path path;
    ArrayList<PointF> linePoints;
    ViewDimension dimension;
    Context context;

    /**
     * Includes logic to format datapoints for the given viewframe dimensions.
     * @param context
     * @param points
     * @param dimension
     */
    public GraphView(Context context, ArrayList<PointF> points, ViewDimension dimension) {
        super(context);
        this.context = context;
        this.dimension = dimension;
        this.linePoints = new ArrayList<>();

        if (points.size() > 0) {
            float topY = points.get(0).y;
            float botY = points.get(0).y;

            for (PointF p: points) {
                if (topY < p.y) {
                    topY = p.y;
                } else if (botY > p.y) {
                    botY = p.y;
                }
            }

            double multiplierX = dimension.width / points.size();
            double multiplierY = dimension.height / (topY  - botY);

            for (PointF p: points) {
                linePoints.add(new PointF((float)(p.x * multiplierX), (float)((topY - p.y) * multiplierY)));
            }
        }

        init();
    }

    public GraphView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GraphView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init(){
        paint = new Paint();

        paint.setStyle(Paint.Style.STROKE);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas);
        path = new Path();
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(2);
        for (int i = 0; i < linePoints.size(); i++) {
            path.lineTo(linePoints.get(i).x, linePoints.get(i).y);
        }
        canvas.drawPath(path, paint);
    }
}
