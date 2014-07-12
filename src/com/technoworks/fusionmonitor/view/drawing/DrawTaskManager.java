package com.technoworks.fusionmonitor.view.drawing;

import android.os.AsyncTask;
import com.technoworks.fusionmonitor.view.widgets.Widget;

import java.util.ArrayList;

/**
 * Created by Vsevolod on 03.07.2014.
 */
public class DrawTaskManager
{
    private ArrayList<Widget> mWidgets;
    private boolean mExecute = false;
    private Draw mDrawTask;

    public DrawTaskManager(ArrayList<Widget> widgets)
    {
        mWidgets = widgets;
    }

    public void draw()
    {
        if(mDrawTask==null || !mDrawTask.getStatus().equals(AsyncTask.Status.RUNNING))
            startAsyncTask();
        else
            mExecute = true;
    }

    private void startAsyncTask()
    {
        mDrawTask = new Draw(mWidgets, this);
        mDrawTask.execute();
    }

    protected void doneAsyncTask()
    {
        if(mExecute)
        {
            mExecute = false;
            startAsyncTask();
        }
    }
}
