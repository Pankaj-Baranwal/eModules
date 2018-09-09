package com.hashinclude.cmoc.emodulesapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.hashinclude.cmoc.emodulesapp.R;
import com.hashinclude.cmoc.emodulesapp.utils.DatabaseAdapter;

public class SelectionActivity extends AppCompatActivity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_selection);
        findViewById(R.id.course_1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseAdapter.DatabaseHelper.TABLE_NAME = "CFA_questions";
                startActivity(new Intent(SelectionActivity.this, MainActivity.class));
            }
        });

        findViewById(R.id.course_2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseAdapter.DatabaseHelper.TABLE_NAME = "GMAT_questions";
                startActivity(new Intent(SelectionActivity.this, MainActivity.class));
            }
        });
    }
}
