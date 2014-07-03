package com.technoworks.fusionmonitor;

import android.graphics.Canvas;
import android.graphics.Color;

import java.util.ArrayList;

/**
 * Created by Всеволод on 01.07.2014.
 */
public class DrawingThread extends Thread
{
    ArrayList<Widget> mWidgets;

    public DrawingThread(ArrayList<Widget> widgets)
    {
        mWidgets = widgets;
        start();
    }

    public void run()
    {
        while(true)
        {
            for (int i = 0; i < mWidgets.size(); i++)
            {
                try
                {
                    Canvas canvas = mWidgets.get(i).mSurfaceHolder.lockCanvas();
                    mWidgets.get(i).draw(canvas);
                    mWidgets.get(i).mSurfaceHolder.unlockCanvasAndPost(canvas);
                }
                catch(NullPointerException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }
}
