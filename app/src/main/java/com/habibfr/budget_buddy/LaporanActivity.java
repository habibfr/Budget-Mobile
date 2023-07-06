package com.habibfr.budget_buddy;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LaporanActivity extends AppCompatActivity {

    ImageView btnBack;
    LineChart lineChart;
    List<Entry> masuk, keluar;
    private static int user_id;
    boolean isSpinnerInitialized = false;
    int minYear, minMonth, maxYear, monthNumber;
    String dateSelectedItem = LocalDate.now().toString();
    private RecyclerView transaksiRecyclerView;
    private TransaksiAdapterLaporan transaksiAdapterLaporan;
    List<Transaksi> transaksiList = new ArrayList<Transaksi>();
    List<String> monthList = new ArrayList<>();

    //        URL COLLECTION
    String dirURL = "http://192.168.1.13/api-budget-buddy";
    String urlLaporan = dirURL.concat("/transactions/get_transaction_per_month.php");
    String urlSpinner = dirURL.concat("/transactions/get_min_date.php");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laporan);

        //        HIDE ACTION BAR
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();

        user_id = getIntent().getIntExtra("user_id", 0);

        Calendar calendar = Calendar.getInstance();

        transaksiRecyclerView = findViewById(R.id.recyclerLaporan);
        transaksiRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        btnBack = findViewById(R.id.imageArrowdown);

//        function collection
        btnBackOnClick();
        callTransaction(urlLaporan, dateSelectedItem);
//        Toast.makeText(this, dateSelectedItem, Toast.LENGTH_SHORT).show();
        getMinYearonTransaksi(urlSpinner, calendar);
    }

    private void onClickItemSpinner(Spinner monthSpinner) {
        monthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (isSpinnerInitialized) {
                    String[] selectedMonth = monthList.get(i).split(" ");
                    String monthSelected = selectedMonth[0];
                    String yearSelected = selectedMonth[1];

                    List<String> months = new ArrayList<>(Arrays.asList(new DateFormatSymbols().getMonths()));
                    monthNumber = months.indexOf(monthSelected) + 1;

                    dateSelectedItem = yearSelected.concat("-").concat(String.valueOf(monthNumber)).concat("-1");
                    callTransaction(urlLaporan, dateSelectedItem);
                } else {
                    isSpinnerInitialized = true;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void arrayAdapterForSpinner(List<String> monthList) {
        ArrayAdapter<String> monthAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, monthList);
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner monthSpinner = findViewById(R.id.spinnerLaporanDate);
        monthSpinner.setAdapter(monthAdapter);
        onClickItemSpinner(monthSpinner);
    }

    private void getMinYearonTransaksi(String url, Calendar calendar) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject iniresponse = new JSONObject(response);
                    JSONArray arr = iniresponse.getJSONArray("data");
                    for (int i = 0; i < arr.length(); i++) {
                        JSONObject inidata = arr.getJSONObject(i);
                        minYear = inidata.getInt("MIN_YEAR");
                        minMonth = inidata.getInt("MIN_MONTH");
                        maxYear = inidata.getInt("MAX_YEAR");
                    }

                    monthListforSpinnerLaporan(calendar, url);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ON ERROR RESPONSE", "onErrorResponse: " + error.getMessage());
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() {
                HashMap<String, String> parameter = new HashMap<>();
                parameter.put("user_id", String.valueOf(user_id));

                return parameter;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(LaporanActivity.this);
        requestQueue.add(stringRequest);
    }

    private void monthListforSpinnerLaporan(Calendar calendar, String url) {

        int currYear = calendar.get(Calendar.YEAR);
        int currMonth = calendar.get(Calendar.MONTH);

        for (int year = currYear; year >= minYear; year--) {
            String yearMonthString = "";
            if (year != currYear) {
                for (int month = 11; month >= 0; month--) {
                    String monthString = new DateFormatSymbols().getMonths()[month];
                    yearMonthString = monthString.concat(" ").concat(String.valueOf(year));
                    monthList.add(yearMonthString);
                }
            } else {
                for (int month = (minMonth - 1); month >= 0; month--) {
                    String monthString = new DateFormatSymbols().getMonths()[month];
                    yearMonthString = monthString.concat(" ").concat(String.valueOf(year));
                    monthList.add(yearMonthString);
                }
            }

        }

        arrayAdapterForSpinner(monthList);
    }

    private void callTransaction(String url, String dateSelectedItem) {
        Toast.makeText(this, dateSelectedItem, Toast.LENGTH_SHORT).show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject objectAcc = new JSONObject(response);
                    masuk = new ArrayList<Entry>();
                    keluar = new ArrayList<Entry>();

                    int status = objectAcc.getInt("status");
                    transaksiAdapterLaporan = new TransaksiAdapterLaporan(transaksiList);
                    transaksiList.clear();
                    if (objectAcc.has("data")) {
//                        Toast.makeText(LaporanActivity.this, objectAcc.getString("message"), Toast.LENGTH_SHORT).show();
                        JSONArray jsonArray = objectAcc.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject item = jsonArray.getJSONObject(i);
                            if (item.getString("type").equalsIgnoreCase("Masuk")) {
//                                Log.d("TAG", "onResponse: " + item.getString("amount"));
                                masuk.add(new Entry(i, Float.parseFloat(item.getString("amount"))));
                            } else {
//                                Log.d("TAG", "onResponse: " + item.getString("amount"));
                                keluar.add(new Entry(i, Float.parseFloat(item.getString("amount"))));
                            }

//                            FORMAT TANGGAL, DIUBAH DARI 'YYYY-MM-DD' MENJADI 'DD-MM-YYYY'
                            String outputFormat = "dd-MM-yyyy";
                            SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                            SimpleDateFormat outputDateFormat = new SimpleDateFormat(outputFormat);
                            Date date = inputDateFormat.parse(item.getString("date"));
                            assert date != null;
                            String outputDate = outputDateFormat.format(date);

                            Transaksi transaksi = new Transaksi(item.getInt("transaction_id"), item.getInt("user_id"), item.getString("title"), outputDate, item.getString("type"), item.getLong("amount"), item.getString("additional_info"), item.getString("created_at"));
                            Log.d("TRANSAKSI", "onResponse: " + transaksi);
                            transaksiList.add(transaksi);
                        }

                        setUpLineChart();

                        Log.d("TRANSAKSI LIST", "onCreate: " + transaksiList);
                        transaksiRecyclerView.setAdapter(transaksiAdapterLaporan);
                        findViewById(R.id.txtTidakAdaTransaksi).setVisibility(View.GONE);
                        transaksiRecyclerView.setVisibility(View.VISIBLE);
                    } else {
                        Toast.makeText(LaporanActivity.this, objectAcc.getString("message"), Toast.LENGTH_SHORT).show();
                        transaksiRecyclerView.setAdapter(transaksiAdapterLaporan);
                        findViewById(R.id.txtTidakAdaTransaksi).setVisibility(View.VISIBLE);
                        transaksiRecyclerView.setVisibility(View.GONE);
                    }

                } catch (JSONException | ParseException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("INI ERROR", "onErrorResponse: " + error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> parameter = new HashMap<>();
                parameter.put("user_id", String.valueOf(user_id));
                parameter.put("tanggal", dateSelectedItem);
//                Toast.makeText(LaporanActivity.this, dateSelectedItem, Toast.LENGTH_SHORT).show();

                return parameter;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(LaporanActivity.this);
        requestQueue.add(stringRequest);
    }

    private void setUpLineChart() {
        lineChart = findViewById(R.id.lineChart);

//        SETTING LINE MASUK OF LINE CHARTS
        LineDataSet masukLineDataSet = new LineDataSet(masuk, "Masuk");
        masukLineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        masukLineDataSet.setColor(Color.BLUE);
        masukLineDataSet.setCircleRadius(5);
        masukLineDataSet.setCircleColor(Color.BLUE);

//        SETTING LINE KELUAR OF LINE CHARTS
        LineDataSet keluarLineDataSet = new LineDataSet(keluar, "Keluar");
        keluarLineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        keluarLineDataSet.setColor(Color.RED);
        keluarLineDataSet.setCircleColor(Color.RED);
        keluarLineDataSet.setCircleRadius(5);

//        SETTING LEGENDA LINE CHARTS
        Legend legend = lineChart.getLegend();
        legend.setEnabled(true);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setDrawInside(false);

//        IMPLEMENT LINE ON LINE CHARTS
        LineData lineData = new LineData(masukLineDataSet, keluarLineDataSet);
        lineChart.setDragEnabled(false);
        lineChart.setPinchZoom(false);
        lineChart.setDoubleTapToZoomEnabled(false);
        lineChart.getDescription().setEnabled(false);
        lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        lineChart.getAxisRight().setEnabled(false);
        lineChart.setData(lineData);
        lineChart.invalidate();
    }

    private void btnBackOnClick() {
        btnBack.setOnClickListener(view -> {
            Intent intent = new Intent(LaporanActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        });
    }
}