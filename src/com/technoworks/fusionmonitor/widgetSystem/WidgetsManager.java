package com.technoworks.fusionmonitor.widgetSystem;

import android.content.Context;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * штука, которая хранит все виджеты и обрабатывает нажатия на них
 * Created by lgor on 14.06.14.
 */
public final class WidgetsManager implements View.OnTouchListener {

    private final static int DELTA = 64;

    private final List<iWidgetView> mWidgets = new ArrayList<iWidgetView>();

    public boolean mEditMode = false;

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
                    scale(currentPos, w/w0, h/h0);
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
                return false;
            default:
                return true;
        }
    }

    private static void scale(Rect r, float sx, float sy) {
        int dx = Math.round(sx * r.width() / 2);
        int dy = Math.round(sy * r.height() / 2);
        r.set(r.centerX() - dx, r.centerY() - dy, r.centerX() + dx, r.centerY() + dy);
    }

    private Rect getIntersectionWith(Rect rect, iWidgetView skip) {
        Rect temp = new Rect();
        for (iWidgetView view : mWidgets) {
            if (view == skip) continue;
            view.getPosition(temp);
            if (temp.right <= rect.left || rect.right <= temp.left) continue;
            if (temp.top >= rect.bottom || temp.bottom <= rect.top) continue;
            return rect;
        }
        return null;
    }
}
