package com.habibfr.budget_buddy;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class HomeActivity extends AppCompatActivity {

    FrameLayout frameTambahTransaksi;
    ImageView imgFolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

//        HIDE ACTION BAR
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();

        frameTambahTransaksi = findViewById(R.id.frameStackplus);
        imgFolder = findViewById(R.id.imageFolder);

        frameTambahTransaksi.setOnClickListener(view -> {
            Intent intent = new Intent(HomeActivity.this, FormActivity.class);
            startActivity(intent);
            finish();
        });

        imgFolder.setOnClickListener(view -> {
            Intent intent = new Intent(HomeActivity.this, LaporanActivity.class);
            startActivity(intent);
            finish();
        });
    }
}