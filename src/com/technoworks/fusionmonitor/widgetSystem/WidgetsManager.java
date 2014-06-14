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
public class WidgetsManager implements View.OnTouchListener {

    private final List<iWidgetView> mWidgets = new ArrayList<iWidgetView>();

    public boolean mEditMode = false;

    private RelativeLayout mRootLayout;
    private Context mContext;

    private final Rect temp = new Rect(0, 0, 256, 256);

    public void reload(Context context, RelativeLayout layout) {
        mContext = context;
        mRootLayout = layout;
        //TODO loading
        mWidgets.clear();
        mRootLayout.setOnTouchListener(this);
    }

    /**
     * @return success of widget adding
     */
    public boolean addWidget(iWidget widget) {
        WidgetView wv = new WidgetView(mContext, widget, temp);
        mWidgets.add(wv);
        mRootLayout.addView(wv);
        wv.setOnTouchListener(this);
        return true;
    }

    @Override
    public boolean onTouch(final View v, final MotionEvent event) {
        if (mEditMode) {
            return edit(v, event);
        }
        return touch(v, event);
    }

    private boolean touch(final View v, final MotionEvent event) {
        if (v == mRootLayout) {
            //нажатие мимо
            return false;
        }
        iWidgetView view = (iWidgetView) v;
        view.onTouch(v, event);
        return true;
    }

    private Touch t;
    private View activeView;

    private boolean edit(final View v, final MotionEvent event) {
        if (v != mRootLayout) {
            activeView = v;
            return false;
        }
        if (activeView == null){
            return false;
        }

        iWidgetView view = (iWidgetView) activeView;
        view.getPosition(temp);

        t = Touch.getTouches(event, t);
        temp.offset(Math.round(t.dx()), Math.round(t.dy()));
        view.setPosition(temp);
        return true;
    }
}
