package com.technoworks.fusionmonitor;

import java.util.*;

/**
 * Created by Всеволод on 18.06.2014.
 */
public class LoggerList<V> extends LinkedList<Timestamped<V>>
{
    public void put(V data)
    {
        offer(new Timestamped<V>(data));
    }

    public ArrayList<V> getLastN(int n)
    {
        ArrayList<V> result = new ArrayList<V>(n);
        ListIterator<Timestamped<V>> iterator = listIterator();
        for(int i = 0; i < n; i++)
        {
            result.add(iterator.next().getData());
        }
        return result;
    }

    public ArrayList<V> getLastMillis(long millis)
    {
        ArrayList<V> result = new ArrayList<V>();
        ListIterator<Timestamped<V>> iterator = listIterator();
        long span = 0;
        Timestamped<V> current = iterator.next();
        result.add(current.getData());
        while(span < millis)
        {
            span += current.getTimestamp();
            current = iterator.next();
            result.add(current.getData());
            span -= current.getTimestamp();
        }
        return result;
    }
}
