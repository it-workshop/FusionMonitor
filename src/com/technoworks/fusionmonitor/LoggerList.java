package com.technoworks.fusionmonitor;


import java.util.ArrayList;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 * Created by Всеволод on 18.06.2014.
 */
public class LoggerList<V> extends LinkedList<Timestamped<V>>
{
    private int position = 0;

    public void put(V data)
    {
        offer(new Timestamped<V>(data));
    }

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

        ListIterator<Timestamped<V>> iterator = listIterator();
        long span = 0;
        position = 0;

        Timestamped<V> current = iterator.next();
        while(span < millis)
        {
            span += current.getTimestamp();
            current = iterator.next();
            position++;
            span -= current.getTimestamp();
        }
    }

    public V getLastOne()
    {
        return get(position).getData();
    }

    public ArrayList<V> getLastN(int n)
    {
        ArrayList<V> result = new ArrayList<V>(n);
        ListIterator<Timestamped<V>> iterator = listIterator(position);

        for(int i = 0; i < n; i++)
        {
            try
            {
                result.add(iterator.next().getData());
            }
            catch (NoSuchElementException e)
            {
                break;
            }
        }
        return result;
    }

    public ArrayList<V> getLastMillis(long millis)
    {
        ArrayList<V> result = new ArrayList<V>();
        ListIterator<Timestamped<V>> iterator = listIterator(position);
        long span = 0;

        try
        {
            Timestamped<V> current = iterator.next();
            result.add(current.getData());
            while (span < millis)
            {
                span += current.getTimestamp();
                current = iterator.next();
                result.add(current.getData());
                span -= current.getTimestamp();
            }
        }
        catch (NoSuchElementException e)
        {}
        return result;
    }
}
