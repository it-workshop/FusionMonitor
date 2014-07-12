package com.technoworks.fusionmonitor;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.InsetDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.RelativeLayout;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Всеволод on 11.05.2014.
 */
public class Widget extends View
{
    public static final int PADDING_DP = 5;
    protected static final int[] DEFAULT_SIZE = {3, 3};

    protected OnTouchListener mInnerListener = null;
    protected EditModeTouchListener mEditModeTouchListener;

    private int mPadding;
    public boolean mIsInEditMode = false;
    protected MonitorActivity mMonitorActivity;

    public Rect mPlacement;

    public static String getType()
    {
        return "Default";
    }

    public Widget(Context context)
    {
        super(context);

        mPlacement = new Rect(0, 0, DEFAULT_SIZE[0], DEFAULT_SIZE[1]);

        mEditModeTouchListener = new EditModeTouchListener();
        mMonitorActivity = (MonitorActivity) context;
        mPadding = (int) (mMonitorActivity.mScreenDensity * PADDING_DP);

        Drawable[] background = new Drawable[2];

        ShapeDrawable fill = new ShapeDrawable(new RectShape());
        fill.getPaint().setColor(Color.WHITE);
        fill.getPaint().setStyle(Paint.Style.FILL);
        background[0] = fill;

        ShapeDrawable frame = new ShapeDrawable(new RectShape());
        frame.getPaint().setColor(Color.CYAN);
        frame.getPaint().setStyle(Paint.Style.STROKE);
        background[1] = frame;

        setBackground(new InsetDrawable(new LayerDrawable(background), mPadding));

        setPadding(mPadding, mPadding, mPadding, mPadding);
    }

    protected void applySettings(JSONObject settings)
    {
        return;
    }

    protected void attachListener(OnTouchListener listener)
    {
        mInnerListener = listener;
        setOnTouchListener(mInnerListener);
    }

    public String save()
    {
        JSONObject settings = new JSONObject();
        try
        {
            settings.put("type", getType());
            settings.put("left", mPlacement.left);
            settings.put("top", mPlacement.top);
            settings.put("right", mPlacement.right);
            settings.put("bottom", mPlacement.bottom);
            settings.put("settings", saveSettings());
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        return settings.toString();
    }

    protected JSONObject saveSettings()
    {
        return new JSONObject();
    }

    public void setEditMode(boolean editMode)
    {
        mIsInEditMode = editMode;
        if (editMode)
            setOnTouchListener(mEditModeTouchListener);
        else
            setOnTouchListener(mInnerListener);
    }

    protected void setPlacement()
    {
        setX(mMonitorActivity.mCellWidth * mPlacement.left);
        setY(mMonitorActivity.mCellHeight * mPlacement.top);
        setLayoutParams(new RelativeLayout.LayoutParams((int) (mMonitorActivity.mCellWidth * mPlacement.width()), (int) (mMonitorActivity.mCellHeight * mPlacement.height())));
    }

    protected void move(int left, int top, int right, int bottom)
    {
        mPlacement.set(left, top, right, bottom);
        setPlacement();
    }

    protected void move(Rect r)
    {
        mPlacement.set(r);
        setPlacement();
    }

    protected void move(int left, int top)
    {
        mPlacement.offsetTo(left, top);
        setPlacement();
    }

    protected class EditModeTouchListener implements OnTouchListener
    {
        public static final int MODE_DRAG = 0;
        public static final int MODE_RESIZE_TOP = 1;
        public static final int MODE_RESIZE_RIGHT = 2;
        public static final int MODE_RESIZE_BOTTOM = 4;
        public static final int MODE_RESIZE_LEFT = 8;

        public static final float RESIZE_BORDERS_INSET_MULTIPLIER = 3;

        private int mMode;
        private float mRelativeInitX;
        private float mRelativeInitY;
        private float mInitHeight;
        private float mInitWidth;

        private float dx;
        private float dy;

        @Override
        public boolean onTouch(View v, MotionEvent event)
        {
            if (event.getActionMasked() == MotionEvent.ACTION_DOWN)
            {
                mMode = 0;
                if (event.getY() < mPadding * RESIZE_BORDERS_INSET_MULTIPLIER)
                    mMode += MODE_RESIZE_TOP;
                if (event.getX() < mPadding * RESIZE_BORDERS_INSET_MULTIPLIER)
                    mMode += MODE_RESIZE_LEFT;
                if (event.getY() > v.getHeight() - mPadding * RESIZE_BORDERS_INSET_MULTIPLIER)
                    mMode += MODE_RESIZE_BOTTOM;
                if (event.getX() > v.getWidth() - mPadding * RESIZE_BORDERS_INSET_MULTIPLIER)
                    mMode += MODE_RESIZE_RIGHT;

                mRelativeInitX = event.getX();
                mRelativeInitY = event.getY();
                mInitHeight = v.getLayoutParams().height;
                mInitWidth = v.getLayoutParams().width;

                return true;
            }
            else if (event.getActionMasked() == MotionEvent.ACTION_MOVE)
            {
                if (mMode == MODE_DRAG)
                {
                    v.setX(v.getX() + event.getX() - mRelativeInitX);
                    v.setY(v.getY() + event.getY() - mRelativeInitY);
                }

                if ((mMode & MODE_RESIZE_TOP) == MODE_RESIZE_TOP)
                {
                    dy = event.getY() - mRelativeInitY;
                    v.setY(v.getY() + dy);
                    v.setLayoutParams(new RelativeLayout.LayoutParams(v.getLayoutParams().width, Math.round(v.getLayoutParams().height - dy)));
                }

                if ((mMode & MODE_RESIZE_LEFT) == MODE_RESIZE_LEFT)
                {
                    dx = event.getX() - mRelativeInitX;
                    v.setX(v.getX() + dx);
                    v.setLayoutParams(new RelativeLayout.LayoutParams(Math.round(v.getLayoutParams().width - dx), v.getLayoutParams().height));
                }

                if ((mMode & MODE_RESIZE_BOTTOM) == MODE_RESIZE_BOTTOM)
                {
                    dy = event.getY() - mRelativeInitY;
                    v.setLayoutParams(new RelativeLayout.LayoutParams(v.getLayoutParams().width, (int) (mInitHeight + dy)));
                }

                if ((mMode & MODE_RESIZE_RIGHT) == MODE_RESIZE_RIGHT)
                {
                    dx = event.getX() - mRelativeInitX;
                    v.setLayoutParams(new RelativeLayout.LayoutParams((int) (mInitWidth + dx), v.getLayoutParams().height));
                }

                return true;
            }
            else if (event.getActionMasked() == MotionEvent.ACTION_UP)
            {
                Rect oldPlacement = new Rect(mPlacement);
                int left = Math.round(v.getX() / mMonitorActivity.mCellWidth);
                int top = Math.round(v.getY() / mMonitorActivity.mCellHeight);
                int right = left + Math.round(v.getLayoutParams().width / mMonitorActivity.mCellWidth);
                int bottom = top + Math.round(v.getLayoutParams().height / mMonitorActivity.mCellHeight);
                ((Widget) v).move(left, top, right, bottom);
                if (!mMonitorActivity.checkPlacement((Widget) v))
                    ((Widget) v).move(oldPlacement);
            }
            return false;
        }
    }
}
