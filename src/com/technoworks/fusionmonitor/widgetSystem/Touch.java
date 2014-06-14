package com.technoworks.fusionmonitor.widgetSystem;

import android.graphics.Rect;
import android.view.MotionEvent;

/**
 * Created by lgor on 09.02.14.
 * нажатие.
 * Если в конкретный момент нажатий несколько, первое касание хранится в ссылке next второго касания и т.д
 */
public class Touch {

    public static int
            PRESSED = 1,
            DRAGGED = 2,
            RELEASED = 3;
    private static String[] typeString = {"error", "pressed", "dragged", "released"};

    //поля public, но они final и с ними ничего страшного не случится
    public final float x, y;
    public final int type;
    public final Touch next;      //next!=null если в данный момент есть ещё одно нажатие
    private final int id;
    private final Touch old;

    private Touch(float x, float y, int type, int id, Touch next, Touch old) {
        this.x = x;
        this.y = y;
        this.type = type;
        this.id = id;
        this.next = next;
        this.old = old;
    }

    private static Touch find(int id, Touch prev) {
        while (prev != null) {
            if (prev.id == id) {
                return prev;
            }
            prev = prev.next;
        }
        return null;
    }

    public static Touch getTouches(MotionEvent event, Touch prev) {
        Touch t = null;
        int mask = event.getActionMasked();
        int pointerWillUp = -1;
        if (mask == MotionEvent.ACTION_POINTER_UP || mask == MotionEvent.ACTION_UP || mask == MotionEvent.ACTION_CANCEL) {
            pointerWillUp = event.getActionIndex();
        }
        boolean newPointer = (mask == MotionEvent.ACTION_DOWN);
        for (int i = 0; i < event.getPointerCount(); i++) {
            float x, y;
            int id;
            x = event.getX(i);
            y = event.getY(i);
            id = event.getPointerId(i);
            Touch sample = find(id, prev);

            if (!newPointer) {                           //продолжение или конец нажатия
                t = new Touch(x, y, pointerWillUp == id ? RELEASED : DRAGGED, id, t, sample);
            }
            else {
                t = new Touch(x, y, PRESSED, id, t, null);
            }
        }
        return t;
    }

    /**
     * самое начало нажатия
     */
    public boolean firstTouch() {
        return (next == null) && (type == PRESSED);
    }

    /**
     * самое последнее событие нажатия
     */
    public boolean lastTouch() {
        return (next == null) && (type == RELEASED);
    }

    /**
     * принадлежит ли нажатие прямоугольнику
     */
    public boolean into(Rect r) {
        return (x > r.left && x < r.right && y < r.bottom && y > r.top);
    }

    /**
     * количество одновременных нажатий
     */
    public int count() {
        Touch t = this;
        int r = 1;
        while (t.next != null) {
            r++;
            t = t.next;
        }
        return r;
    }

    public float oldX() {
        return old != null ? old.x : x;
    }

    public float oldY() {
        return old != null ? old.y : y;
    }

    public float dx() {
        return old != null ? x - old.x : 0;
    }

    public float dy() {
        return old != null ? y - old.y : 0;
    }

    @Override
    public String toString() {
        return (int) x + "," + (int) y + "," + id + "," + typeString[type] + "; " +
                (next != null ? next.toString() : "");
    }
}

