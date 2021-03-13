package com.sqlite.tutorial.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.sqlite.tutorial.utilities.LogcatUtils;

public class SQLiteDatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = SQLiteDatabaseHelper.class.getSimpleName();

    public SQLiteDatabaseHelper(Context context) {
        super(context, SQLiteDatabaseConstants.SQLite_DATABASE_NAME, null, SQLiteDatabaseConstants.DATABASE_VERSION);
        LogcatUtils.informationMessage(TAG, "Database created / opened.....");
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        try {
            sqLiteDatabase.execSQL(SQLiteDatabaseConstants.CREATE_TABLE_1);
            sqLiteDatabase.execSQL(SQLiteDatabaseConstants.CREATE_TABLE_2);
            LogcatUtils.informationMessage(TAG, "Table create...");
        } catch (Exception exception) {
            LogcatUtils.informationMessage(TAG, "onCreate(SQLiteDatabase sqLiteDatabase) : " + exception);
        }
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onDowngrade(db, oldVersion, newVersion);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        LogcatUtils.informationMessage(TAG, "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");
        try {
            sqLiteDatabase.execSQL(SQLiteDatabaseConstants.DROP_TABLE_1);
            sqLiteDatabase.execSQL(SQLiteDatabaseConstants.DROP_TABLE_2);
            /* Create tables again */
            onCreate(sqLiteDatabase);
        } catch (Exception exception) {
            LogcatUtils.informationMessage(TAG, "onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) : " + exception);
        }
    }
}

