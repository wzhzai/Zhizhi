package com.cleverzone.zhizhi.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by WANGZHENGZE on 2015/4/16.
 * SharedPreferences������
 */
public class SharedPreferencesUtil {

    private final static String APP_NAME = "clever_zone";

    /**
     * �õ�SharedPreferencesEditor
     * @param context context
     * @return editor
     */
    private static SharedPreferences.Editor getAppSharedPreferencesEditor(Context context) {
        return context.getSharedPreferences(APP_NAME, 0).edit();
    }

    /**
     * �õ�SharedPreferences
     * @param context context
     * @return SharedPreferences
     */
    private static SharedPreferences getAppSharedPreferences(Context context) {
        return context.getSharedPreferences(APP_NAME, 0);
    }

    /**
     * SharedPreferences��String
     * @param context context
     * @param columnName columnName
     * @param value value
     */
    public static void saveString(Context context, String columnName, String value) {
        try {
            SharedPreferences.Editor editor = getAppSharedPreferencesEditor(context);
            editor.putString(columnName, value);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * SharedPreferences��int
     * @param context context
     * @param columnName columnName
     * @param value value
     */
    public static void saveInteger(Context context, String columnName, int value) {
        try {
            SharedPreferences.Editor editor = getAppSharedPreferencesEditor(context);
            editor.putInt(columnName, value);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * SharedPreferences�Ѵ��ڵ�int���и���
     * @param context context
     * @param columnName columnName
     * @param value value
     */
    public static void addInteger(Context context, String columnName, int value) {
        int t = getInteger(context, columnName, 0);
        t = t + value;
        saveInteger(context, columnName, t);
    }

    /**
     * SharedPreferences��long
     * @param context context
     * @param columnName columnName
     * @param value value
     */
    public static void saveLong(Context context, String columnName, long value) {
        try {
            SharedPreferences.Editor editor = getAppSharedPreferencesEditor(context);
            editor.putLong(columnName, value);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * ɾ��SharedPreferences
     * @param context context
     * @param columnName columnName
     */
    public static void removeString(Context context, String columnName) {
        try {
            SharedPreferences.Editor editor = getAppSharedPreferencesEditor(context);
            editor.remove(columnName);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * ��ȡSharedPreferences��String
     * @param context context
     * @param columnName columnName
     * @param defValue defValue
     * @return string
     */
    public static String getString(Context context, String columnName, String defValue) {
        try {
            SharedPreferences sharedPreferences = getAppSharedPreferences(context);
            return sharedPreferences.getString(columnName, defValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return defValue;
    }

    /**
     * ��ȡSharedPreferences��int
     * @param context context
     * @param columnName columnName
     * @param defValue defValue
     * @return int
     */
    public static int getInteger(Context context, String columnName, int defValue) {
        try {
            SharedPreferences sharedPreferences = getAppSharedPreferences(context);
            return sharedPreferences.getInt(columnName, defValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return defValue;
    }

    /**
     * ��ȡSharedPreferences��long
     * @param context context
     * @param columnName columnName
     * @param defValue defValue
     * @return long
     */
    public static Long getLong(Context context, String columnName, long defValue) {
        try {
            SharedPreferences sharedPreferences = getAppSharedPreferences(context);
            return sharedPreferences.getLong(columnName, defValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return defValue;
    }

    /**
     * �Ƴ�SharedPreferences�е�key
     * @param context context
     * @param columnName columnName
     */
    public static void removeKey(Context context, String columnName) {
        try {
            SharedPreferences.Editor sharedPreferencesEditor = getAppSharedPreferencesEditor(context);
            sharedPreferencesEditor.remove(columnName);
            sharedPreferencesEditor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
