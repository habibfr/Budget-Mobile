package com.habibfr.budget_buddy;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class FormActivity extends AppCompatActivity {

    String[] item = {"Keluar", "Masuk"};

    AutoCompleteTextView autoCompleteTextView;

    ArrayAdapter<String> adapterItems;

    Button btn, pilihTanggal, btnsub;

    EditText tanggal;

    EditText title, amount, additional_info;

    AutoCompleteTextView type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();

        autoCompleteTextView = findViewById(R.id.auto_complete_txt);
        // untuk dropdown
        adapterItems = new ArrayAdapter<String>(this, R.layout.list_transaksi, item);
        autoCompleteTextView.setAdapter(adapterItems);
        pilihTanggal = (Button) findViewById(R.id.InputTanggal);
        showDateDialog();
        type = findViewById(R.id.auto_complete_txt);
        tanggal = findViewById(R.id.inputViewTanggal_transaksi);
        title = findViewById(R.id.title);
        amount = findViewById(R.id.amount);
        additional_info = findViewById(R.id.additional_info);
        int user_id = getIntent().getIntExtra("user_id", 0);
        Button btnsub = findViewById(R.id.submit);
        btnsub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int id = user_id;
                String tipe = type.getText().toString();
                String tgl = tanggal.getText().toString();
                String judul = title.getText().toString();
                String total = amount.getText().toString();
                String keterangan = additional_info.getText().toString();
                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                String url = "http://172.16.55.12/pbm/uas/transactions/add_transaction.php";
//                String url = "http://192.168.43.37/pbm/uas/transactions/add_transaction.php";

                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
//                                if (response.equalsIgnoreCase("success")) {
//                                    Toast.makeText(getApplicationContext(), "Data Success : " + response, Toast.LENGTH_SHORT).show();
//                                }else{
//
//                                }
                                Intent intentToHome = new Intent(FormActivity.this, HomeActivity.class);
                                Toast.makeText(getApplicationContext(), "Data Success Ditambahkan", Toast.LENGTH_SHORT).show();
                                startActivity(intentToHome);
                                finish();
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("error", error.getLocalizedMessage());
                    }
                }) {
                    protected Map<String, String> getParams() {
                        Map<String, String> paramV = new HashMap<>();
                        paramV.put("user_id", String.valueOf(id));
                        paramV.put("type", tipe);
                        paramV.put("date", tgl);
                        paramV.put("title", judul);
                        paramV.put("amount", total);
                        paramV.put("additional_info", keterangan);
                        return paramV;
                    }

                };

                queue.add(stringRequest);
            }
        });
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String item = adapterView.getItemAtPosition(i).toString();
                Toast.makeText(FormActivity.this, "item : " + item, Toast.LENGTH_SHORT).show();
            }
        });
        btn = findViewById(R.id.btn_batal);
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
                        tanggal.setText(year + "-" + (month + 1) + "-" + day);
                    }
                }, currentYear, currentMonth, currentDate);
                datePickerDialog.show();
            }
        });
    }
}