package com.sqlite.tutorial.sqlite;

import com.sqlite.tutorial.constants.AppConstants;

public class SQLiteDatabaseConstants {

    public static final String NOT_NULL                         = " NOT NULL";
    public static final String TEXT_TYPE                        = " TEXT";
    public static final String VARCHAR_TYPE                     = " VARCHAR";
    public static final String INTEGER_TYPE                     = " INTEGER";
    public static final String DOUBLE_TYPE                      = " DOUBLE";
    public static final String LONG_TYPE                        = " LONG";
    public static final String BLOB_TYPE                        = " BLOB";

    public static final String INTEGER_PRIMARY_KEY              = INTEGER_TYPE+" PRIMARY KEY, ";
    public static final String INTEGER_PRIMARY_KEY_AUTOINCREMENT= INTEGER_TYPE+" PRIMARY KEY AUTOINCREMENT NOT NULL, ";
    public static final String DROP_TABLE_IF_EXISTS             = "DROP TABLE IF EXISTS ";
    public static final String CREATE_TABLE_IF_NOT_EXISTS       = "CREATE TABLE IF NOT EXISTS ";
    public static final String INSERT_INTO                      = "INSERT INTO ";
    public static final String SELECT_FROM                      = "SELECT * FROM ";
    public static final String ALTER_TABLE                      = "ALTER TABLE ";
    public static final String DELETE_FROM                      = "DELETE FROM ";
    public static final String WHERE                            = " WHERE ";
    public static final String AND                              = " AND ";
    public static final String OR                               = " AND ";
    public static final String SET                              = " SET ";
    public static final String ADD                              = " ADD ";

    /**
     * File extensions :
     *
     * .db, .sqlite, .sqlite3
     * (".db" does not uniquely identify SQLite database files. Other extensions are commonly used.)
     *
     * Mime type :
     *
     * "application/x-sqlite3 "; @Deprecated
     * "application/vnd.sqlite3";
     */
    public static final String SQLite_DATABASE_NAME             = AppConstants.APP_NAME + "LocalDB";
    public static final int DATABASE_VERSION                    = 1;

    /*
     ***********************************************************************************************
     *************************************** Here define table one *********************************
     ***********************************************************************************************
     */
    public static final String TABLE_1                          = "student";
    public static final String DROP_TABLE_1                     = DROP_TABLE_IF_EXISTS + TABLE_1;

    public static final String TABLE_1_COLUMN_1                 = "_id";
    public static final String TABLE_1_COLUMN_2                 = "firstName";
    public static final String TABLE_1_COLUMN_3                 = "lastName";
    public static final String TABLE_1_COLUMN_4                 = "rollNumber";
    public static final String TABLE_1_COLUMN_5                 = "picture";

    public static final String[] TABLE_1_COLUMNS = {TABLE_1_COLUMN_1, TABLE_1_COLUMN_2, TABLE_1_COLUMN_3, TABLE_1_COLUMN_4, TABLE_1_COLUMN_5};

    public static final String CREATE_TABLE_1 = CREATE_TABLE_IF_NOT_EXISTS + TABLE_1 + " ("+
            TABLE_1_COLUMN_1 + INTEGER_PRIMARY_KEY_AUTOINCREMENT+
            TABLE_1_COLUMN_2 + TEXT_TYPE +", "+
            TABLE_1_COLUMN_3 + TEXT_TYPE +", "+
            TABLE_1_COLUMN_4 + TEXT_TYPE +", "+
            TABLE_1_COLUMN_5 + BLOB_TYPE +
            ")";
    /*
     ***********************************************************************************************
     *************************************** Here define table two *********************************
     ***********************************************************************************************
     */
    public static final String TABLE_2                          = "user";
    public static final String DROP_TABLE_2                     = DROP_TABLE_IF_EXISTS + TABLE_2;

    public static final String TABLE_2_COLUMN_1                 = "_id";
    public static final String TABLE_2_COLUMN_2                 = "username";
    public static final String TABLE_2_COLUMN_3                 = "email";
    public static final String TABLE_2_COLUMN_4                 = "password";

    public static final String[] TABLE_2_COLUMNS = {TABLE_2_COLUMN_1, TABLE_2_COLUMN_2, TABLE_2_COLUMN_3, TABLE_2_COLUMN_4};

    public static final String CREATE_TABLE_2 = CREATE_TABLE_IF_NOT_EXISTS + TABLE_2 + " ("+
            TABLE_2_COLUMN_1 + INTEGER_PRIMARY_KEY_AUTOINCREMENT+
            TABLE_2_COLUMN_2 + TEXT_TYPE +", "+
            TABLE_2_COLUMN_3 + TEXT_TYPE +", "+
            TABLE_2_COLUMN_4 + TEXT_TYPE +
            ")";
}
