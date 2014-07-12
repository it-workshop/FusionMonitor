package com.technoworks.fusionmonitor.view.widgets;

import android.content.Context;
import com.technoworks.fusionmonitor.view.widgets.TextWidget;
import com.technoworks.fusionmonitor.view.widgets.Widget;
import org.json.JSONObject;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Vsevolod on 11.07.2014.
 */
public class WidgetParser
{
    public static final String TYPE_DEFAULT = "Default";
    public static Map<String, Constructor<?>> TYPES;

    public static void init()
    {
        TYPES = new HashMap<String, Constructor<?>>();
        try
        {
            TYPES.put(Widget.TYPE, Widget.class.getConstructor(Context.class));
			TYPES.put(TextWidget.TYPE, TextWidget.class.getConstructor(Context.class));
			TYPES.put(WidgetGraph2.TYPE, WidgetGraph2.class.getConstructor(Context.class));
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
        widget.loadSettings(jsonSettings.getJSONObject("settings"));
        return widget;
    }

    public static Widget parseFromName(Context context, String name) throws Exception
    {
        if(!TYPES.containsKey(name))
            throw new Exception("No such widget type");
        return (Widget) TYPES.get(name).newInstance(context);
    }
}
