package com.technoworks.fusionmonitor.widgetSystem;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * "оболочка" над виджетом.
 * Created by lgor on 14.06.14.
 */
final class WidgetView extends View implements iWidgetView, View.OnTouchListener {

    public final iWidget widget;
    private final Rect mPosition = new Rect();

    public WidgetView(final Context context, iWidget widget, Rect position) {
        super(context);
        this.widget = widget;
        setPosition(position);
    }

    public void setPosition(Rect position) {
        mPosition.set(position);
        this.setLayoutParams(new RelativeLayout.LayoutParams(position.width(), position.height()));
        this.setX(position.left);
        this.setY(position.top);
    }

    @Override
    public void getPosition(final Rect position) {
        position.set(mPosition);
    }

    @Override
    public View castToView() {
        return this;
    }

    @Override
    protected void onDraw(final Canvas canvas) {
        widget.onDraw(canvas);
    }

    @Override
    public boolean onTouch(final View v, final MotionEvent event) {
        return widget.onTouch(v, event);
    }
}
