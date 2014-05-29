package com.technoworks.fusionmonitor;

import android.app.Activity;
import android.graphics.*;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.PathShape;
import android.graphics.drawable.shapes.RectShape;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;

public class MonitorActivity extends Activity
{
    public static final int APPROXIMATE_CELL_SIZE_DP = 50;
    public static final float VERTICAL_CORRECTION = 0.925f; //A workaround! Requires fixing!

    public RelativeLayout rootLayout;

    public float mCellWidth;
    public float mCellHeight;
    private int mColumns;
    private int mRows;
    private Rect mBoundaries;
    private Point mDisplaySize;
    private ArrayList<Widget> mWidgets;
    public float mScreenDensity;
    private boolean mEditMode;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        mWidgets = new ArrayList<Widget>();
        DisplayMetrics metrics = new DisplayMetrics();
        Display display = getWindow().getWindowManager().getDefaultDisplay();
        display.getMetrics(metrics);
        mScreenDensity = metrics.density;
        mDisplaySize = new Point();
        display.getSize(mDisplaySize);
        mColumns = Math.round(mDisplaySize.x / (APPROXIMATE_CELL_SIZE_DP * mScreenDensity));
        mRows = Math.round(mDisplaySize.y / (APPROXIMATE_CELL_SIZE_DP * mScreenDensity));
        mBoundaries = new Rect(0, 0, mColumns, mRows);
        mCellWidth = (float) mDisplaySize.x / mColumns;
        mCellHeight = (float) mDisplaySize.y / mRows;
        mEditMode = false;

        ShapeDrawable[] backgroundElements = new ShapeDrawable[mColumns + mRows - 1];

        ShapeDrawable fill = new ShapeDrawable(new RectShape());
        fill.getPaint().setColor(Color.WHITE);
        //fill.setBounds(0, 0, mDisplaySize.x, mDisplaySize.y);
        backgroundElements[0] = fill;

        for (int i = 1; i < mColumns; i++)
        {
            ShapeDrawable line = new ShapeDrawable(new PathShape(generateLine(mCellWidth * i, 0, mCellWidth * i, mDisplaySize.y), mDisplaySize.x, mDisplaySize.y));
            line.setBounds(0, 0, mDisplaySize.x, mDisplaySize.y);
            line.getPaint().setColor(Color.LTGRAY);
            line.getPaint().setStyle(Paint.Style.STROKE);
            backgroundElements[i] = line;
        }
        for (int i = 1; i < mRows; i++)
        {
            ShapeDrawable line = new ShapeDrawable(new PathShape(generateLine(0, mCellHeight * i, mDisplaySize.x, mCellHeight * i), mDisplaySize.x, mDisplaySize.y));
            line.setBounds(0, 0, mDisplaySize.x, mDisplaySize.y);
            line.getPaint().setColor(Color.LTGRAY);
            line.getPaint().setStyle(Paint.Style.STROKE);
            backgroundElements[i + mColumns - 1] = line;
        }

        mCellHeight *= VERTICAL_CORRECTION; //A workaround! Requires fixing!

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
            case R.id.edit_mode:
                mEditMode = !mEditMode;
                item.setTitle(mEditMode ? R.string.edit_mode_on : R.string.edit_mode_off);
                for (Widget widget : mWidgets)
                    widget.setEditMode(mEditMode);
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

    private void addWidget()
    {
        //Widget widget = new Widget(this);
        Widget widget = new TextWidget(this);
        if (!findPlacement(widget))
        {
            Toast.makeText(this, "No room for new widget", Toast.LENGTH_SHORT).show();
            return;
        }
        widget.setPlacement();
        widget.setEditMode(mEditMode);
        //widget.setMinimumWidth((int) (mCellWidth*5));
        //widget.setMinimumHeight((int) (mCellHeight*10));
        rootLayout.addView(widget);
        mWidgets.add(widget);
    }

    private boolean findPlacement(Widget fittingWidget)
    {
        for (int i = 0; i < mRows; i++)
            for (int j = 0; j < mColumns; j++)
            {
                fittingWidget.move(j, i);
                if (checkPlacement(fittingWidget))
                    return true;
            }
        return false;
    }

    public boolean checkPlacement(Widget checkingWidget)
    {
        if (!mBoundaries.contains(checkingWidget.mPlacement))
            return false;
        for (Widget widget : mWidgets)
        {
            if (widget.equals(checkingWidget))
                continue;
            if (Rect.intersects(widget.mPlacement, checkingWidget.mPlacement))
                return false;
        }
        return true;
    }
}

