package com.alok328raj.digitalcafe;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.side_in, R.anim.side_out);
    }
}
