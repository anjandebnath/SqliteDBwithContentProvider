package com.anjan.user06.sqlitedbwithcontentprovider;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.anjan.user06.sqlitedbwithcontentprovider.adapter.StudentsAdapter;
import com.anjan.user06.sqlitedbwithcontentprovider.loader.StudentsLoader;
import com.anjan.user06.sqlitedbwithcontentprovider.model.Students;
import com.anjan.user06.sqlitedbwithcontentprovider.sqlitedb.DbManager;

import java.util.ArrayList;

public class AsyncLoaderActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<Students>> {

    private static final int LOADER_ID = 1;
    private StudentsLoader studentsLoader;

    private EditText editName;
    private EditText editDept;
    private EditText editRegId;
    private Button btnAdd;
    private Button btnFetch;

    private DbManager dbManager;


    private RecyclerView recyclerView;
    private StudentsAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_async_loader);

        editName = findViewById(R.id.editName);
        editDept = findViewById(R.id.textDept);
        editRegId = findViewById(R.id.editReg);
        btnAdd = findViewById(R.id.buttonAdd);
        btnFetch = findViewById(R.id.buttonFetch);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        dbManager = new DbManager(this);
        dbManager.open();

        /**
         * insert record to database
         */
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editName.getEditableText().toString();
                String dept = editDept.getEditableText().toString();
                String regId = editRegId.getEditableText().toString();

                dbManager.insert(name, dept, regId);

            }
        });

        /**
         * Fetch all records from database and display
         */
        btnFetch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // This work sequence of this class a nutshell:
                // initLoader -> onCreateLoader -> loadInBackground -> deliverResult -> onLoadFinished
                getSupportLoaderManager().initLoader(LOADER_ID, null, AsyncLoaderActivity.this).forceLoad();
            }
        });


    }

    @NonNull
    @Override
    public Loader<ArrayList<Students>> onCreateLoader(int id, @Nullable Bundle args) {
        studentsLoader = new StudentsLoader(this);
        return studentsLoader;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<ArrayList<Students>> loader, ArrayList<Students> data) {
        Log.e("Loader", "onLoadFinished " + data.size());
        if (data != null && data.size() > 0) {
            initRecyclerView(data);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<ArrayList<Students>> loader) {

    }

    private void initRecyclerView(ArrayList<Students> students){

        mAdapter = new StudentsAdapter(students);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
    }
}
