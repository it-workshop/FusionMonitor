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

    public ArrayList<Double> values = new ArrayList<Double>();
    private Paint backgroundPaint = new Paint();
    private RectF graphBounds = new RectF();

    public WidgetGraph2(Context context)
    {
        super(context);

        this.backgroundPaint.setColor(Color.BLACK);
        refreshSizes(DEFAULT_SIZE[0] * mMonitorActivity.mCellWidth,
                     DEFAULT_SIZE[1] * mMonitorActivity.mCellHeight);
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
