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

/**
 * тестовый виджет.
 * Можно перегрузить updateText() и выводить другой текст.
 * Created by lgor on 29.05.2014.
 */
public class TextWidget extends Widget implements ChooseFieldDoubleDialogFragment.ChooseFieldDoubleDialogListener
{
    public static final String TYPE = "Text";
    private final static int INDENT = 8;
    private static final float INSET_MULTIPLIER = 0.8f;

    private final Paint mPaint;
    private final float mMaxTextSize;
    private final Rect mTextBounds;
    private float mTextForMonitor;
    private int mFieldSignature = 0;

    public String getType()
    {
        return TYPE;
    }

    public TextWidget(Context context)
    {
        super(context);

        attachListener(new TextWidgetTouchListener());

        mPaint = new Paint();
        mPaint.setColor(0xFF000000);
        mMaxTextSize = 2* mPaint.getTextSize() * ((MonitorActivity) context).mScreenDensity;
        mPaint.setTextSize(mMaxTextSize);
        mTextBounds = new Rect();
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        String mText = Float.toString(mTextForMonitor);
        updateText();
        int i;
        if (mText.contains(",")) {
            i = 7;
        }
        else {
            i = 6;
        }
        mText = mText.substring(0, i > mText.length() ? mText.length() : i);

        float max = 0;
        float w = 1.0f / canvas.getWidth();

        mPaint.setTextSize(100);
        mPaint.getTextBounds(mText, 0, mText.length(), mTextBounds);
        float xScale = (canvas.getWidth() - getPaddingLeft() - getPaddingRight())*INSET_MULTIPLIER/mTextBounds.width();
        float yScale = (canvas.getHeight() - getPaddingTop() - getPaddingBottom())* INSET_MULTIPLIER/mTextBounds.height();

        float mash = xScale < yScale ? xScale : yScale;
        float textSize = mPaint.getTextSize()*mash;
        mPaint.setTextSize(textSize);
        float mTextWidthMiddle = mTextBounds.width()*mash/2;
        float mTextHeightMiddle = mTextBounds.height()*mash/2;
        float canvasWidthMiddle = canvas.getWidth()/2;
        float canvasHeightMiddle = canvas.getHeight()/2;
        canvas.drawText(mText, canvasWidthMiddle - mTextWidthMiddle, canvasHeightMiddle + mTextHeightMiddle, mPaint);
    }

    protected void updateText()
    {
        mTextForMonitor = (float) MessagingHelper.getDouble(mMonitorActivity.mLog.getLastOne(), mFieldSignature);
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
