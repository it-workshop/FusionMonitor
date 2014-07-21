package com.technoworks.fusionmonitor;

import android.content.SharedPreferences;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;

/**
 * Created by loredan on 21.07.14.
 */
public class CopterSettings
{
    private static final String EMPTY_SETTINGS = "v0.0";
    private static final String SETTINGS_VERSION = "v0.1";

    private static SharedPreferences mSettings;

    public static void init(SharedPreferences settings)
    {
        if(!settings.getString("version", EMPTY_SETTINGS).equals(SETTINGS_VERSION))
            initializeSettings(settings);

        mSettings = settings;

        onChangedSettings();
    }

    private static void initializeSettings(SharedPreferences settings)
    {
        SharedPreferences.Editor editor = settings.edit();

        editor.clear();

        editor.putString("version", SETTINGS_VERSION);

        editor.putFloat("Angle offset.x", mSettings==null ? 0 : mSettings.getFloat("Angle offset.x", 0));
        editor.putFloat("Angle offset.y", mSettings==null ? 0 : mSettings.getFloat("Angle offset.y", 0));
        editor.putFloat("Angle offset.z", mSettings==null ? 0 : mSettings.getFloat("Angle offset.z", 0));

        editor.putFloat("PID settings.x.p", mSettings==null ? 0 : mSettings.getFloat("PID settings.x.p", 0));
        editor.putFloat("PID settings.x.i", mSettings==null ? 0 : mSettings.getFloat("PID settings.x.i", 0));
        editor.putFloat("PID settings.x.d", mSettings==null ? 0 : mSettings.getFloat("PID settings.x.d", 0));

        editor.putFloat("PID settings.y.p", mSettings==null ? 0 : mSettings.getFloat("PID settings.y.p", 0));
        editor.putFloat("PID settings.y.i", mSettings==null ? 0 : mSettings.getFloat("PID settings.y.i", 0));
        editor.putFloat("PID settings.y.d", mSettings==null ? 0 : mSettings.getFloat("PID settings.y.d", 0));

        editor.putFloat("PID settings.z.p", mSettings==null ? 0 : mSettings.getFloat("PID settings.z.p", 0));
        editor.putFloat("PID settings.z.i", mSettings==null ? 0 : mSettings.getFloat("PID settings.z.i", 0));
        editor.putFloat("PID settings.z.d", mSettings==null ? 0 : mSettings.getFloat("PID settings.z.d", 0));

        editor.commit();
    }

    public static LinkedHashSet<String> getFields()
    {
        LinkedHashSet<String> fields = (LinkedHashSet<String>) mSettings.getAll().keySet();
        fields.remove("version");
        return fields;
    }

    public static float get(String fieldName)
    {
        return mSettings.getFloat(fieldName, 0);
    }

    public static void set(String fieldName, float fieldValue)
    {
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putFloat(fieldName, fieldValue);
        editor.commit();
        onChangedSettings();
    }

    private static void onChangedSettings()
    {

    }
}
