package com.technoworks.fusionmonitor;


import java.util.ArrayList;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.NoSuchElementException;

import static com.technoworks.fusionmonitor.Messaging.Telemetry;

/**
 * Created by Всеволод on 18.06.2014.
 */
public class LoggerList extends LinkedList<Telemetry>
{
    private int position = 0;

    public long getLogTimeLength()
    {
        return getFirst().getTimestamp() - getLast().getTimestamp();
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
        return get(position);
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
