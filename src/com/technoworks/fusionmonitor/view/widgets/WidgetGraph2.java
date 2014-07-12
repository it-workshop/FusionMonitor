package com.technoworks.fusionmonitor.view.widgets;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import com.technoworks.fusionmonitor.MonitorActivity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Queue;

/**
 * Created by Roman on 12.07.2014.
 *
 * A base class for all graph-based widgets.
 */
public class WidgetGraph2 extends Widget
{
    public static final String TYPE = "Graph2";

    public ArrayList<Float> values = new ArrayList<Float>();
    private Paint backgroundPaint = new Paint();
    private Paint foregroundPaint = new Paint();
    private RectF graphBounds = new RectF();

    public WidgetGraph2(Context context)
    {
        super(context);

        this.backgroundPaint.setColor(Color.BLACK);
        this.foregroundPaint.setColor(Color.GREEN);

        refreshSizes(DEFAULT_SIZE[0] * mMonitorActivity.mCellWidth,
                     DEFAULT_SIZE[1] * mMonitorActivity.mCellHeight);

        values.add(10f);
        values.add(129f);
        values.add(150f);
        values.add(152f);
        values.add(12f);
        values.add(69f);
        values.add(200f);
        values.add(70f);
    }

    @Override
    protected void onResized()
    {
        if(this.getLayoutParams() != null)
        {
            refreshSizes(this.getLayoutParams().width, this.getLayoutParams().height);
        }
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        canvas.save();
        {
            canvas.drawRect(graphBounds, backgroundPaint);

            canvas.translate(graphBounds.left, graphBounds.top);

            float maxval = 0;
            for(float val : this.values) if(val > maxval) maxval = val;

            float w = (graphBounds.right - graphBounds.left),
                  h = (graphBounds.bottom - graphBounds.top);

            for(int i = 0; i < this.values.size() - 1; i++)
            {
                float sx = i * (w / (this.values.size() - 1)),
                      ex = (i + 1) * (w / (this.values.size() - 1)),
                      sy = h - (this.values.get(i) * (h / maxval)),
                      ey = h - (this.values.get(i + 1) * (h / maxval));
                canvas.drawLine(sx, sy, ex, ey, foregroundPaint);
            }
        }
        canvas.restore();
    }

    protected void refreshSizes(float w, float h)
    {
        this.graphBounds.set(this.getPaddingLeft() + 8,
                             this.getPaddingTop() + 8,
                             w - (this.getPaddingRight()) - 8,
                             h - (this.getPaddingBottom()) - 8);
    }
}
