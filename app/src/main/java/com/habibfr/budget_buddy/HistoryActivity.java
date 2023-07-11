package com.habibfr.budget_buddy;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HistoryActivity extends AppCompatActivity {

    int user_id;
    String url;
    ImageView imageArrowDownHist;
    private RecyclerView recyclerViewHist;
    private TransaksiAdapterLaporan transaksiAdapterLaporan;
    List<Transaksi> transaksiList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        //        HIDE ACTION BAR
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();

        user_id = getIntent().getIntExtra("user_id", 0);
        url = "http://172.16.55.12/pbm/uas/transactions/get_transactions.php";

        imageArrowDownHist = findViewById(R.id.imageArrowdownHist);
        recyclerViewHist = findViewById(R.id.recyclerHistory);
        recyclerViewHist.setLayoutManager(new LinearLayoutManager(this));

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject res = new JSONObject(response);
                    System.out.println(response);

                    transaksiAdapterLaporan = new TransaksiAdapterLaporan(transaksiList);

                    String resStatus = res.getString("status");
                    if (resStatus.equalsIgnoreCase("1")) {
                        JSONArray dataArray = res.getJSONArray("data");

                        for (int i = 0; i < dataArray.length(); i++) {
                            JSONObject item = dataArray.getJSONObject(i);

                            String outputFormat = "dd-MM-yyyy";
                            SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                            SimpleDateFormat outputDateFormat = new SimpleDateFormat(outputFormat);
                            Date date = inputDateFormat.parse(item.getString("date"));
                            assert date != null;
                            String outputDate = outputDateFormat.format(date);

                            Transaksi transaksi = new Transaksi(item.getInt("transaction_id"), item.getInt("user_id"), item.getString("title"), outputDate, item.getString("type"), item.getLong("amount"), item.getString("additional_info"), item.getString("created_at"));
                            transaksiList.add(transaksi);
                        }
                    }

                    Collections.reverse(transaksiList);
                    recyclerViewHist.setAdapter(transaksiAdapterLaporan);
                } catch (JSONException | ParseException e) {
                    throw new RuntimeException(e);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> parameters = new HashMap<>();
                parameters.put("user_id", String.valueOf(user_id));
                return parameters;
            }
        };

        RequestQueue req = Volley.newRequestQueue(HistoryActivity.this);
        req.add(stringRequest);

        imageArrowDownHist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}