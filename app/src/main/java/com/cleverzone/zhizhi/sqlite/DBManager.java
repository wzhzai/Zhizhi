package com.cleverzone.zhizhi.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.cleverzone.zhizhi.bean.NewProductBean;
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


}
