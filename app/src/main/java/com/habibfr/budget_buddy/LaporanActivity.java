package com.habibfr.budget_buddy;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class LaporanActivity extends AppCompatActivity {

    ImageView btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laporan);

        //        HIDE ACTION BAR
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();

        btnBack = findViewById(R.id.imageArrowdown);

//        function collection
        btnBackOnClick();
    }

    private void btnBackOnClick() {
        btnBack.setOnClickListener(view -> {
            Intent intent = new Intent(LaporanActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        });
    }
}