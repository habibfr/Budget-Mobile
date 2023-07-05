package com.habibfr.budget_buddy;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
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
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LaporanActivity extends AppCompatActivity {

    ImageView btnBack;
    LineChart lineChart;
    List<Entry> masuk, keluar;
    private RecyclerView transaksiRecyclerView;
    private TransaksiAdapterLaporan transaksiAdapterLaporan;
    List<Transaksi> transaksiList = new ArrayList<Transaksi>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laporan);

        //        HIDE ACTION BAR
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();

        transaksiRecyclerView = findViewById(R.id.recyclerLaporan);
        transaksiRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        String url = "http://192.168.1.13/api-budget-buddy/transactions/get_transaction_per_periode.php";

        btnBack = findViewById(R.id.imageArrowdown);

//        function collection
        btnBackOnClick();
        callTransaction(url);
    }

    private void callTransaction(String url) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject objectAcc = new JSONObject(response);
                    masuk = new ArrayList<Entry>();
                    keluar = new ArrayList<Entry>();

                    int status = objectAcc.getInt("status");
                    if (status == 1) {
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
                            Transaksi transaksi = new Transaksi(item.getInt("transaction_id"), item.getInt("user_id"), item.getString("title"), item.getString("date"), item.getString("type"), item.getLong("amount"), item.getString("additional_info"), item.getString("created_at"));
                            Log.d("TRANSAKSI", "onResponse: " + transaksi);
                            transaksiList.add(transaksi);
                        }

                        setUpLineChart();

                        Log.d("TRANSAKSI LIST", "onCreate: " + transaksiList);
                        transaksiAdapterLaporan = new TransaksiAdapterLaporan(transaksiList);
                        transaksiRecyclerView.setAdapter(transaksiAdapterLaporan);
//                        implementRecyclerViewLaporan(transaksiList);
                    } else {
                        Toast.makeText(LaporanActivity.this, objectAcc.getString("message"), Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
//                Toast.makeText(MainActivity.this, "Berhasil Register", Toast.LENGTH_LONG).show();
//                            Log.d("INI RESPONSE", "onResponse: "+response.trim());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                Log.d("INI ERROR", "onErrorResponse: " + error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameter = new HashMap<>();
                parameter.put("user_id", String.valueOf(1));

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