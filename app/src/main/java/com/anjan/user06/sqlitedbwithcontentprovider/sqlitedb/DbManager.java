package com.anjan.user06.sqlitedbwithcontentprovider.sqlitedb;

/**
 * Created by Anjan Debnath on 5/25/2018.
 * Copyright (c) 2018, W3 Engineers Ltd. All rights reserved.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.anjan.user06.sqlitedbwithcontentprovider.model.Students;

import java.util.ArrayList;
import java.util.List;

/**
 * DatabaseHelper is initialized and the CRUD Operations are defined
 */
public class DbManager {

    private DatabaseHelper dbHelper;

    private Context context;

    private SQLiteDatabase database;

    public DbManager(Context c) {
        context = c;
    }

    public DbManager open() throws SQLException {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    /**
     * Raw Query for direct insert to SQLite
     * @param name
     * @param dept
     * @param regId
     */
    public void insert(String name, String dept, String regId) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DatabaseHelper.NAME, name);
        contentValue.put(DatabaseHelper.DEPT, dept);
        contentValue.put(DatabaseHelper.REG_ID, regId);
        database.insert(DatabaseHelper.TABLE_STUDENTS, null, contentValue);
    }


    /**
     * insert via ContentProvider
     * @param contentValues
     */
    public long insertThroughProvider(ContentValues contentValues, SQLiteDatabase db){
        long row = db.insert(DatabaseHelper.TABLE_STUDENTS, "", contentValues);
      return row;
    }


    /**
     * Raw Query for direct fetch from SQLite
     * @return
     */
    public List<Students> fetchStudents() {

        List<Students> studentsList = new ArrayList<>();

        String[] columns = new String[] { DatabaseHelper._ID, DatabaseHelper.NAME, DatabaseHelper.DEPT, DatabaseHelper.REG_ID };
        Cursor cursor = database.query(DatabaseHelper.TABLE_STUDENTS, columns, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                String name = cursor.getString(cursor.getColumnIndex(DatabaseHelper.NAME));
                String dept = cursor.getString(cursor.getColumnIndex(DatabaseHelper.DEPT));
                String regId = cursor.getString(cursor.getColumnIndex(DatabaseHelper.REG_ID));

                Students students = new Students();
                students.setName(name);
                students.setDept(dept);
                students.setRegId(regId);

                studentsList.add(students);

                cursor.moveToNext();
            }
            cursor.close();

        }
        return studentsList;
    }

    /**
     * fetch data via content provider
     * @param queryBuilder
     * @param db
     * @param projection
     * @param selection
     * @param selectionArgs
     * @param sortOrder
     * @return
     */
    public Cursor fetchStudentsThroughProvider(SQLiteQueryBuilder queryBuilder, SQLiteDatabase db, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        Cursor cursor = queryBuilder.query(db,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder);
        return cursor;
    }


    /**
     * update data via content provider
     * @return
     */
    public int update(SQLiteDatabase db, ContentValues contentValues, String whereClause, String[] whereArgs) {
        int i = db.update(DatabaseHelper.TABLE_STUDENTS, contentValues, whereClause, whereArgs);
        return i;
    }

    /**
     * delete record via content provider
     * @param _id
     */
    public void delete(long _id) {
        database.delete(DatabaseHelper.TABLE_STUDENTS, DatabaseHelper._ID + "=" + _id, null);
    }
}
