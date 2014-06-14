package com.technoworks.fusionmonitor.widgetSystem;

import android.content.Context;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * штука, которая хранит все виджеты и обрабатывает нажатия на них
 * Created by lgor on 14.06.14.
 */
public final class WidgetsManager implements View.OnTouchListener {

    public boolean mEditMode = false;

    private final List<iWidgetView> mWidgets = new ArrayList<iWidgetView>();

    private RelativeLayout mRootLayout;
    private Context mContext;

    private final Rect temp = new Rect(0, 0, 256, 256);

    public void init(Context context, RelativeLayout layout) {
        mContext = context;
        mRootLayout = layout;
        mWidgets.clear();
        mRootLayout.setOnTouchListener(this);
    }

    /**
     * @return success of widget adding
     */
    public boolean addWidget(iWidget widget) {
        temp.set(0, 0, 256, 256);
        int counter = 0;
        while (getIntersectionWith(temp, null) != null) {
            int dx = (int) (Math.random() * (mRootLayout.getWidth() - 256));
            int dy = (int) (Math.random() * (mRootLayout.getHeight() - 256));
            temp.set(dx, dy, temp.width()+dx, temp.height()+dy);
            counter++;
            if (counter > 100) {
                counter = 0;
                scale(temp, 0.9f, 0.9f);
            }
        }
        WidgetView wv = new WidgetView(mContext, widget, temp);
        mWidgets.add(wv);
        mRootLayout.addView(wv);
        wv.setOnTouchListener(this);
        return true;
    }

    @Override
    public boolean onTouch(final View v, final MotionEvent event) {
        if (v == mRootLayout) {
            return false;
        }
        if (mEditMode) {
            return edit((iWidgetView) v, event);
        }
        return ((iWidgetView) v).onTouch(v, event);
    }

    private float x0, y0, w0, h0;
    private final Rect startPos = new Rect();
    private final Rect currentPos = new Rect();

    private boolean edit(final iWidgetView v, final MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN: {
                v.getPosition(temp);
                x0 = event.getX() + temp.left;
                y0 = event.getY() + temp.top;
                v.getPosition(startPos);
                currentPos.set(startPos);
                return true;
            }
            case MotionEvent.ACTION_MOVE: {
                v.getPosition(temp);
                float x = event.getX() + temp.left;
                float y = event.getY() + temp.top;
                if (event.getPointerCount() > 1) {
                    if (w0 == 0 && h0 == 0) {
                        w0 = Math.abs(event.getX(0) - event.getX(1));
                        h0 = Math.abs(event.getY(0) - event.getY(1));
                    }
                    float w = Math.abs(event.getX(0) - event.getX(1));
                    float h = Math.abs(event.getY(0) - event.getY(1));
                    currentPos.set(startPos);
                    scale(currentPos, w / w0, h / h0);
                }
                else {
                    w0 = h0 = 0;
                }
                temp.set(currentPos);
                temp.offset((int) (x - x0), (int) (y - y0));
                v.setPosition(temp);
                return true;
            }
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                v.getPosition(temp);
                Rect frame = new Rect(temp);
                scale(frame, 0.8f, 0.8f);
                if (getIntersectionWith(frame, v) != null) {
                    v.setPosition(startPos);
                    return false;
                }
                frame.set(temp);
                scale(frame, 1.25f, 1.25f);
                if (getIntersectionWith(frame, v) == null) {
                    v.setPosition(temp);
                    return false;
                }
                Rect frameMax = new Rect(frame);
                correctRect(frame, v);
                if (frame.top == frameMax.top) frame.top = temp.top;
                if (frame.left == frameMax.left) frame.left = temp.left;
                if (frame.right == frameMax.right) frame.right = temp.right;
                if (frame.bottom == frameMax.bottom) frame.bottom = temp.bottom;
                v.setPosition(frame);
                return false;
            default:
                return true;
        }
    }

    private Rect[] mBounds;

    private void initBounds() {
        if (mBounds != null) return;
        int l = 10000;
        mBounds = new Rect[4];
        mBounds[0] = new Rect(-l, -l, l, 0);
        mBounds[1] = new Rect(-l, -l, 0, l);
        mBounds[2] = new Rect(mRootLayout.getRight(), -l, l, l);
        mBounds[3] = new Rect(-l, mRootLayout.getBottom(), l, l);
    }

    private Rect getIntersectionWith(Rect rect, iWidgetView skip) {
        {
            Rect tmp = new Rect();
            for (iWidgetView view : mWidgets) {
                if (view == skip) continue;
                view.getPosition(tmp);
                if (Rect.intersects(tmp, rect)) {
                    return tmp;
                }
            }
        }
        initBounds();
        for (Rect tmp : mBounds) {
            if (Rect.intersects(tmp, rect)) {
                return tmp;
            }
        }
        return null;
    }


    private void correctRect(Rect rect, iWidgetView skip) {
        Rect temp = new Rect();
        for (iWidgetView view : mWidgets) {
            if (view == skip) continue;
            view.getPosition(temp);
            correctRect(rect, temp);
        }
        initBounds();
        for (Rect r : mBounds) {
            temp.set(r);
            correctRect(rect, temp);
        }
    }

    private static void correctRect(Rect corrected, Rect another) {
        if (!corrected.intersects(another, corrected)) {
            return;
        }
        another.intersect(corrected);
        if (another.width() > another.height()) {
            if (another.top == corrected.top) {
                corrected.top = another.bottom;
            }
            else {
                corrected.bottom = another.top;
            }
        }
        else {
            if (another.left == corrected.left) {
                corrected.left = another.right;
            }
            else {
                corrected.right = another.left;
            }
        }
    }

    private static void scale(Rect r, float sx, float sy) {
        int dx = Math.round(sx * r.width() / 2);
        int dy = Math.round(sy * r.height() / 2);
        r.set(r.centerX() - dx, r.centerY() - dy, r.centerX() + dx, r.centerY() + dy);
    }
}
