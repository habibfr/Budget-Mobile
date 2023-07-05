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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {

    FrameLayout frameTambahTransaksi;
    ImageView imgFolder;
    TextView helloUsername;
    static String username = "";
    static int user_id = 0;
    private String URL = "http://192.168.43.37/pbm/uas/transactions/get_transactions.php";
    StringRequest stringRequest;
    RequestQueue requestQueue;

    List<Transaksi> transaksiList = new ArrayList<>();
    TransaksiAdapter transaksiAdapter;
    ListView lvTransaksiHome;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

//        HIDE ACTION BAR
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();

        frameTambahTransaksi = findViewById(R.id.frameStackplus);
        imgFolder = findViewById(R.id.imageFolder);
        helloUsername = findViewById(R.id.helloUsername);
        lvTransaksiHome = findViewById(R.id.lvTransaksiHome);

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

        helloUsername.setText(username);
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
                        System.out.println(date);
                        transaksiList.add(new Transaksi(transaction_id, user_id, title, date, type, amount, additional_info, created_at));
                    }
                    transaksiAdapter = new TransaksiAdapter(getApplicationContext(), transaksiList);
                    System.out.println(transaksiList.size());
                    lvTransaksiHome.setAdapter(transaksiAdapter);
                    transaksiAdapter.notifyDataSetChanged();
//                    //hitung jumlah baris data
//                    for (int i = 0; i < jsonMahasiswa.length(); i++) {
//                        jsonData = jsonMahasiswa.getJSONObject(i);
//                        //tampung data ke dalam variabel
//                        nim = jsonData.getString("nim");
//                        nama = jsonData.getString("nama");
//
//                        //membuat data adapter menggunakan method add()
//                        adapterMahasiswa.add("NIM = " + nim + "\n" + "Nama = " + nama);
//                    }
//                    //mengirim data adapter utk di tempatkan ke dalam List view menggunakan method setAdapter()
//                    code_lvMahasiswa.setAdapter(adapterMahasiswa);
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



        frameTambahTransaksi.setOnClickListener(view -> {
            Intent intent = new Intent(HomeActivity.this, FormActivity.class);
            startActivity(intent);
            finish();
        });

        imgFolder.setOnClickListener(view -> {
            Intent intent = new Intent(HomeActivity.this, LaporanActivity.class);
            startActivity(intent);
            finish();
        });
    }
}