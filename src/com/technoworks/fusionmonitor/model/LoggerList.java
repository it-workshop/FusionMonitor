package com.technoworks.fusionmonitor.model;


import com.technoworks.fusionmonitor.Messaging;
import com.technoworks.fusionmonitor.view.drawing.DrawTaskManager;
import com.technoworks.fusionmonitor.view.widgets.Widget;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.NoSuchElementException;

import static com.technoworks.fusionmonitor.Messaging.Telemetry;

/**
 * Created by Vsevolod on 18.06.2014.
 */
public class LoggerList extends LinkedList<Telemetry>
{
    private int position;
    private Telemetry mNullTelemetry;
    private DrawTaskManager mDrawTaskManager;

    public LoggerList(ArrayList<Widget> widgets)
    {
        mDrawTaskManager = new DrawTaskManager(widgets);

        position = 0;
        Messaging.Vector vector = Messaging.Vector.newBuilder()
                .setX(0)
                .setY(0)
                .setZ(0)
                .build();
        Messaging.PIDValues pidValues = Messaging.PIDValues.newBuilder()
                .setP(0)
                .setI(0)
                .setD(0)
                .build();
        mNullTelemetry = Telemetry.newBuilder()
                .setTimestamp(0)
                .setTorques(vector)
                .setAngularVelocity(vector)
                .setPidX(pidValues)
                .setPidY(pidValues)
                .setPidZ(pidValues)
                .setHeading(0)
                .setControlHeading(0)
                .setForce(0)
                .setCorrectionX(0)
                .setCorrectionY(0)
                .build();
    }

    public long getLogTimeLength()
    {
        return getFirst().getTimestamp() - getLast().getTimestamp();
    }

    public void put(Telemetry telemetry)
    {
        push(telemetry);
        mDrawTaskManager.draw();
    }

    public void setPosition(long millis)
    {
        if(millis > getLogTimeLength())
        {
            position = size();
            return;
        }

        ListIterator<Telemetry> iterator = listIterator();
        long span = 0;
        position = 0;

        Telemetry current = iterator.next();
        while(span < millis)
        {
            span += current.getTimestamp();
            current = iterator.next();
            position++;
            span -= current.getTimestamp();
        }
    }

    public Telemetry getLastOne()
    {
        try
        {
            return get(position);
        }
        catch (IndexOutOfBoundsException e)
        {
            return mNullTelemetry;
        }
    }

    public ArrayList<Telemetry> getLastN(int n)
    {
        ArrayList<Telemetry> result = new ArrayList<Telemetry>(n);
        ListIterator<Telemetry> iterator = listIterator(position);

        for(int i = 0; i < n; i++)
        {
            try
            {
                result.add(iterator.next());
            }
            catch (NoSuchElementException e)
            {
                break;
            }
        }
        return result;
    }

    public ArrayList<Telemetry> getLastMillis(long millis)
    {
        ArrayList<Telemetry> result = new ArrayList<Telemetry>();
        ListIterator<Telemetry> iterator = listIterator(position);
        long span = 0;

        try
        {
            Telemetry current = iterator.next();
            result.add(current);
            while (span < millis)
            {
                span += current.getTimestamp();
                current = iterator.next();
                result.add(current);
                span -= current.getTimestamp();
            }
        }
        catch (NoSuchElementException e)
        {}
        return result;
    }
}
