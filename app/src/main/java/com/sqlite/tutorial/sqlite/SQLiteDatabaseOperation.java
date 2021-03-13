package com.sqlite.tutorial.sqlite;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import com.sqlite.tutorial.sqlite.model.Student;
import java.util.ArrayList;

public class SQLiteDatabaseOperation {

    private static final String TAG = SQLiteDatabaseOperation.class.getSimpleName();

    @SuppressLint("StaticFieldLeak")
    private static SQLiteDatabaseOperation instance;

    public static SQLiteDatabaseOperation getInstance(Context context) {
        if (instance == null) {
            synchronized (SQLiteDatabaseOperation.class) {
                if (instance == null) {
                    instance = new SQLiteDatabaseOperation(context);
                }
            }
        }
        return instance;
    }

    public SQLiteDatabaseHelper sqLiteDatabaseHelper;
    private SQLiteDatabase sqLiteDatabase;

    public SQLiteDatabaseOperation(Context context) {
        this.sqLiteDatabaseHelper = new SQLiteDatabaseHelper(context);
    }

    public void open(){
        sqLiteDatabase = sqLiteDatabaseHelper.getWritableDatabase();
    }

    public void close(){
        sqLiteDatabase.close();
    }

    /*
     ***********************************************************************************************
     ****************************************** INSERT OPERATION ***********************************
     ***********************************************************************************************
     */
    public boolean insertFirstWay(Student object) {
        this.open();

        ContentValues values = new ContentValues();
        values.put(SQLiteDatabaseConstants.TABLE_1_COLUMN_2, object.getFirstName());
        values.put(SQLiteDatabaseConstants.TABLE_1_COLUMN_3, object.getLastName());
        values.put(SQLiteDatabaseConstants.TABLE_1_COLUMN_4, object.getRollNumber());
        values.put(SQLiteDatabaseConstants.TABLE_1_COLUMN_5, object.getPicture());

        long newInsertedRowId = sqLiteDatabase.insert(SQLiteDatabaseConstants.TABLE_1, null, values);

        this.close();

        /* @return the row ID of the newly inserted row, or -1 if an error occurred */
        if(newInsertedRowId == -1)
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    public void insertSecondWay(Student object) {
        this.open();

        String query = "INSERT INTO " + SQLiteDatabaseConstants.TABLE_1 + " ("
                + SQLiteDatabaseConstants.TABLE_1_COLUMN_2 + ", "
                + SQLiteDatabaseConstants.TABLE_1_COLUMN_3 + ", "
                + SQLiteDatabaseConstants.TABLE_1_COLUMN_4 + ", "
                + SQLiteDatabaseConstants.TABLE_1_COLUMN_5 + ") VALUES('"
                + object.getFirstName() + "', '"
                + object.getLastName() + "', '"
                + object.getRollNumber() + "', '"
                + object.getPicture() + "')";

        System.out.println("******************************"+query);
        sqLiteDatabase.execSQL(query);

        this.close();
    }
    /*
     ***********************************************************************************************
     ****************************************** READ OPERATION *************************************
     ***********************************************************************************************
     */
    public int getNumberOfRowsFirstWay(String tableName) {
        this.open();

        long count = DatabaseUtils.queryNumEntries(sqLiteDatabase, tableName);

        this.close();
        return (int) count;
    }

    public int getNumberOfRowsSecondWay(String tableName) {
        this.open();

        String query = "SELECT  * FROM " + tableName;
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);
        int count = cursor.getCount();
        cursor.close();

        this.close();
        return count;
    }

    public ArrayList<Student> getAllRowFirstWay() {
        this.open();

        ArrayList<Student> objectArrayList = new ArrayList<Student>();

        Cursor cursor = sqLiteDatabase.query(SQLiteDatabaseConstants.TABLE_1,
                null,
                null,
                null,
                null,
                null,
                null);

        if (cursor != null && cursor.getCount() > 0)
        {
            cursor.moveToFirst();
            do {
                int idInt                 = cursor.getInt(cursor.getColumnIndex(SQLiteDatabaseConstants.TABLE_1_COLUMN_1));
                String firstNameString    = cursor.getString(cursor.getColumnIndex(SQLiteDatabaseConstants.TABLE_1_COLUMN_2));
                String lastNameString     = cursor.getString(cursor.getColumnIndex(SQLiteDatabaseConstants.TABLE_1_COLUMN_3));
                String rollNumberString   = cursor.getString(cursor.getColumnIndex(SQLiteDatabaseConstants.TABLE_1_COLUMN_4));
                byte[] pictureBlob        = cursor.getBlob(cursor.getColumnIndex(SQLiteDatabaseConstants.TABLE_1_COLUMN_5));

                Student object = new Student(idInt, firstNameString, lastNameString, rollNumberString, pictureBlob);
                objectArrayList.add(object);
            }while (cursor.moveToNext());

            cursor.close();
        }

        this.close();
        return objectArrayList;
    }

    public ArrayList<Student> getAllRowSecondWay() {
        this.open();

        ArrayList<Student> objectArrayList = new ArrayList<Student>();

        String query = "SELECT  * FROM " + SQLiteDatabaseConstants.TABLE_1;
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);

        if (cursor != null && cursor.getCount() > 0)
        {
            cursor.moveToFirst();
            do {
                int idInt                 = cursor.getInt(cursor.getColumnIndex(SQLiteDatabaseConstants.TABLE_1_COLUMN_1));
                String firstNameString    = cursor.getString(cursor.getColumnIndex(SQLiteDatabaseConstants.TABLE_1_COLUMN_2));
                String lastNameString     = cursor.getString(cursor.getColumnIndex(SQLiteDatabaseConstants.TABLE_1_COLUMN_3));
                String rollNumberString   = cursor.getString(cursor.getColumnIndex(SQLiteDatabaseConstants.TABLE_1_COLUMN_4));
                byte[] pictureBlob        = cursor.getBlob(cursor.getColumnIndex(SQLiteDatabaseConstants.TABLE_1_COLUMN_5));

                Student object = new Student(idInt, firstNameString, lastNameString, rollNumberString, pictureBlob);
                objectArrayList.add(object);
            }while (cursor.moveToNext());

            cursor.close();
        }

        this.close();
        return objectArrayList;
    }

    public Student getSingleRowFirstWay(String rollNumber) {
        this.open();

        Student object = null;

        Cursor cursor = sqLiteDatabase.query(SQLiteDatabaseConstants.TABLE_1,
                SQLiteDatabaseConstants.TABLE_1_COLUMNS,
                SQLiteDatabaseConstants.TABLE_1_COLUMN_4+ "=?",
                new String[]{String.valueOf(rollNumber)},
                null,
                null,
                null,
                null);

        if (cursor != null && cursor.getCount() > 0)
        {
            cursor.moveToNext();

            int idInt                 = cursor.getInt(cursor.getColumnIndex(SQLiteDatabaseConstants.TABLE_1_COLUMN_1));
            String firstNameString    = cursor.getString(cursor.getColumnIndex(SQLiteDatabaseConstants.TABLE_1_COLUMN_2));
            String lastNameString     = cursor.getString(cursor.getColumnIndex(SQLiteDatabaseConstants.TABLE_1_COLUMN_3));
            String rollNumberString   = cursor.getString(cursor.getColumnIndex(SQLiteDatabaseConstants.TABLE_1_COLUMN_4));
            byte[] pictureBlob        = cursor.getBlob(cursor.getColumnIndex(SQLiteDatabaseConstants.TABLE_1_COLUMN_5));

            object = new Student(idInt, firstNameString, lastNameString, rollNumberString, pictureBlob);

            cursor.close();
        }

        this.close();
        return object;
    }

    public Student getSingleRowSecondWay(String rollNumber) {
        this.open();

        Student object = null;

        String query = "SELECT * FROM "+SQLiteDatabaseConstants.TABLE_1+" WHERE "+SQLiteDatabaseConstants.TABLE_1_COLUMN_4+" = ? ";
        Cursor cursor = sqLiteDatabase.rawQuery(query, new String[]{String.valueOf(rollNumber)});

        if (cursor != null && cursor.getCount() > 0)
        {
            cursor.moveToNext();

            int idInt                 = cursor.getInt(cursor.getColumnIndex(SQLiteDatabaseConstants.TABLE_1_COLUMN_1));
            String firstNameString    = cursor.getString(cursor.getColumnIndex(SQLiteDatabaseConstants.TABLE_1_COLUMN_2));
            String lastNameString     = cursor.getString(cursor.getColumnIndex(SQLiteDatabaseConstants.TABLE_1_COLUMN_3));
            String rollNumberString   = cursor.getString(cursor.getColumnIndex(SQLiteDatabaseConstants.TABLE_1_COLUMN_4));
            byte[] pictureBlob        = cursor.getBlob(cursor.getColumnIndex(SQLiteDatabaseConstants.TABLE_1_COLUMN_5));

            object = new Student(idInt, firstNameString, lastNameString, rollNumberString, pictureBlob);

            cursor.close();
        }

        this.close();
        return object;
    }

    public Student getTableInfoById(int id) {
        this.open();

        Student object = null;

        Cursor cursor = sqLiteDatabase.query(SQLiteDatabaseConstants.TABLE_1, null,
                SQLiteDatabaseConstants.TABLE_1_COLUMN_1 +" = " + id, null, null, null, null);

        if (cursor != null && cursor.getCount() > 0)
        {
            cursor.moveToNext();

            int idInt                 = cursor.getInt(cursor.getColumnIndex(SQLiteDatabaseConstants.TABLE_1_COLUMN_1));
            String firstNameString    = cursor.getString(cursor.getColumnIndex(SQLiteDatabaseConstants.TABLE_1_COLUMN_2));
            String lastNameString     = cursor.getString(cursor.getColumnIndex(SQLiteDatabaseConstants.TABLE_1_COLUMN_3));
            String rollNumberString   = cursor.getString(cursor.getColumnIndex(SQLiteDatabaseConstants.TABLE_1_COLUMN_4));
            byte[] pictureBlob        = cursor.getBlob(cursor.getColumnIndex(SQLiteDatabaseConstants.TABLE_1_COLUMN_5));

            object = new Student(idInt, firstNameString, lastNameString, rollNumberString, pictureBlob);

            cursor.close();
        }

        this.close();
        return object;
    }
    /*
     ***********************************************************************************************
     ****************************************** UPDATE OPERATION ***********************************
     ***********************************************************************************************
     */
    public boolean updateFirstWay(Student object) {
        this.open();

        ContentValues values = new ContentValues();
        values.put(SQLiteDatabaseConstants.TABLE_1_COLUMN_2, object.getFirstName());
        values.put(SQLiteDatabaseConstants.TABLE_1_COLUMN_3, object.getLastName());
        values.put(SQLiteDatabaseConstants.TABLE_1_COLUMN_5, object.getPicture());

        long affectedRow = sqLiteDatabase.update(
                SQLiteDatabaseConstants.TABLE_1,
                values,
                SQLiteDatabaseConstants.TABLE_1_COLUMN_4 + " = ?",
                new String[]{String.valueOf(object.getRollNumber())});

        /*long affectedRow = sqLiteDatabase.update(
                SQLiteDatabaseConstants.TABLE_1,
                values,
                SQLiteDatabaseConstants.TABLE_1_COLUMN_4 + " = "+object.getRollNumber(),
                null);*/

        this.close();

        /* @return the number of rows affected */
        if(affectedRow > 0)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public void updateSecondWay(Student object) {
        this.open();

        sqLiteDatabase.execSQL("UPDATE " + SQLiteDatabaseConstants.TABLE_1
                + " SET "
                + SQLiteDatabaseConstants.TABLE_1_COLUMN_2 + "='" + object.getFirstName() + "',"
                + SQLiteDatabaseConstants.TABLE_1_COLUMN_3 + "='" + object.getLastName() + "',"
                + SQLiteDatabaseConstants.TABLE_1_COLUMN_5 + "='" + object.getPicture() +
                "' WHERE "
                + SQLiteDatabaseConstants.TABLE_1_COLUMN_4 + "='" + object.getRollNumber()+"'");

        this.close();
    }

    /*
     ***********************************************************************************************
     ****************************************** DELETE OPERATION ***********************************
     ***********************************************************************************************
     */
    public boolean deleteFirstWay(String rollNumber) {
        this.open();

        long affectedRow = sqLiteDatabase.delete(SQLiteDatabaseConstants.TABLE_1,
                SQLiteDatabaseConstants.TABLE_1_COLUMN_4 + " = ?",
                new String[]{String.valueOf(rollNumber)});

        this.close();

        /* @return the number of rows affected */
        if(affectedRow > 0)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public void deleteSecondWay(String rollNumber) {
        this.open();

        sqLiteDatabase.execSQL("DELETE FROM "+ SQLiteDatabaseConstants.TABLE_1
                + " WHERE "
                + SQLiteDatabaseConstants.TABLE_1_COLUMN_4 + "='" +rollNumber+"'");

        this.close();
    }

    public void deleteWithMatchTwoField(int id, String firstName){
        this.open();

        String query = "DELETE FROM " + SQLiteDatabaseConstants.TABLE_1 + " WHERE "
                + SQLiteDatabaseConstants.TABLE_1_COLUMN_1 + " = '" + id + "'" +
                " AND " + SQLiteDatabaseConstants.TABLE_1_COLUMN_2 + " = '" + firstName + "'";
        sqLiteDatabase.execSQL(query);

        this.close();
    }

    public boolean setTableEmptyFirstWay(String tableName) {
        this.open();

        long affectedRow = sqLiteDatabase.delete(tableName,null,null);

        this.close();

        /* @return the number of rows affected */
        if(affectedRow > 0)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public void setTableEmptySecondWay(String tableName) {
        this.open();

        sqLiteDatabase.execSQL("delete from " + tableName);

        this.close();
    }

    /*
     ***********************************************************************************************
     ****************************************** SEARCH OPERATION ***********************************
     ***********************************************************************************************
     */
    public ArrayList<Student> search(String queryText) {
        this.open();

        ArrayList<Student> objectArrayList = new ArrayList<Student>();

        String[] args = new String[1];
        args[0] = "%"+queryText+"%";
        String query ="select * from "+SQLiteDatabaseConstants.TABLE_1+" where "+SQLiteDatabaseConstants.TABLE_1_COLUMN_2+" Like ?";
        Cursor cursor = sqLiteDatabase.rawQuery(query,args);

        if (cursor != null && cursor.getCount() > 0)
        {
            cursor.moveToFirst();
            do {
                int idInt                 = cursor.getInt(cursor.getColumnIndex(SQLiteDatabaseConstants.TABLE_1_COLUMN_1));
                String firstNameString    = cursor.getString(cursor.getColumnIndex(SQLiteDatabaseConstants.TABLE_1_COLUMN_2));
                String lastNameString     = cursor.getString(cursor.getColumnIndex(SQLiteDatabaseConstants.TABLE_1_COLUMN_3));
                String rollNumberString   = cursor.getString(cursor.getColumnIndex(SQLiteDatabaseConstants.TABLE_1_COLUMN_4));
                byte[] pictureBlob        = cursor.getBlob(cursor.getColumnIndex(SQLiteDatabaseConstants.TABLE_1_COLUMN_5));

                Student object = new Student(idInt, firstNameString, lastNameString, rollNumberString, pictureBlob);
                objectArrayList.add(object);
            }while (cursor.moveToNext());

            cursor.close();
        }

        this.close();
        return objectArrayList;
    }

    public boolean isEmailExit(String email) {
        this.open();

        boolean isExit = false;
        try
        {
            String query = "SELECT * FROM " + SQLiteDatabaseConstants.TABLE_2 + " WHERE " + SQLiteDatabaseConstants.TABLE_2_COLUMN_3 + " = '" + email + "'";
            Cursor cursor = sqLiteDatabase.rawQuery(query, null);
            if(cursor != null && cursor.getCount() > 0) {
                isExit = true;
            }
        }
        catch (SQLiteException sQLiteException) {
            sQLiteException.printStackTrace();
        }

        this.close();
        return isExit;
    }

    public int findLastId() {
        this.open();

        int lastId = 0;
        String query = "SELECT * FROM " + SQLiteDatabaseConstants.TABLE_1;
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);
        if(cursor != null && cursor.getCount() > 0) {
            cursor.moveToLast();
            lastId = cursor.getInt(cursor.getColumnIndex(SQLiteDatabaseConstants.TABLE_1_COLUMN_1));
            cursor.close();
        }

        this.close();
        return lastId;
    }
}
