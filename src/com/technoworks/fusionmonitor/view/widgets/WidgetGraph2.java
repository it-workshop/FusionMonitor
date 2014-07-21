package com.technoworks.fusionmonitor.view.widgets;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import com.technoworks.fusionmonitor.Messaging;
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

    public int valueCount = 30;
    public ArrayList<Float> values = new ArrayList<Float>();
    private final Paint backgroundPaint = new Paint();
    private final Paint graphPaint = new Paint();
    private final Paint foregroundPaint = new Paint();
    private RectF graphBounds = new RectF();
    public int separatorsH = 1;
    public int separatorsV = 10;
    public int precision = 3; //Amount of numbers after the comma that the graph will be sensetive to


    public WidgetGraph2(Context context)
    {
        super(context);

        this.backgroundPaint.setColor(Color.WHITE);
        this.graphPaint.setColor(Color.GREEN);
        this.foregroundPaint.setColor(Color.BLACK);
        foregroundPaint.setTextSize(20);

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

    private void updateValues()
    {
        values.clear();

        ArrayList<Messaging.Telemetry> list = mMonitorActivity.mLog.getLastN(valueCount);

        for(Messaging.Telemetry tel : list)
        {
            values.add((float) tel.getForce());
        }
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        updateValues();

        canvas.save();
        {
            canvas.drawRect(graphBounds, backgroundPaint);

            canvas.translate(graphBounds.left, graphBounds.top);

            float maxval = 0;
            for(float val : this.values) if(Math.abs(val) > maxval) maxval = Math.abs(val);

            float w = (graphBounds.right - graphBounds.left),
                  h = (graphBounds.bottom - graphBounds.top);

            int estH = graphSeparationFunc(maxval);
            int preH = graphSeparationFunc(maxval * (float) ((Math.pow(10, precision))));

            //The graph
            for(int i = 0; i < valueCount - 1; i++)
            {
                if(values.size() <= i || values.size() <= i + 1) break;

                float sx = i * (w / (this.values.size() - 1)),
                      ex = (i + 1) * (w / (this.values.size() - 1)),
                      sy = (h / 2f) - (this.values.get(i) * (h / 2 / estH)),
                      ey = (h / 2f) - (this.values.get(i + 1) * (h / 2 / estH));
                canvas.drawLine(sx, sy, ex, ey, graphPaint);
            }

            canvas.drawLine(0, h / 2, w, h / 2, foregroundPaint);

            //The horizontal separators
            int hsc = (valueCount - 1) * separatorsH; //Horizontal separators count
            for(int i = 1; i < hsc; i++)
            {
                if(values.size() <= i) break;

                float l = w / 80f; //Length of a separator
                float a = i * w / hsc; //Horizontal position of a separator

                if(i % separatorsH == 0)
                {
                    l *= 2;
                }

                canvas.drawLine(a, (h / 2) - l, a, (h / 2) + l, foregroundPaint);
            }

            //The vertical separators
            int vsc = preH / separatorsV; //Vertical separators count
            for(int i = -vsc / 2; i < vsc / 2 + 1; i++)
            {
                float l = h / 20f; //Length of a separator
                float a = (h / 2) - (i * h / vsc); //Vertical position of the separator

                if(i % separatorsV == 0)
                {
                    l *= 2;
                    canvas.drawText(Float.toString(i / ((float) vsc) * estH), l + 2, a + (foregroundPaint.getTextSize() / 2), foregroundPaint);
                }

                canvas.drawLine(0, a, l, a, foregroundPaint);
            }
        }
        canvas.restore();
    }

    protected int graphSeparationFunc(float f)
    {
        float a = (float) (Math.pow(10, Math.floor(Math.log10(f))));
        return (int) (Math.ceil(f / a) * a);
    }

    protected void refreshSizes(float w, float h)
    {
        this.graphBounds.set(this.getPaddingLeft() + 8,
                             this.getPaddingTop() + 8,
                             w - (this.getPaddingRight()) - 8,
                             h - (this.getPaddingBottom()) - 8);
    }

    public String getType()
    {
        return TYPE;
    }
}
