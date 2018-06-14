package com.anjan.user06.sqlitedbwithcontentprovider;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.anjan.user06.sqlitedbwithcontentprovider.adapter.StudentsAdapter;
import com.anjan.user06.sqlitedbwithcontentprovider.contentprovider.StudentProvider;
import com.anjan.user06.sqlitedbwithcontentprovider.model.Students;
import com.anjan.user06.sqlitedbwithcontentprovider.sqlitedb.DbManager;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    EditText editName;
    EditText editDept;
    EditText editRegId;
    Button btnAdd;
    Button btnFetch;

    private DbManager dbManager;

    private RecyclerView recyclerView;
    private StudentsAdapter mAdapter;
    private ArrayList<Students> studentsArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editName = findViewById(R.id.editName);
        editDept = findViewById(R.id.textDept);
        editRegId = findViewById(R.id.editReg);
        btnAdd = findViewById(R.id.buttonAdd);
        btnFetch = findViewById(R.id.buttonFetch);

        studentsArrayList = new ArrayList<>();

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

                studentsArrayList = (ArrayList<Students>) dbManager.fetchStudents();
                mAdapter = new StudentsAdapter(studentsArrayList);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(mAdapter);

            }
        });
    }
}
