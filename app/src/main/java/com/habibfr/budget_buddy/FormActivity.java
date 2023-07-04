package com.habibfr.budget_buddy;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;

public class FormActivity extends AppCompatActivity {

    String[] item = {"Pengeluaran","Pemasukkan"};

    AutoCompleteTextView autoCompleteTextView;

    ArrayAdapter<String> adapterItems;

    Button btn, pilihTanggal;

    EditText tanggal;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);
        autoCompleteTextView = findViewById(R.id.auto_complete_txt);
        // untuk dropdown
        adapterItems = new ArrayAdapter<String>(this,R.layout.list_transaksi,item);
        autoCompleteTextView.setAdapter(adapterItems);
        pilihTanggal = (Button) findViewById(R.id.InputTanggal);
        showDateDialog();
        tanggal = (EditText) findViewById(R.id.inputViewTanggal_transaksi);
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
    private void showDateDialog() {
        pilihTanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                deklarasi kalender dan ambil tahun, bulan, dan tanggal dari dialog
                final Calendar cal = Calendar.getInstance();
                int currentYear = cal.get(Calendar.YEAR);
                int currentMonth = cal.get(Calendar.MONTH);
                int currentDate = cal.get(Calendar.DAY_OF_MONTH);

//                date picker dialog
                DatePickerDialog datePickerDialog = new DatePickerDialog(FormActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        tanggal.setText(day + "-" + (month + 1) + "-" + year);
                    }
                }, currentYear, currentMonth, currentDate);
                datePickerDialog.show();
            }
        });
    }
}