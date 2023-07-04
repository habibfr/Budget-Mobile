package com.habibfr.budget_buddy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

public class FormActivity extends AppCompatActivity {

    String[] item = {"Pengeluaran","Pemasukkan"};

    AutoCompleteTextView autoCompleteTextView;

    ArrayAdapter<String> adapterItems;

    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);
        autoCompleteTextView = findViewById(R.id.auto_complete_txt);
        // untuk dropdown
        adapterItems = new ArrayAdapter<String>(this,R.layout.list_transaksi,item);
        autoCompleteTextView.setAdapter(adapterItems);
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String item = adapterView.getItemAtPosition(i).toString();
                Toast.makeText(FormActivity.this,"item : "+item,Toast.LENGTH_SHORT).show();
            }
        });
        btn=findViewById(R.id.btn_batal);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FormActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
}