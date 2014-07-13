package com.technoworks.fusionmonitor;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
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
import android.view.SubMenu;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.technoworks.fusionmonitor.controller.SimulationThread;
import com.technoworks.fusionmonitor.model.LoggerList;
import com.technoworks.fusionmonitor.view.RemoveLayoutDialogFragment;
import com.technoworks.fusionmonitor.view.SaveLayoutDialogFragment;
import com.technoworks.fusionmonitor.view.widgets.Widget;
import com.technoworks.fusionmonitor.view.widgets.WidgetParser;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class MonitorActivity extends Activity
{
    public static final int APPROXIMATE_CELL_SIZE_DP = 50;

    private RelativeLayout mRootLayout;
    private ArrayList<Widget> mWidgets;
    public LoggerList mLog;
    private SimulationThread mSimulationThread;
    public SharedPreferences mLayouts;

    public float mCellWidth;
    public float mCellHeight;
    private int mColumns;
    private int mRows;
    private Rect mBoundaries;
    private Point mDisplaySize;
    public float mScreenDensity;

    private boolean mEditMode;
    private boolean mSimulationOn;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        WidgetParser.init();
        MessagingHelper.init();

        mLayouts = getSharedPreferences("layouts", 0);

        mWidgets = new ArrayList<Widget>();
        mLog = new LoggerList(mWidgets);

        DisplayMetrics metrics = new DisplayMetrics();
        Display display = getWindow().getWindowManager().getDefaultDisplay();
        display.getMetrics(metrics);
        mScreenDensity = metrics.density;
        mDisplaySize = new Point();
        display.getSize(mDisplaySize);

        final TypedArray styledAttributes = getTheme().obtainStyledAttributes(new int[] {android.R.attr.actionBarSize});
        float actionBarHeight = styledAttributes.getDimension(0, 0);
        styledAttributes.recycle();

        mColumns = Math.round(mDisplaySize.x / (APPROXIMATE_CELL_SIZE_DP * mScreenDensity));
        mRows = Math.round((mDisplaySize.y - actionBarHeight) / (APPROXIMATE_CELL_SIZE_DP * mScreenDensity));
        mCellWidth = (float) mDisplaySize.x / mColumns;
        mCellHeight = (mDisplaySize.y - actionBarHeight) / mRows;
        mBoundaries = new Rect(0, 0, mColumns, mRows);
        mEditMode = false;
        mSimulationOn = false;

        ShapeDrawable[] backgroundElements = new ShapeDrawable[mColumns + mRows - 1];

        ShapeDrawable fill = new ShapeDrawable(new RectShape());
        fill.getPaint().setColor(Color.WHITE);
        backgroundElements[0] = fill;

        for (int i = 1; i < mColumns; i++)
        {
            ShapeDrawable line = new ShapeDrawable(new PathShape(generateLine(mCellWidth * i, 0, mCellWidth * i, mDisplaySize.y - actionBarHeight), mDisplaySize.x, mDisplaySize.y - actionBarHeight));
            line.setBounds(0, 0, mDisplaySize.x, (int) (mDisplaySize.y - actionBarHeight));
            line.getPaint().setColor(Color.LTGRAY);
            line.getPaint().setStyle(Paint.Style.STROKE);
            backgroundElements[i] = line;
        }
        for (int i = 1; i < mRows; i++)
        {
            ShapeDrawable line = new ShapeDrawable(new PathShape(generateLine(0, mCellHeight * i, mDisplaySize.x, mCellHeight * i), mDisplaySize.x, mDisplaySize.y - actionBarHeight));
            line.setBounds(0, 0, mDisplaySize.x, (int) (mDisplaySize.y - actionBarHeight));
            line.getPaint().setColor(Color.LTGRAY);
            line.getPaint().setStyle(Paint.Style.STROKE);
            backgroundElements[i + mColumns - 1] = line;
        }

        LayerDrawable background = new LayerDrawable(backgroundElements);

        mRootLayout = new RelativeLayout(this);
        mRootLayout.setBackground(background);

        setContentView(mRootLayout);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu, menu);

        SubMenu widgetsSubMenu = menu.findItem(R.id.add_widget).getSubMenu();
        for(String widgetName : WidgetParser.TYPES.keySet())
            widgetsSubMenu.add(R.id.add_widget_menu, Menu.NONE, Menu.NONE, widgetName);



        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getGroupId())
        {
            case R.id.add_widget_menu:
                addWidget((String) item.getTitle());
                return true;
            case R.id.load_layout_menu:
                loadLayout((String) item.getTitle());
                return true;
        }
        switch (item.getItemId())
        {
            case R.id.edit_mode:
                mEditMode = !mEditMode;
                item.setTitle(mEditMode ? R.string.edit_mode_on : R.string.edit_mode_off);
                for (Widget widget : mWidgets)
                    widget.setEditMode(mEditMode);
                return true;
            case R.id.toggle_simulation:
                mSimulationOn = !mSimulationOn;
                if(mSimulationOn)
                {
                    item.setIcon(R.drawable.ic_action_pause);
                    mSimulationThread = new SimulationThread(mLog);
                }
                else
                {
                    item.setIcon(R.drawable.ic_action_play);
                    mSimulationThread.finish();
                }
                return true;
            case R.id.save_layout:
                showSaveDialog();
                return true;
            case R.id.load_layout:
                SubMenu layoutsSubMenu = item.getSubMenu();
                layoutsSubMenu.clear();
                for(String layoutName : mLayouts.getAll().keySet())
                    layoutsSubMenu.add(R.id.load_layout_menu, Menu.NONE, Menu.NONE, layoutName);
                return true;
            case R.id.remove_layout:
                showRemoveDialog();
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

    private void addWidget(String widgetName)
    {
        try
        {
            Widget widget = WidgetParser.parseFromName(this, widgetName);
            if (!findPlacement(widget))
            {
                Toast.makeText(this, "No room for new widget", Toast.LENGTH_SHORT).show();
                return;
            }
            widget.setPlacement();
            widget.setEditMode(mEditMode);
            mRootLayout.addView(widget);
            mWidgets.add(widget);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
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
        if (checkingWidget.mPlacement.height() == 0 && checkingWidget.mPlacement.width() == 0)
        {
            mRootLayout.removeView(checkingWidget);
            mWidgets.remove(checkingWidget);
            return true;
        }
        if (checkingWidget.mPlacement.height() == 0 || checkingWidget.mPlacement.width() == 0)
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

    private void showSaveDialog()
    {
        SaveLayoutDialogFragment saveDialog = new SaveLayoutDialogFragment();
        saveDialog.show(getFragmentManager(), "saveDialog");
    }

    private void showRemoveDialog()
    {
        RemoveLayoutDialogFragment removeDialog = new RemoveLayoutDialogFragment();
        removeDialog.show(getFragmentManager(), "removeDialog");
    }

    public boolean saveLayout(String name)
    {
        if(mLayouts.getAll().containsKey(name))
            return false;
        Set<String> layout = new HashSet<String>(mWidgets.size());
        for(Widget widget : mWidgets)
        {
            layout.add(widget.save());
        }
        SharedPreferences.Editor editor = mLayouts.edit();
        editor.putStringSet(name, layout);
        editor.commit();
        return true;
    }

    private void loadLayout(String name)
    {
        mRootLayout.removeAllViews();
        mWidgets.clear();
        for(String widgetSettings : (Set<String>) mLayouts.getAll().get(name))
        {
            try
            {
                Widget widget = WidgetParser.parseJSON(this, widgetSettings);
                widget.setPlacement();
                widget.setEditMode(mEditMode);
                mRootLayout.addView(widget);
                mWidgets.add(widget);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
}

