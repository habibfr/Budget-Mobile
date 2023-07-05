package com.habibfr.budget_buddy;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class LoginActivity extends AppCompatActivity {

    TextView txtRegisterInLogin, txtMessageError, messageSuccessRegis;
    Button btnLogin;
    EditText editEmail, editPassword;
    User user;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();

        txtRegisterInLogin = findViewById(R.id.txtSignUpInLogin);
        btnLogin = findViewById(R.id.btnLogin);
        editEmail = findViewById(R.id.editEmail);
        editPassword = findViewById(R.id.editPassword);
        txtMessageError = findViewById(R.id.txtMessageError);
        messageSuccessRegis = findViewById(R.id.pesanSukesRegis);


        Intent intent = getIntent();
        if (intent != null) {
            Bundle s = intent.getExtras();
            if (s != null) {
                String pesan = (String) s.get("message");

                if ((!pesan.isEmpty()) || (pesan != null)) {
                    messageSuccessRegis.setText("Successfully registration, please login!");
                    messageSuccessRegis.setVisibility(View.VISIBLE);
                }
                System.out.println("null");
            }

        }


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // ganti sesuai php file masing-masing
                new Konektor(LoginActivity.this, "http://192.168.43.37/pbm/uas/users/cek_login.php", new Uri.Builder().appendQueryParameter("email", editEmail.getText().toString()).appendQueryParameter("password", editPassword.getText().toString())).execute();
            }
        });


        txtRegisterInLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });


    }

    private class Konektor extends AsyncTask<String, String, String> {
        private static final int READ_TIMEOUT = 15000;
        private static final int CONNECTION_TIMEUT = 10000;
        Context context;
        ProgressDialog pdLoading;
        HttpURLConnection conn;
        URL url = null;
        String situs;
        //        String p1, p2, p3;
        Uri.Builder builder;
        String result = null;


        public Konektor(Context context, String situs, Uri.Builder builder) {
            this.context = context;
            this.situs = situs;
            this.builder = builder;
        }

        public String getResult() {
            return result;
        }

        public void setResult(String result) {
            this.result = result;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdLoading = new ProgressDialog(context);
            txtMessageError.setVisibility(View.GONE);
            pdLoading.setMessage("\tloading...");
            pdLoading.setCancelable(false);
            pdLoading.show();
        }

        @Override
        protected String doInBackground(String... strings) {

            try {
                url = new URL(situs);

                System.out.println("try 1");
            } catch (MalformedURLException e) {
//            throw new RuntimeException(e);
                e.printStackTrace();
                System.out.println("gagal 1");
                return e.getMessage();
            }

            try {
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEUT);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

//                Uri.Builder builder = new Uri.Builder().appendQueryParameter("nama", p1).appendQueryParameter("username", p2).appendQueryParameter("password", p3);
                String query = builder.build().getEncodedQuery();
                OutputStream outputStream = conn.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));
                bufferedWriter.write(query);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                conn.connect();
                System.out.println("try 2");
                System.out.println("yourLink?" + query);

            } catch (IOException e) {
                System.out.println("gagal 2");
                throw new RuntimeException(e);
            }

            try {
                int response_code = conn.getResponseCode();
                if (response_code != HttpURLConnection.HTTP_OK) {
                    System.out.println("gagal 3");
                    return ("Connection error");
                } else {
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }
                    System.out.println("try 3");
                    return (result.toString());
                }
            } catch (IOException e) {
                e.printStackTrace();
                return e.toString();
            } finally {
                conn.disconnect();
            }
        }

        @Override
        protected void onPostExecute(String s) {
            pdLoading.dismiss();
//            setResult(s);

            try {
                JSONObject result = new JSONObject(s);
                String success = result.getString("success");

                if (success.equals("1")) {
                    System.out.println("success");
                    System.out.println(result);
                    JSONArray users = result.getJSONArray("users");
                    JSONObject userData = null;
                    for (int i = 0; i < users.length(); i++) {
                        userData = users.getJSONObject(i);
                        System.out.println(userData);
                        String user_id = userData.getString("user_id");
                        String fullname = userData.getString("fullname");
                        String email = userData.getString("password");
                        user = new User(Integer.parseInt(user_id), fullname, email);
                    }
                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                    intent.putExtra("user", user);
                    startActivity(intent);
                    finish();
//                    System.out.println(dataUser.toString());
                } else {
                    txtMessageError.setVisibility(View.VISIBLE);
                    String msgGagal = result.getString("message");
                    txtMessageError.setText(msgGagal.toString());
//                    System.out.println(result.toString());
//                    System.out.println("gagal");
                }

            } catch (JSONException e) {
                throw new RuntimeException(e);
            }


//            txtStatus.setText(s.toString());
//            txtStatusLogin.setText(s.toString());
        }
    }


}