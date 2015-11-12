package com.cleverzone.zhizhi.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.cleverzone.zhizhi.bean.NewProductBean;
import com.cleverzone.zhizhi.bean.ProductClassifyBean;
import com.cleverzone.zhizhi.util.DateUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by WANGZHENGZE on 2015/4/26.
 */
public class DBManager {

    private static final String TAG = "DBManager";

    // database版本
    private final static int DB_VERSION = 1;
    // database名
    private final static String DB_NAME = "record.db";

    private Context context;

    private static DBManager dbManager;

    private SQLiteDatabase db = null;

    private DataBaseHelper dbHelper = null;

    private DBManager(Context context) {
        this.context = context;
    }

    private static class DataBaseHelper extends SQLiteOpenHelper {

        Context context;
        DataBaseHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
            this.context = context;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("create table record(_id integer primary key, name text, pic_path text, " +
                    "pr_date timestamp not null default (datetime('now','localtime')), " +
                    "shelf_life int, shelf_type int, " +
                    "ex_date timestamp not null default (datetime('now','localtime')), " +
                    "count int, position text, advance int, main_classify text, " +
                    "sub_classify text, backup text, " +
                    "frequency int, " +
                    "hint_date timestamp not null default (datetime('now','localtime')))");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }

    public void saveProduct(NewProductBean bean) {
        ContentValues values = new ContentValues();
        values.put("name", bean.name);
        values.put("pic_path", bean.picPath);
        values.put("pr_date", bean.prDate);
        values.put("shelf_life", bean.shelfLife);
        values.put("shelf_type", bean.shelfLifeType);
        values.put("ex_date", bean.exDate);
        values.put("count", bean.count);
        values.put("position", bean.position);
        values.put("advance", bean.advance);
        values.put("main_classify", bean.mainClassify);
        values.put("sub_classify", bean.subClassify);
        values.put("backup", bean.backup);
        values.put("frequency", bean.frequency);
        values.put("hint_date", bean.hintDate);
        if (bean.id == -1) {
            db.insert("record", null, values);
        } else {
            db.update("record", values, "_id = ?", new String[]{String.valueOf(bean.id)});
        }
    }

    public static synchronized DBManager getInstance(Context context) {
        if (dbManager == null) {
            dbManager = new DBManager(context);
            dbManager.open();
        }
        return dbManager;
    }

    public void open() throws SQLException {
        if (isOpen()) {
            return;
        }
        dbHelper = new DataBaseHelper(context);
        db = dbHelper.getWritableDatabase();
    }

    public void quit() {
        try {
            if (isOpen() && dbHelper != null)
                dbHelper.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isOpen() {
        return db != null && db.isOpen();
    }

    public List<Date> getAllRecentHintDate() {
        List<Date> dateList = new ArrayList<>();
        Cursor cursor = db.query("record", new String[]{"hint_date"}, null, null, null, null, null);
        while (cursor.moveToNext()) {
            dateList.add(DateUtil.toDate(cursor.getInt(0)));
        }
        cursor.close();
        return dateList;
    }

    public int getRecentHintDate() {
        int hintDate = -1;
        Cursor cursor = db.query("record", new String[]{"hint_date"}, "hint_date >= ?", new String[]{String.valueOf(DateUtil.getTenTimestamp())}, null, null, "hint_date");
        cursor.moveToFirst();
        if (cursor.moveToFirst()) {
            hintDate = cursor.getInt(0);
        }
        cursor.close();
        return hintDate;
    }

    public int getRecentHintDateByMainClassify(String mainClassify) {
        int hintDate = -1;
        Cursor cursor = db.query("record", new String[]{"hint_date"}, "main_classify = ? and hint_date >= ?", new String[]{mainClassify, String.valueOf(DateUtil.getTenTimestamp())}, null, null, "hint_date");
        if (cursor.moveToFirst()) {
            hintDate = cursor.getInt(0);
        }
        cursor.close();
        return hintDate;
    }

    public int getRecentHintDateByMainAndSubClassify(String mainClassify, String subClassify) {
        int hintDate = -1;
        Cursor cursor = db.query("record", new String[]{"hint_date"}, "sub_classify = ? and main_classify = ? and hint_date >= ?", new String[]{subClassify, mainClassify, String.valueOf(DateUtil.getTenTimestamp())}, null, null, "hint_date");
        if (cursor.moveToFirst()) {
            hintDate = cursor.getInt(0);
        }
        cursor.close();
        return hintDate;
    }

    public int getProductInfoCount(String mainClassify) {
        int count;
        Cursor cursor = db.query("record", new String[]{"count(name)"}, "main_classify = ? and hint_date >= ?", new String[]{mainClassify, String.valueOf(DateUtil.getTenTimestamp())}, null, null, null);
        cursor.moveToFirst();
        count = cursor.getInt(0);
        cursor.close();
        return count;
    }

    public int getOverdueProductInfoCount() {
        int count;
        Cursor cursor = db.query("record", new String[]{"count(name)"}, "hint_date < ?", new String[]{String.valueOf(DateUtil.getTenTimestamp())}, null, null, null);
        cursor.moveToFirst();
        count = cursor.getInt(0);
        cursor.close();
        return count;
    }

    public Map<String, List<NewProductBean>> getAllProductInfoByMainClassify(String mainClassify) {
        Map<String, List<NewProductBean>> map = new LinkedHashMap<>();
        List<String> subClassifyList = getAllSubClassifyByMainClassify(mainClassify);
        for (String s : subClassifyList) {
            map.put(s, getAllProductInfo(mainClassify, s));
        }
        return map;
    }

    public Map<String, List<NewProductBean>> getAllOverDueInfo() {
        Map<String, List<NewProductBean>> map = new LinkedHashMap<>();
        List<ProductClassifyBean> subClassifyList = getAllOverDueClassifyInfo();
        for (ProductClassifyBean bean : subClassifyList) {
            map.put(bean.mainClassify + "--" + bean.subClassify, getAllOverDueProductInfo(bean.mainClassify, bean.subClassify));
        }
        return map;
    }

    public Map<String, List<NewProductBean>> getAllInfoByDate(int date) {
        Map<String, List<NewProductBean>> map = new LinkedHashMap<>();
        List<ProductClassifyBean> subClassifyList = getAllSubClassifyByDate(date);
        for (ProductClassifyBean bean : subClassifyList) {
            map.put(bean.mainClassify + "--" + bean.subClassify, getAllOverDueProductInfo(bean.mainClassify, bean.subClassify));
        }
        return map;
    }

    public List<NewProductBean> getAllProductInfo(String mainClassify, String subClassify) {
        List<NewProductBean> productBeanList = new ArrayList<>();
        Cursor cursor = db.query("record", new String[]{"*"}, "main_classify = ? and sub_classify = ? and hint_date >= ?", new String[]{mainClassify, subClassify, String.valueOf(DateUtil.getTenTimestamp())}, null, null, null);
        setCursorToBean(productBeanList, cursor);
        cursor.close();
        return productBeanList;
    }

    public List<NewProductBean> getAllOverDueProductInfo(String mainClassify, String subClassify) {
        List<NewProductBean> productBeanList = new ArrayList<>();
        Cursor cursor = db.query("record", new String[]{"*"}, "main_classify = ? and sub_classify = ? and hint_date < ?", new String[]{mainClassify, subClassify, String.valueOf(DateUtil.getTenTimestamp())}, null, null, null);
        setCursorToBean(productBeanList, cursor);
        cursor.close();
        return productBeanList;
    }

    private void setCursorToBean(List<NewProductBean> productBeanList, Cursor cursor) {
        while (cursor.moveToNext()) {
            NewProductBean productBean = new NewProductBean();
            productBean.id = cursor.getInt(cursor.getColumnIndex("_id"));
            productBean.name = cursor.getString(cursor.getColumnIndex("name"));
            productBean.picPath = cursor.getString(cursor.getColumnIndex("pic_path"));
            productBean.prDate = cursor.getInt(cursor.getColumnIndex("pr_date"));
            productBean.shelfLife = cursor.getInt(cursor.getColumnIndex("shelf_life"));
            productBean.shelfLifeType = cursor.getInt(cursor.getColumnIndex("shelf_type"));
            productBean.exDate = cursor.getInt(cursor.getColumnIndex("ex_date"));
            productBean.count = cursor.getInt(cursor.getColumnIndex("count"));
            productBean.position = cursor.getString(cursor.getColumnIndex("position"));
            productBean.advance = cursor.getInt(cursor.getColumnIndex("advance"));
            productBean.mainClassify = cursor.getString(cursor.getColumnIndex("main_classify"));
            productBean.subClassify = cursor.getString(cursor.getColumnIndex("sub_classify"));
            productBean.frequency = cursor.getInt(cursor.getColumnIndex("frequency"));
            productBean.hintDate = cursor.getInt(cursor.getColumnIndex("hint_date"));
            productBeanList.add(productBean);
        }
    }

    public List<String> getAllSubClassifyByMainClassify(String mainClassify) {
        List<String> subClassifyList = new ArrayList<>();
        Cursor cursor = db.query("record", new String[]{"sub_classify"}, "main_classify = ? and hint_date >= ?", new String[]{mainClassify, String.valueOf(DateUtil.getTenTimestamp())}, null, null, null);
        while (cursor.moveToNext()) {
            subClassifyList.add(cursor.getString(0));
        }
        cursor.close();
        return subClassifyList;
    }

    public List<ProductClassifyBean> getAllOverDueClassifyInfo() {
        List<ProductClassifyBean> list = new ArrayList<>();
        Cursor cursor = db.query(true, "record", new String[]{"main_classify, sub_classify"}, "hint_date < ?", new String[]{String.valueOf(DateUtil.getTenTimestamp())}, null, null, null, null);
        while (cursor.moveToNext()) {
            ProductClassifyBean bean = new ProductClassifyBean();
            bean.mainClassify = cursor.getString(cursor.getColumnIndex("main_classify"));
            bean.subClassify = cursor.getString(cursor.getColumnIndex("sub_classify"));
            list.add(bean);
        }
        cursor.close();
        return list;
    }

    public List<ProductClassifyBean> getAllSubClassifyByDate(int date) {
        List<ProductClassifyBean> list = new ArrayList<>();
        Cursor cursor = db.query(true, "record", new String[]{"main_classify, sub_classify"}, "hint_date = ?", new String[]{String.valueOf(date)}, null, null, null, null);
        while (cursor.moveToNext()) {
            ProductClassifyBean bean = new ProductClassifyBean();
            bean.mainClassify = cursor.getString(cursor.getColumnIndex("main_classify"));
            bean.subClassify = cursor.getString(cursor.getColumnIndex("sub_classify"));
            list.add(bean);
        }
        cursor.close();
        return list;
    }

}
