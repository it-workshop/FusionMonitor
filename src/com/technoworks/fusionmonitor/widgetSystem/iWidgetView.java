package com.technoworks.fusionmonitor.widgetSystem;

import android.graphics.Rect;
import android.view.View;

/**
 * скрытый класс для коварных целей
 * Created by lgor on 14.06.14.
 */
interface iWidgetView extends View.OnTouchListener {

    public void setPosition(Rect position);

    public void getPosition(Rect position);

    /**
     * для того, чтобы можно было сделать реализацию iWidgetView, основанную на SurfaceView
     */
    public View castToView();
}
