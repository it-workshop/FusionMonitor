package com.technoworks.fusionmonitor;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.*;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.PathShape;
import android.graphics.drawable.shapes.RectShape;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.*;
import android.widget.RelativeLayout;

import java.util.ArrayList;

public class MonitorActivity extends Activity
{
    public static final int APPROXIMATE_CELL_SIZE_DP = 50;

    public RelativeLayout rootLayout;

    private float mCellWidth;
    private float mCellHeight;
    private int mColumns;
    private int mRows;
    private Point mDisplaySize;
    public float mScreenDensity;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        DisplayMetrics metrics = new DisplayMetrics();
        Display display = getWindow().getWindowManager().getDefaultDisplay();
        display.getMetrics(metrics);
        mScreenDensity = metrics.density;
        mDisplaySize = new Point();
        display.getSize(mDisplaySize);
        mColumns = Math.round(mDisplaySize.x / (APPROXIMATE_CELL_SIZE_DP * mScreenDensity));
        mRows = Math.round(mDisplaySize.y / (APPROXIMATE_CELL_SIZE_DP * mScreenDensity));
        mCellWidth = (float) mDisplaySize.x / mColumns;
        mCellHeight = (float) mDisplaySize.y / mRows;

        ShapeDrawable[] backgroundElements = new ShapeDrawable[mColumns + mRows - 1];

        ShapeDrawable fill = new ShapeDrawable(new RectShape());
        fill.getPaint().setColor(Color.WHITE);
        //fill.setBounds(0, 0, mDisplaySize.x, mDisplaySize.y);
        backgroundElements[0] = fill;

        for(int i = 1; i < mColumns; i++)
        {
            ShapeDrawable line = new ShapeDrawable(new PathShape(generateLine(mCellWidth*i, 0, mCellWidth*i, mDisplaySize.y), mDisplaySize.x, mDisplaySize.y));
            line.setBounds(0, 0, mDisplaySize.x, mDisplaySize.y);
            line.getPaint().setColor(Color.LTGRAY);
            line.getPaint().setStyle(Paint.Style.STROKE);
            backgroundElements[i] = line;
        }
        for(int i = 1; i < mRows; i++)
        {
            ShapeDrawable line = new ShapeDrawable(new PathShape(generateLine(0, mCellHeight*i, mDisplaySize.x, mCellHeight*i), mDisplaySize.x, mDisplaySize.y));
            line.setBounds(0, 0, mDisplaySize.x, mDisplaySize.y);
            line.getPaint().setColor(Color.LTGRAY);
            line.getPaint().setStyle(Paint.Style.STROKE);
            backgroundElements[i + mColumns - 1] = line;
        }

        LayerDrawable background = new LayerDrawable(backgroundElements);

        rootLayout = new RelativeLayout(this);
        rootLayout.setBackground(background);

        setContentView(rootLayout);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.add_empty:
                addWidget();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private Path generateLine(float x1, float y1, float x2, float y2)
    {
        Path line = new Path();
        line.moveTo(x1, y1);
        line.lineTo(x2, y2);
        return line;
    }

    Widget widget;

    private void addWidget()
    {
        widget = new Widget(this);
        widget.setLayoutParams(new RelativeLayout.LayoutParams((int) mCellWidth*5, (int) mCellHeight*10));
        widget.setX(mCellWidth);
        widget.setY(mCellHeight*2);
        //widget.setMinimumWidth((int) (mCellWidth*5));
        //widget.setMinimumHeight((int) (mCellHeight*10));
        rootLayout.addView(widget);
    }
}

