package com.habibfr.budget_buddy;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DetailActivity extends AppCompatActivity {

    private TextView tipe_transaksi;
    private TextView judul;
    private TextView tanggal;
    private TextView total;
    private TextView ket;
    private Button btn;
    int transaction_id = 0;
    int user_id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        //        HIDE ACTION BAR
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();

        Intent intent = getIntent();
        if (intent != null) {
            Bundle s = intent.getExtras();
            if (s != null) {
                int id = (int) s.get("transaction_id");
                int id_user = (int) s.get("user_id");
                if ((!String.valueOf(id).isEmpty()) || (String.valueOf(id) != null)) {
                    transaction_id = id;
                    user_id = id_user;
                }
            }

        }

        tipe_transaksi = findViewById(R.id.tipe_transaksi);
        judul = findViewById(R.id.judul);
        tanggal = findViewById(R.id.inputViewTanggal_transaksi);
        total = findViewById(R.id.amount);
        ket = findViewById(R.id.keterangan);
        btn = findViewById(R.id.btn_ok);

        tipe_transaksi.setEnabled(false);
        judul.setEnabled(false);
        tanggal.setEnabled(false);
        total.setEnabled(false);
        ket.setEnabled(false);

        executeRequest();
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void executeRequest() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://172.16.55.12/pbm/uas/transactions/get_transaction.php";
//        String url = "http://192.168.43.37/pbm/uas/transactions/get_transaction.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        System.out.println(response);
//                       J
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");

                            if (status.equalsIgnoreCase("1")) {
                                JSONObject dataTransaction = jsonObject.getJSONObject("data");
                                System.out.println(dataTransaction);
                                String title = dataTransaction.getString("title");
                                String type = dataTransaction.getString("type");
                                String amount = dataTransaction.getString("amount");
                                String additional_info = dataTransaction.getString("additional_info");
                                String date = dataTransaction.getString("date");
                                tipe_transaksi.setText(type);
                                judul.setText(title);
                                total.setText(amount);
                                tanggal.setText(date);
                                ket.setText(additional_info);
                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error", error.getMessage());
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> paramV = new HashMap<>();
                paramV.put("transaction_id", String.valueOf(transaction_id));
                paramV.put("user_id", String.valueOf(user_id));
                return paramV;
            }
        };

        queue.add(stringRequest);
    }
}