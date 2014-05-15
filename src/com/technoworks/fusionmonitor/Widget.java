package com.technoworks.fusionmonitor;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.InsetDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Всеволод on 11.05.2014.
 */
public class Widget extends View
{

    public static final int PADDING_DP = 0;

    private int inset;

    public Widget(Context context)
    {
        super(context);

        inset = (int) (((MonitorActivity) context).mScreenDensity * PADDING_DP);

        ShapeDrawable frame = new ShapeDrawable(new RectShape());
        frame.getPaint().setColor(Color.CYAN);
        frame.getPaint().setStyle(Paint.Style.STROKE);

        InsetDrawable background = new InsetDrawable(frame, inset);

        setBackground(background);
    }

    protected class EditModeTouchListener implements OnTouchListener
    {

        @Override
        public boolean onTouch(View v, MotionEvent event)
        {
            return false;
        }
    }
}
