package com.technoworks.fusionmonitor.view.widgets;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import com.technoworks.fusionmonitor.MessagingHelper;
import com.technoworks.fusionmonitor.MonitorActivity;
import com.technoworks.fusionmonitor.view.ChooseFieldDoubleDialogFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * тестовый виджет.
 * Можно перегрузить updateText() и выводить другой текст.
 * Created by lgor on 29.05.2014.
 */
public class TextWidget extends Widget implements ChooseFieldDoubleDialogFragment.ChooseFieldDoubleDialogListener
{
    public static final String TYPE = "Text";
    private final static int INDENT = 8;

    private final Paint mPaint;
    private final float mMaxTextSize;
    private final Rect mTextBounds;
    private final List<String> mText = new ArrayList<String>();
    private int mFieldSignature = 0;

    public TextWidget(Context context)
    {
        super(context);

        attachListener(new TextWidgetTouchListener());

        mPaint = new Paint();
        mPaint.setColor(0xFF000000);
        mMaxTextSize = 2 * mPaint.getTextSize() * ((MonitorActivity) context).mScreenDensity;
        mPaint.setTextSize(mMaxTextSize);
        mTextBounds = new Rect();
    }

    public String getType()
    {
        return TYPE;
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        updateText();

        float max = 0;
        float w = 1.0f / canvas.getWidth();
        for (String s : mText)
        {
            mPaint.getTextBounds(s, 0, s.length(), mTextBounds);
            max = Math.max((float) (mTextBounds.width() + 2 * INDENT + getPaddingLeft() + getPaddingRight()) * w, max);
        }

        float textSize = Math.min((mPaint.getTextSize() -1) / max, mMaxTextSize);
        mPaint.setTextSize(textSize);

        for (int i = 0; i < mText.size(); i++)
        {
            canvas.drawText(mText.get(i), INDENT + getPaddingLeft(), mTextBounds.height() * (i + 1) + getPaddingTop() + INDENT, mPaint);
        }
    }

    protected void updateText()
    {
        mText.clear();
        mText.add(String.valueOf(MessagingHelper.getDouble(mMonitorActivity.mLog.getLastOne(), mFieldSignature)));
    }

    @Override
    public void onChoose(int signature)
    {
        mFieldSignature = signature;
    }

    private final class TextWidgetTouchListener implements OnTouchListener
    {
        float mInitX = 0;
        float mInitY = 0;
        @Override
        public boolean onTouch(View v, MotionEvent event)
        {
            switch (event.getActionMasked())
            {
                case MotionEvent.ACTION_DOWN:
                    mInitX = event.getX();
                    mInitY = event.getY();
                    break;
                case MotionEvent.ACTION_UP:
                    float dx = event.getX() - mInitX;
                    float dy = event.getY() - mInitY;
                    Log.d("d", dx + ", " + dy);
                    if(Math.abs(dx) < dy)
                    {
                        ChooseFieldDoubleDialogFragment dialogFragment = new ChooseFieldDoubleDialogFragment(TextWidget.this);
                        dialogFragment.show(mMonitorActivity.getFragmentManager(), "chooseDoubleDialog");
                    }
            }
            return true;
        }
    }
}
