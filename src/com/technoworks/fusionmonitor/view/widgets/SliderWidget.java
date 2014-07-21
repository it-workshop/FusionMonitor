package com.technoworks.fusionmonitor.view.widgets;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Vladimir on 21.07.2014.
 */


public class SliderWidget extends Widget {

    public static final String TYPE = "Slider";
    public final int VERTICAL = 0, HORIZONTAL = 1;  // for constructor, if we want vertical slider
    private final Paint mPaint;
    private int mSliderOrientation = HORIZONTAL; // default
    private int mCx, mCy, mRadius, mLaunchCounter; // circle
    private int mLeftRect, mTopRect, mRightRect, mBottomRect;


    private int counter = 0;

    public SliderWidget(Context context) {
        super(context);
        attachListener(new SliderWidgetTouchListener());
        mPaint = new Paint();
    }

    public String getType() {
        return TYPE;
    }

    @Override
    protected void onDraw(Canvas canvas) {

        mPaint.setAntiAlias(true); // сглаживание ?


        if (counter > 2) {
            mPaint.setColor(Color.BLUE); // палка, по которой оно бегает
            mLeftRect = 9 * getPaddingLeft();
            mTopRect = 10 * getPaddingTop();
            mRightRect = -10 * getPaddingRight() + canvas.getWidth();
            mBottomRect = -10 * getPaddingBottom() + canvas.getHeight();
            canvas.drawRect(mLeftRect, mTopRect, mRightRect, mBottomRect, mPaint);

            mPaint.setColor(Color.RED);
            mCy = canvas.getHeight() / 2;
            mCx = canvas.getWidth() / 2;
            mRadius = mCx / 3; //  - it's a number
            canvas.drawCircle(mCx, mCy, mRadius, mPaint);
            mLaunchCounter++;

        } else {// если мы уже запустили и меняем
            mPaint.setColor(Color.BLUE);
            if (canvas.getHeight() > canvas.getWidth())
                mCy = canvas.getHeight() / 2;
            switch (mSliderOrientation) {
                case VERTICAL:
                    mCy = canvas.getHeight() / 2;
                    mRadius = canvas.getWidth() / 3; //  - it's a number

                    mTopRect = getPaddingTop() + canvas.getHeight() / 5;
                    mBottomRect = -getPaddingBottom() + canvas.getHeight() * 4 / 5;
                    break;

                case HORIZONTAL:
                    mCx = canvas.getWidth() / 2;
                    mRadius = mCy / 3; // fix later

                    mLeftRect = 9 * getPaddingLeft();
                    mRightRect = -9 * getPaddingRight() + canvas.getWidth();
                    break;
            }

            canvas.drawRect(mLeftRect, mTopRect, mRightRect, mBottomRect, mPaint);

            mPaint.setColor(Color.RED);
            canvas.drawCircle(mCx, mCy, mRadius, mPaint);
        }
    }

    protected void onChanged() {
        // to-do
    }

    private final class SliderWidgetTouchListener implements OnTouchListener {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            return false;
        }
    }
}
