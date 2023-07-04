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

public class RegisterActivity extends AppCompatActivity {

    TextView txtLoginInRegister, txtMessageError;
    Button btnRegister;
    EditText editFullname, editEmail, editPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();

//        INSERT INTO `users` (`id`, `fullname`, `email`, `password`, `createdAt`) VALUES (NULL, 'Habib Fatkhul Rohman', 'habib@gmail.com', '12345', current_timestamp());
        txtLoginInRegister = findViewById(R.id.txtLoginInRegister);
        btnRegister = findViewById(R.id.btnRegister);
        editFullname = findViewById(R.id.editFullnameRegister);
        editEmail = findViewById(R.id.editEmailRegister);
        editPassword = findViewById(R.id.editPasswordRegister);
        txtMessageError = findViewById(R.id.txtMessageErrorRegister);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                if (!editPassword.getText().toString().equals(editConfirmPassword.getText().toString())){
//                    txtMessageError.setText("Password tidak sama!");
//                    System.out.println("tidak sama");
//                    txtMessageError.setVisibility(View.VISIBLE);
//                }else{
                    System.out.println("sama");
                    new Konektor(RegisterActivity.this, "http://192.168.43.37/pbm/uas/users/add_user.php", new Uri.Builder().appendQueryParameter("fullname", editFullname.getText().toString()).appendQueryParameter("email", editEmail.getText().toString()).appendQueryParameter("password", editPassword.getText().toString())).execute();
//                }

            }
        });




        txtLoginInRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
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
//            txtMessageError.setVisibility(View.GONE);
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
            System.out.println(s);
            try {
                JSONObject result = new JSONObject(s);
//                System.out.println(result);
                String success = result.getString("success");

                if (success.equals("1")) {
//                    System.out.println("success");
                    System.out.println(result);
                    String message = result.getString("message");

                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    intent.putExtra("message", message);
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