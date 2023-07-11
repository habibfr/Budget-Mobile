package com.habibfr.budget_buddy;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {

    FrameLayout frameTambahTransaksi;
    ImageView imgFolder, imageComponentThree, imageUser;
    TextView helloUsername, txtCashInCard;
    static String username = "";
    static int user_id = 0;
    //    private String URL = "http://192.168.43.37/pbm/uas/transactions/get_transactions.php";
    private String URL = "http://172.16.55.12/pbm/uas/transactions/get_transactions.php";
    private final String URL_SALDO = "http://172.16.55.12/pbm/uas/transactions/get_saldo_per_account.php";
    //    private final String URL_SALDO = "http://192.168.43.37/pbm/uas/transactions/get_saldo_per_account.php";
    StringRequest stringRequest, stringRequestSaldo;
    RequestQueue requestQueue;

    List<Transaksi> transaksiList = new ArrayList<>();
    List<Saldo> saldoList = new ArrayList<>();
    TransaksiAdapter transaksiAdapter;
    ListView lvTransaksiHome;
    GridView gvSaldoHome;
    SaldoAdapter saldoAdapter;
    int saldoMasuk = 0;
    int saldoKeluar = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

//        HIDE ACTION BAR
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();

        imageUser = findViewById(R.id.imageUser);
        frameTambahTransaksi = findViewById(R.id.frameStackplus);
        imgFolder = findViewById(R.id.imageFolder);
        helloUsername = findViewById(R.id.helloUsername);
        lvTransaksiHome = findViewById(R.id.lvTransaksiHome);
        txtCashInCard = findViewById(R.id.txtCashInCard);
        gvSaldoHome = findViewById(R.id.gvSaldoHome);
        imageComponentThree = findViewById(R.id.imageComponentThree);


        Intent intentFromLogin = getIntent();
        if (intentFromLogin != null) {
            Bundle s = intentFromLogin.getExtras();
            if (s != null) {
                User user = (User) s.get("user");
                System.out.println(user.getUser_id());
                username = user.getFullname().toString();
                user_id = user.getUser_id();
            }
        }

        helloUsername.setText("Hello, " + username);


        stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    //instance of class JSONObj
                    JSONObject jsonObj = new JSONObject(response);
                    System.out.println(jsonObj);
                    String success = jsonObj.getString("status");
                    //instance of class JSONObj. Isi parameter berdasarkan dari nama array di JSON
                    JSONArray datas = jsonObj.getJSONArray("data");
                    for (int i = 0; i < datas.length(); i++) {
                        JSONObject data = datas.getJSONObject(i);
                        int transaction_id = Integer.parseInt(data.getString("transaction_id"));
                        int user_id = Integer.parseInt(data.getString("user_id"));
                        String title = data.getString("title");
                        String date = data.getString("date");
                        String type = data.getString("type");
                        String amount = data.getString("amount");
                        String additional_info = data.getString("additional_info");
                        String created_at = data.getString("created_at");
//                        System.out.println(date);
                        transaksiList.add(new Transaksi(transaction_id, user_id, title, date, type, Long.parseLong(amount), additional_info, created_at));
                        if (type.equals("Masuk")) {
                            saldoMasuk += Integer.parseInt(amount);
                        } else {
                            saldoKeluar += Integer.parseInt(amount);
                        }
                    }
                    saldoList.add(new Saldo(saldoMasuk, "Income"));
                    saldoList.add(new Saldo(saldoKeluar, "Expensive"));
                    saldoAdapter = new SaldoAdapter(getApplicationContext(), saldoList);
                    saldoAdapter.notifyDataSetChanged();
                    gvSaldoHome.setAdapter(saldoAdapter);

                    Collections.reverse(transaksiList);
                    transaksiAdapter = new TransaksiAdapter(getApplicationContext(), transaksiList);
                    transaksiAdapter.notifyDataSetChanged();
                    lvTransaksiHome.setAdapter(transaksiAdapter);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error.getMessage());
            }
        }) {
            @NonNull
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", String.valueOf(user_id));
                return params;
            }
        };

        stringRequestSaldo = new StringRequest(Request.Method.POST, URL_SALDO, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    //instance of class JSONObj
                    JSONObject jsonObj = new JSONObject(response);
//                    System.out.println(jsonObj);
                    JSONObject datas = jsonObj.getJSONObject("data");
                    System.out.println(datas);
                    String saldo = datas.getString("saldo");
                    txtCashInCard.setText("Rp. " + saldo);


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error.getMessage());
            }
        }) {
            @NonNull
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", String.valueOf(user_id));
                return params;
            }
        };

        requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
        requestQueue.add(stringRequestSaldo);


        frameTambahTransaksi.setOnClickListener(view -> {
            Intent intent = new Intent(HomeActivity.this, FormActivity.class);
            intent.putExtra("user_id", user_id);
            startActivity(intent);
//            finish();
        });

        imgFolder.setOnClickListener(view -> {
            Intent intent = new Intent(HomeActivity.this, LaporanActivity.class);
            intent.putExtra("user_id", user_id);
            startActivity(intent);
//            finish();
        });

        imageComponentThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, HistoryActivity.class);
                intent.putExtra("user_id", user_id);
                startActivity(intent);

            }
        });

        imageUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(HomeActivity.this, ProfilActivity.class);
                intent.putExtra("user_id", user_id);
                startActivity(intent);
//                finish();
            }
        });
    }
}