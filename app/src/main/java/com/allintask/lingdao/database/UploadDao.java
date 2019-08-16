package com.allintask.lingdao.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.File;

/**
 * Created by TanJiaJun on 2018/3/15.
 */

public class UploadDao {

    private UploadDbOpenHelper uploadDbOpenHelper;

    public UploadDao(Context context) {
        uploadDbOpenHelper = new UploadDbOpenHelper(context);
    }

    public void save(String sourceId, File uploadFile) {
        SQLiteDatabase sqLiteDatabase = uploadDbOpenHelper.getWritableDatabase();
        sqLiteDatabase.execSQL("insert into uploadLog(uploadFilePath, sourceId) values(?,?)", new Object[]{uploadFile.getAbsolutePath(), sourceId});
    }

    public void delete(File uploadFile) {
        SQLiteDatabase sqLiteDatabase = uploadDbOpenHelper.getWritableDatabase();
        sqLiteDatabase.execSQL("delete from uploadLog where uploadFilePath=?", new Object[]{uploadFile.getAbsolutePath()});
    }

    public String getSourceId(File uploadFile) {
        SQLiteDatabase sqLiteDatabase = uploadDbOpenHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select sourceId from uploadLog where uploadFilePath=?", new String[]{uploadFile.getAbsolutePath()});

        if (cursor.moveToFirst()) {
            return cursor.getString(0);
        }
        return null;
    }

}
