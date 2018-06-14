package com.anjan.user06.sqlitedbwithcontentprovider.contentprovider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.anjan.user06.sqlitedbwithcontentprovider.sqlitedb.DatabaseHelper;
import com.anjan.user06.sqlitedbwithcontentprovider.sqlitedb.DbManager;

import java.util.HashMap;

import static com.anjan.user06.sqlitedbwithcontentprovider.sqlitedb.DatabaseHelper.NAME;

/**
 * Created by Anjan Debnath on 5/25/2018.
 * Copyright (c) 2018, W3 Engineers Ltd. All rights reserved.
 */
public class StudentProvider extends ContentProvider{

    private SQLiteDatabase database;

    private DatabaseHelper dbHelper;
    private DbManager dbManager;

    // Table columns
    public static final String _ID = "_id";
    public static final String NAME = "name";
    public static final String DEPT = "dept";
    public static final String REG_ID = "reg_id";

    public static final String PROVIDER_NAME = "com.anjan.user06.sqlitedbwithcontentprovider.contentprovider";
    public static final String CONTENT_PATH =  "students";
    /**
     * URI:
     i)- Stands for Universal Resource Identifier.
     ii)- URI is used to identify a ContentProvider uniquely in the current device.
     */
    /**
     * Content://Authority/Path
     where,
     content -> protocol used to communicate with the ContentProvider.
     Authority -> A unique identifier used to identify ContentProvider (generally Authority is the name of the package in which ContentProvider is registered).
     Path -> It is a unique string that appears after authority which is used to identify the table used in an operation.
     */
    public static final String URL = "content://" + PROVIDER_NAME + "/" + CONTENT_PATH;
    public static final Uri CONTENT_URI = Uri.parse(URL);
    static final int STUDENTS = 1;
    static final int STUDENTS_ID = 2;


    static final String SINGLE_RECORD_MIME_TYPE =
            "vnd.android.cursor.item/vnd.com.anjan.user06.sqlitedbwithcontentprovider.contentprovider.students";
    static final String MULTIPLE_RECORDS_MIME_TYPE =
            "vnd.android.cursor.item/vnd.com.anjan.user06.sqlitedbwithcontentprovider.contentprovider.students";

    private static HashMap<String,String> StudentMap;


    //To know the coming URI is of which type, we use URIMatcher class, it matches the patterns.
    private static final UriMatcher uriMatcher = getUriMatcher();
    private static UriMatcher getUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "students", STUDENTS);
        uriMatcher.addURI(PROVIDER_NAME, "students/#", STUDENTS_ID);
        return uriMatcher;
    }


    @Override
    public boolean onCreate() {
        Context context = getContext();
        dbHelper = new DatabaseHelper(context);
        dbManager = new DbManager(context);
        // permissions to be writable
        database = dbHelper.getWritableDatabase();
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(DatabaseHelper.TABLE_STUDENTS);
        switch (uriMatcher.match(uri)) {
            case STUDENTS:
                queryBuilder.setProjectionMap(StudentMap);
                break;
            case STUDENTS_ID:
                queryBuilder.appendWhere( DatabaseHelper._ID + "=" + uri.getLastPathSegment());
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        if (sortOrder == null || sortOrder == ""){
            sortOrder = NAME;
        }
        Cursor cursor = dbManager.fetchStudentsThroughProvider(queryBuilder, database, projection, selection, selectionArgs, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;


    }


    /**
     * For content URIs that point to a row or rows of table data,
     * and that are thus unique to your app, the MIME type should be in Android's vendor-specific MIME format.
     * The general format is:
       type.subtype/provider-specific-part
         Where the parts should be:
         Type part: vnd
         Subtype part:
            If the URI pattern is for a single row: android.cursor.item/
            If the URI pattern is for more than one row: android.cursor.dir/
         Provider-specific part: vnd..
         You supply the and .
         The value should be globally unique. A good choice for is your company's name or some part of your application's Android package name.
         The value should be unique to the corresponding URI pattern. A good choice for the is a string that identifies the table associated with the URI.
     vnd.android.cursor.dir/vnd.com.anjan.user06.sqlitedbwithcontentprovider.contentprovider.students
     * @param uri
     * @return
     */
    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (uriMatcher.match(uri)) {
            case STUDENTS:
                return MULTIPLE_RECORDS_MIME_TYPE;
            case STUDENTS_ID:
                return SINGLE_RECORD_MIME_TYPE;
            default:
                return null;
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        try {
            // insert record in student table and get the row number of recently inserted record
            long id = dbManager.insertThroughProvider(values, database);
            if(id > 0){
                Uri returnUri = ContentUris.withAppendedId(CONTENT_URI, id);
                getContext().getContentResolver().notifyChange(returnUri, null);
                return returnUri;
            }
            throw new SQLException("Fail to add a new record into " + uri);
        } catch(Exception e) {
            return null;
        }
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {

        int count = 0;
        switch (uriMatcher.match(uri)){
            case STUDENTS:
                count = dbManager.delete(database, selection, selectionArgs);
                break;
            case STUDENTS_ID:
                dbManager.delete(database, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;

    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {

        int count = 0;
        switch (uriMatcher.match(uri)){
            case STUDENTS:
                count = dbManager.update(database, values, selection, selectionArgs);
                break;
            case STUDENTS_ID:
                count = dbManager.update(database, values, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI " + uri );
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;

    }
}
