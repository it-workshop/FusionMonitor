package com.technoworks.fusionmonitor;

import android.graphics.Canvas;
import android.os.AsyncTask;

import java.util.ArrayList;

/**
 * Created by Всеволод on 03.07.2014.
 */
public class Draw extends AsyncTask<Void, Void, Void>
{
    private ArrayList<Widget> mWidgets;
    private DrawTaskManager mManager;

    public Draw(ArrayList<Widget> widgets, DrawTaskManager manager)
    {
        mWidgets = widgets;
        mManager = manager;
    }

    @Override
    protected Void doInBackground(Void... params)
    {
        for (int i = 0; i < mWidgets.size(); i++)
        {
            mWidgets.get(i).postInvalidate();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void result)
    {
        mManager.doneAsyncTask();
    }
}
