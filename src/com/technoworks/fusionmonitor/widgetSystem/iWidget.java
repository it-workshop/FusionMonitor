package com.technoworks.fusionmonitor.widgetSystem;

import android.graphics.Canvas;
import android.view.View;

/**
 * виджет. рисует себя на Canvas и обрабатывает нажатия
 * Created by lgor on 14.06.14.
 */
public interface iWidget extends View.OnTouchListener{

    public void onDraw(Canvas canvas);
}
