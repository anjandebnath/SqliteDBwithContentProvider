package com.anjan.user06.sqlitedbwithcontentprovider.sqlitedb;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Anjan Debnath on 5/25/2018.
 * Copyright (c) 2018, W3 Engineers Ltd. All rights reserved.
 */

/**
 * This class is a subclass of SQLiteOpenHelper
 */
public class DatabaseHelper extends SQLiteOpenHelper{

    // Table Name
    public static final String TABLE_STUDENTS = "STUDENTS";

    // Table columns
    public static final String _ID = "_id";
    public static final String NAME = "name";
    public static final String DEPT = "dept";
    public static final String REG_ID = "reg_id";

    // Database Information
    private static final String DB_NAME = "ajDb.nosql";

    // database version
    private static final int DB_VERSION = 1;


    //create query
    private static final String CREATE_STUDENT_TABLE = "CREATE TABLE IF NOT EXISTS "
            + TABLE_STUDENTS + "("
            + _ID + " integer primary key, "
            + NAME + " text not null, "
            + DEPT + " text not null, "
            + REG_ID + " text not null"
            + ")";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_STUDENT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STUDENTS);
        onCreate(db);
    }
}
