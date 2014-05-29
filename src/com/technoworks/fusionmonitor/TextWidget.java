package com.technoworks.fusionmonitor;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import java.util.ArrayList;
import java.util.List;

/**
 * тестовый виджет.
 * Можно перегрузить updateText() и выводить другой текст.
 * Created by lgor on 29.05.2014.
 */
public class TextWidget extends Widget
{
    private final static int INDENT = 8;

    private final Paint mPaint;
    private final float mMaxTextSize;
    private final Rect mTextBounds;
    private final List<String> mText = new ArrayList<String>();
    private int mDrawCalls = 0;

    public TextWidget(Context context)
    {
        super(context);
        mPaint = new Paint();
        mPaint.setColor(0xFF00FF00);
        mMaxTextSize = 2* mPaint.getTextSize() * ((MonitorActivity) context).mScreenDensity;
        mPaint.setTextSize(mMaxTextSize);
        mTextBounds = new Rect();
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        //super.onDraw(canvas);
        canvas.drawColor(0xFF000000);

        updateText(canvas.getWidth(), canvas.getHeight());

        float max = 0;
        float w = 1.0f / canvas.getWidth();
        for (String s : mText)
        {
            mPaint.getTextBounds(s, 0, s.length(), mTextBounds);
            max = Math.max((float) (mTextBounds.width() + 2 * INDENT) * w, max);
        }

        float textSize = Math.min((mPaint.getTextSize() -1) / max, mMaxTextSize);
        mPaint.setTextSize(textSize);

        for (int i = 0; i < mText.size(); i++)
        {
            canvas.drawText(mText.get(i), INDENT, mTextBounds.height() * (i + 1), mPaint);
        }
    }

    protected void updateText(int w, int h)
    {
        mText.clear();
        mText.add("draw calls : " + mDrawCalls++);
        mText.add("drawing size = (" + w + "," + h + ")");
        mText.add("total memory = " + (Runtime.getRuntime().totalMemory() >> 10) + " kb");
    }
}
