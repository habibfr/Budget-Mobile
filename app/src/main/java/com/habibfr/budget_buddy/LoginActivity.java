package com.habibfr.budget_buddy;

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

    TextView txtRegisterInLogin, txtMessageError;
    Button btnLogin;
    EditText editUsername, editPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txtRegisterInLogin = findViewById(R.id.txtSignUpInLogin);
        btnLogin = findViewById(R.id.btnLogin);
        editUsername = (EditText) findViewById(R.id.editEmail);
        editPassword = (EditText) findViewById(R.id.editPassword);
        txtMessageError = findViewById(R.id.txtMessageError);


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Konektor("http://192.168.43.37/pbm/uas/users/cek_login.php", new Uri.Builder().appendQueryParameter("email", editUsername.getText().toString()).appendQueryParameter("password", editPassword.getText().toString())).execute();


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

    public class Konektor extends AsyncTask<String, String, String> {
        private static final int READ_TIMEOUT = 15000;
        private static final int CONNECTION_TIMEUT = 10000;
        Context context;
//        ProgressDialog pdLoading = new ProgressDialog(LoginActivity.this);
        HttpURLConnection conn;
        URL url = null;
        String situs;
        //        String p1, p2, p3;
        Uri.Builder builder;


        public Konektor(String situs, Uri.Builder builder) {
            this.situs = situs;
            this.builder = builder;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            pdLoading.setMessage("\tloading...");
//            pdLoading.setCancelable(false);
//            pdLoading.show();
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
//            System.out.println(s);
            try {
                JSONObject jsonObject = new JSONObject(s);
//                System.out.println(jsonObject);
                String result = jsonObject.getString("success");
                System.out.println(result);
                if(!result.equals("1")){
                    txtMessageError.setVisibility(View.VISIBLE);
//                    System.out.println("gagal");
                }else{
//                    System.out.println("succes");
                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
//            pdLoading.dismiss();
//            txtStatus.setText(s.toString());
//            txtStatusLogin.setText(s.toString());
        }
    }



}