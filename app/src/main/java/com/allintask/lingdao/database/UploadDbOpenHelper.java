package com.allintask.lingdao.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by TanJiaJun on 2018/3/15.
 */

public class UploadDbOpenHelper extends SQLiteOpenHelper {

    public UploadDbOpenHelper(Context context) {
        super(context, "upload.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE uploadLog (_id integer primary key autoincrement, uploadFilePath varchar(100), sourceId varchar(10))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS uploadLog");
        onCreate(db);
    }

}
