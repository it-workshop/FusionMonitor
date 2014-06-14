package com.technoworks.fusionmonitor.widgetSystem;

import android.graphics.Canvas;

/**
 * виджет. рисует себя на Canvas и обрабатывает нажатия
 * Created by lgor on 14.06.14.
 */
public interface iWidget {

    public void onDraw(Canvas canvas);

    public boolean onTouch(Touch touch);
}
