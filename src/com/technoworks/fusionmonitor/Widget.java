package com.technoworks.fusionmonitor;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.InsetDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Всеволод on 11.05.2014.
 */
public class Widget extends View
{
    public static final int PADDING_DP = 5;

    protected OnTouchListener mInnerListener = null;
    protected EditModeTouchListener mEditModeTouchListener;

    private int mInset;
    private boolean mIsInEditMode;
    private MonitorActivity mMonitorActivity;

    public Widget(Context context)
    {
        super(context);

        mEditModeTouchListener = new EditModeTouchListener();
        mMonitorActivity = (MonitorActivity) context;
        mInset = (int) (mMonitorActivity.mScreenDensity * PADDING_DP);
        mIsInEditMode = false;

        Drawable[] background = new Drawable[2];

        ShapeDrawable fill = new ShapeDrawable(new RectShape());
        fill.getPaint().setColor(Color.WHITE);
        fill.getPaint().setStyle(Paint.Style.FILL);
        background[0] = fill;//new InsetDrawable(fill, mInset);

        ShapeDrawable frame = new ShapeDrawable(new RectShape());
        frame.getPaint().setColor(Color.CYAN);
        frame.getPaint().setStyle(Paint.Style.STROKE);
        background[1] = frame;//new InsetDrawable(frame, mInset);

        setBackground(new InsetDrawable(new LayerDrawable(background), mInset));
    }

    public void attachListener(OnTouchListener listener)
    {
        mInnerListener = listener;
        setOnTouchListener(mInnerListener);
    }

    public void editModeOn()
    {
        Log.d("Edit mode", "On");
        mIsInEditMode = true;
        setOnTouchListener(mEditModeTouchListener);
    }

    public void editModeOff()
    {
        Log.d("Edit mode", "Off");
        mIsInEditMode = false;
        setOnTouchListener(mInnerListener);
    }

    public void toggleEditMode()
    {
        Log.d("Edit mode", "Toggle");
        if(mIsInEditMode)
        {
            setOnTouchListener(mInnerListener);
            mIsInEditMode = false;
        }
        else
        {
            setOnTouchListener(mEditModeTouchListener);
            mIsInEditMode = true;
        }
    }

    protected class EditModeTouchListener implements OnTouchListener
    {
        public static final int MODE_DRAG = 0;
        public static final int MODE_RESIZE_TOP = 1;
        public static final int MODE_RESIZE_RIGHT = 2;
        public static final int MODE_RESIZE_BOTTOM = 4;
        public static final int MODE_RESIZE_LEFT = 8;

        public static final float RESIZE_BORDERS_INSET_MULTIPLIER = 8;

        private int mMode;
        private float mRelativeInitX;
        private float mRelativeInitY;

        @Override
        public boolean onTouch(View v, MotionEvent event)
        {
            Log.wtf("event", event.toString());
            if(event.getActionMasked() == MotionEvent.ACTION_DOWN)
            {
                mMode = 0;
                if(event.getY() < mInset *RESIZE_BORDERS_INSET_MULTIPLIER)
                    mMode +=MODE_RESIZE_TOP;
                if(event.getX() < mInset *RESIZE_BORDERS_INSET_MULTIPLIER)
                    mMode +=MODE_RESIZE_LEFT;
                if(event.getY() > v.getHeight() - mInset *RESIZE_BORDERS_INSET_MULTIPLIER)
                    mMode +=MODE_RESIZE_BOTTOM;
                if(event.getX() > v.getWidth() - mInset *RESIZE_BORDERS_INSET_MULTIPLIER)
                    mMode +=MODE_RESIZE_RIGHT;

                mRelativeInitX = event.getX();
                mRelativeInitY = event.getY();

                return true;
            }
            else if(event.getActionMasked() == MotionEvent.ACTION_MOVE)
            {
                if(mMode == MODE_DRAG)
                {
                    v.setX(v.getX()+event.getX()-mRelativeInitX);
                    v.setY(v.getY()+event.getY()-mRelativeInitY);
                }
                return true;
            }
            else if(event.getActionMasked() == MotionEvent.ACTION_UP)
            {
                if(mMode == MODE_DRAG)
                {
                    v.setX(mMonitorActivity.mCellWidth*Math.round(v.getX()/mMonitorActivity.mCellWidth));
                    v.setY(mMonitorActivity.mCellHeight*Math.round(v.getY()/mMonitorActivity.mCellHeight));
                    Log.d("Align", v.getX() + " " + v.getY());
                }
            }
            return false;
        }
    }
}
