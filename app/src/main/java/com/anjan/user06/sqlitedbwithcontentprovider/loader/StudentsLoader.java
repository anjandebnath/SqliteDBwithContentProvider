package com.anjan.user06.sqlitedbwithcontentprovider.loader;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;

import com.anjan.user06.sqlitedbwithcontentprovider.model.Students;
import com.anjan.user06.sqlitedbwithcontentprovider.sqlitedb.DbManager;

import java.util.ArrayList;

/**
 * Created by Anjan Debnath on 6/12/2018.
 * Copyright (c) 2018, W3 Engineers Ltd. All rights reserved.
 */
public class StudentsLoader extends AsyncTaskLoader<ArrayList<Students>> {

    private ArrayList<Students> students;
    private DbManager dbManager;
    private Context mContext;

    public StudentsLoader(@NonNull Context context) {
        super(context);
        mContext = context;
        dbManager = new DbManager(mContext);
        dbManager.open();
        students = new ArrayList<>();
    }

    @Nullable
    @Override
    public ArrayList<Students> loadInBackground() {
        students = (ArrayList<Students>) dbManager.fetchStudents();
        return students;
    }

    @Override
    public void deliverResult(ArrayList<Students> data) {
        if (isReset()) {
            // The Loader has been reset; ignore the result and invalidate the data.
            return;
        }

        // Hold a reference to the old data so it doesn't get garbage collected.
        // We must protect it until the new data has been delivered.
        ArrayList<Students> oldData = students;
        students = data;

        if (isStarted()) {
            // If the Loader is in a started state, deliver the results to the
            // client. The superclass method does this for us.
            super.deliverResult(data);
        }

    }
}
