package com.cleverzone.zhizhi.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.cleverzone.zhizhi.bean.ProductBean;

/**
 * Created by WANGZHENGZE on 2015/4/26.
 */
public class DBManage {
    // database版本
    private final static int DB_VERSION = 1;
    // database名
    private final static String DB_NAME = "record.db";

    private Context context;

    private static DBManage dbManage;

    private SQLiteDatabase db = null;

    private DataBaseHelper dbHelper = null;

    private DBManage(Context context) {
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
                    "sub_classify text, backup text)");
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
        db.insert("record", null, contentValues);
    }

    public static synchronized DBManage getInstance(Context context) {
        if (dbManage == null) {
            dbManage = new DBManage(context);
            dbManage.open();
        }
        return dbManage;
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
