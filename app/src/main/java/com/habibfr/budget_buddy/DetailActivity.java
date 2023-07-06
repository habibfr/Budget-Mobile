package com.habibfr.budget_buddy;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

public class DetailActivity extends AppCompatActivity {

    private TextView tipe_transaksi;
    private TextView judul;
    private TextView tanggal;
    private TextView total;
    private TextView ket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        tipe_transaksi = findViewById(R.id.tipe_transaksi);
        judul = findViewById(R.id.judul);
        tanggal = findViewById(R.id.inputViewTanggal_transaksi);
        total = findViewById(R.id.amount);
        ket = findViewById(R.id.keterangan);
        executeRequest();
    }

    private void executeRequest() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://10.0.2.2/uas/transactions/get_transaction.php";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Gson gson = new Gson();
                            ResponseData responseData = gson.fromJson(response, ResponseData.class);
                            Log.d("test", "onResponse: "+responseData);
                            if (responseData != null && responseData.getStatus() == 1) {
                                TransactionData transactionData = responseData.getData();
                                Log.d("test", "onResponse: "+ transactionData.getTransactionId());
                                if (transactionData != null) {
                                    tipe_transaksi.setText(String.valueOf(transactionData.getTransactionId()));
                                    judul.setText(String.valueOf(transactionData.getUserId()));
                                    tanggal.setText(transactionData.getTitle());
                                    total.setText(transactionData.getDate());
                                    ket.setText(transactionData.getType());
                                } else {
                                    Log.d("Error", "No transaction data found");
                                }
                            } else {
                                Log.d("Error", "Invalid response or status is not 1");
                            }
                        } catch (Exception e) {
                            Log.e("Error", "Failed to parse JSON response: " + e.getMessage());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error", error.getLocalizedMessage());
            }
        });
        queue.add(stringRequest);
    }

    private class ResponseData {
        private int status;
        private TransactionData data;

        public int getStatus() {
            return status;
        }

        public TransactionData getData() {
            return data;
        }
    }

    private class TransactionData {
        private int transaction_id;
        private int user_id;
        private String title;
        private String date;
        private String type;
        private String amount;
        private String additional_info;
        private String created_at;

        public int getTransactionId() {
            return transaction_id;
        }

        public int getUserId() {
            return user_id;
        }

        public String getTitle() {
            return title;
        }

        public String getDate() {
            return date;
        }

        public String getType() {
            return type;
        }

        public String getAmount() {
            return amount;
        }

        public String getAdditionalInfo() {
            return additional_info;
        }

        public String getCreatedAt() {
            return created_at;
        }
    }
}