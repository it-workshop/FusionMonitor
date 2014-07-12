package com.technoworks.fusionmonitor;

import android.content.Context;
import org.json.JSONObject;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Всеволод on 11.07.2014.
 */
public class WidgetParser
{
    public static final String TYPE_DEFAULT = "Default";
    public static Map<String, Constructor<Widget>> TYPES;

    public static void init()
    {
        TYPES = new HashMap<String, Constructor<Widget>>();
        try
        {
            TYPES.put(Widget.getType(), Widget.class.getConstructor(Context.class));
        }
        catch (NoSuchMethodException e)
        {
            e.printStackTrace();
        }
    }

    public static Widget parseJSON(Context context, String settings) throws Exception
    {
        Widget widget;
        JSONObject jsonSettings = new JSONObject(settings);
        widget = parseFromName(context, jsonSettings.getString("type"));
        widget.mPlacement.set(
                jsonSettings.getInt("left"),
                jsonSettings.getInt("top"),
                jsonSettings.getInt("right"),
                jsonSettings.getInt("bottom"));
        widget.applySettings(jsonSettings.getJSONObject("settings"));
        return widget;
    }

    public static Widget parseFromName(Context context, String name) throws Exception
    {
        if(!TYPES.containsKey(name))
            throw new Exception("No such widget type");
        return TYPES.get(name).newInstance(context);
    }
}
