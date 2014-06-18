package com.technoworks.fusionmonitor;

/**
 * Created by Всеволод on 18.06.2014.
 */
public class Timestamped<V>
{
    private long mTimestamp;
    private V mData;

    public Timestamped(V data)
    {
        mTimestamp = System.currentTimeMillis();
        mData = data;
    }

    public long getTimestamp()
    {
        return mTimestamp;
    }

    public V getData()
    {
        return mData;
    }
}
