package com.cleverzone.zhizhi.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.cleverzone.zhizhi.bean.ProductBean;

import java.util.ArrayList;
import java.util.LinkedHashMap;

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
                    "pr date, shelf_life_month int, shelf_life_day, ex date, count int, position text, advance int, main_classify text, " +
                    "sub_classify text, backup text, hint_date text)");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }

    public void saveProduct(ProductBean bean) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", bean.name);
        contentValues.put("pic_path", bean.picPath);
        contentValues.put("pr", bean.prDate);
        contentValues.put("shelf_life_month", bean.shelfLifMonth);
        contentValues.put("shelf_life_day", bean.shelfLifeDay);
        contentValues.put("ex", bean.exDate);
        contentValues.put("count", bean.count);
        contentValues.put("position", bean.position);
        contentValues.put("advance", bean.advance);
        contentValues.put("main_classify", bean.mainClassify);
        contentValues.put("sub_classify", bean.subClassify);
        contentValues.put("backup", bean.backup);
        contentValues.put("hint_date", bean.hintDate);
        db.insert("record", null, contentValues);
    }

    public int getProductInfoCount(String mainClassify) {
        String sql = "select count(_id) from record where main_classify = ?";
        Cursor cursor = db.rawQuery(sql, new String[]{mainClassify});
        cursor.moveToNext();
        int count = cursor.getInt(0);
        cursor.close();
        return count;
    }

    public LinkedHashMap<String, ArrayList<ProductBean>> getAllProductInfoByMainClassify(String mainClassify) {
        ArrayList<String> subClassifyList = getAllSubClassifyByMainClassify(mainClassify);
        LinkedHashMap<String, ArrayList<ProductBean>> allInfoMap = new LinkedHashMap<>();
        for (String sub : subClassifyList) {
            String allInfoSql = "select * from record where main_classify = ? and sub_classify= ?";
            Cursor allInfoCursor = db.rawQuery(allInfoSql, new String[]{mainClassify, sub});
            while (allInfoCursor.moveToNext()) {
                if (sub.isEmpty()) {
                    sub = "未分类";
                }
                ProductBean bean = getAllProductInfoBean(allInfoCursor);
                if (allInfoMap.containsKey(sub)) {
                    allInfoMap.get(sub).add(bean);
                } else {
                    ArrayList<ProductBean> arrayList = new ArrayList<>();
                    arrayList.add(bean);
                    allInfoMap.put(sub, arrayList);
                }
            }
            allInfoCursor.close();
        }
        Log.e(TAG, "result=" + allInfoMap);
        return allInfoMap;
    }

    private ProductBean getAllProductInfoBean(Cursor allInfoCursor) {
        ProductBean bean = new ProductBean();
        bean.name = allInfoCursor.getString(allInfoCursor.getColumnIndex("name"));
        bean.picPath = allInfoCursor.getString(allInfoCursor.getColumnIndex("pic_path"));
        bean.prDate = allInfoCursor.getString(allInfoCursor.getColumnIndex("pr"));
        bean.shelfLifMonth = allInfoCursor.getInt(allInfoCursor.getColumnIndex("shelf_life_month"));
        bean.shelfLifeDay = allInfoCursor.getInt(allInfoCursor.getColumnIndex("shelf_life_day"));
        bean.exDate = allInfoCursor.getString(allInfoCursor.getColumnIndex("ex"));
        bean.count = allInfoCursor.getInt(allInfoCursor.getColumnIndex("count"));
        bean.position = allInfoCursor.getString(allInfoCursor.getColumnIndex("position"));
        bean.advance = allInfoCursor.getInt(allInfoCursor.getColumnIndex("advance"));
        bean.mainClassify = allInfoCursor.getString(allInfoCursor.getColumnIndex("main_classify"));
        bean.subClassify = allInfoCursor.getString(allInfoCursor.getColumnIndex("sub_classify"));
        bean.backup = allInfoCursor.getString(allInfoCursor.getColumnIndex("backup"));
        bean.hintDate = allInfoCursor.getString(allInfoCursor.getColumnIndex("hint_date"));
        return bean;
    }

    public ArrayList<String> getAllRecentHintDate() {
        String sql = "select hint_date from record";
        Cursor cursor = db.rawQuery(sql, new String[]{});
        ArrayList<String> hintDateList = new ArrayList<>();
        while (cursor.moveToNext()) {
            hintDateList.add(cursor.getString(0));
        }
        cursor.close();
        return hintDateList;
    }

    public String getRecentHintDateByMainClassify(String mainClassify) {
        String sql = "select min(hint_date) from record where main_classify = ?";
        Cursor cursor = db.rawQuery(sql, new String[]{mainClassify});
        cursor.moveToFirst();
        String hintDate = cursor.getString(0);
        cursor.close();
        return hintDate;
    }

    public String getRecentHintDateByMainAndSubClassify(String mainClassify, String subClassify) {
        if (subClassify.equals("未分类")) {
            subClassify = "";
        }
        String sql = "select min(hint_date) from record where main_classify = ? and sub_classify = ?";
        Cursor cursor = db.rawQuery(sql, new String[]{mainClassify, subClassify});
        cursor.moveToFirst();
        String hintDate = cursor.getString(0);
        cursor.close();
        return hintDate;
    }

    private ArrayList<String> getAllSubClassifyByMainClassify(String mainClassify) {
        String subClassifySql = "select distinct sub_classify from record where main_classify = ? order by sub_classify desc";
        Cursor subClassifyCursor = db.rawQuery(subClassifySql, new String[]{mainClassify});
        ArrayList<String> subClassifyList = new ArrayList<>();
        while (subClassifyCursor.moveToNext()) {
            subClassifyList.add(subClassifyCursor.getString(0));
        }
        subClassifyCursor.close();
        return subClassifyList;
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


}
