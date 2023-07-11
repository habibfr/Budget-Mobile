package com.habibfr.budget_buddy;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ProfilActivity extends AppCompatActivity {
    int user_id = 0;
    TextView txtFullname, txtEmail, txtPassword;
    ImageView imgEditFullname, imgEditEmail, imgEditPassword, imgArrowDown;
    Button logoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

        //        HIDE ACTION BAR
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();

        Intent intent = getIntent();
        if (intent != null) {
            Bundle s = intent.getExtras();
            if (s != null) {
                int id_user = (int) s.get("user_id");
                if ((!String.valueOf(id_user).isEmpty()) || (String.valueOf(id_user) != null)) {
                    user_id = id_user;
                }
            }

        }

        txtFullname = findViewById(R.id.txtFullnameProfile);
        txtEmail = findViewById(R.id.txtEmailProfile);
        txtPassword = findViewById(R.id.txtPasswordProfile);
        imgEditEmail = findViewById(R.id.imgEditEmail);
        imgEditFullname = findViewById(R.id.imgEditFullName);
        imgEditPassword = findViewById(R.id.imgEditPassword);
        imgArrowDown = findViewById(R.id.imageArrowdownProfile);
        logoutButton = findViewById(R.id.logoutButton);

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://172.16.55.12/pbm/uas/users/get_user.php";
//        String url = "http://192.168.43.37/pbm/uas/transactions/get_transaction.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        System.out.println(response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("success");

                            if (status.equalsIgnoreCase("1")) {
                                JSONArray dataTransaction = jsonObject.getJSONArray("users");
//                                System.out.println(dataTransaction);
                                for (int i = 0; i < dataTransaction.length(); i++) {
                                    JSONObject item = dataTransaction.getJSONObject(i);

                                    String fullName = item.getString("fullname");
                                    String email = item.getString("email");
                                    String password = item.getString("password");

                                    txtEmail.setText(email);
                                    txtFullname.setText(fullName);
                                    txtPassword.setText(password);
                                }
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
                paramV.put("user_id", String.valueOf(user_id));
                return paramV;
            }
        };

        queue.add(stringRequest);

        imgArrowDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent1 = new Intent(ProfilActivity.this, HomeActivity.class);
//                intent1.putExtra("user_id", user_id);
//                startActivity(intent1);
                finish();
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(ProfilActivity.this, LoginActivity.class);
//                intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent1);
                finish();
            }
        });

        imgEditEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Creating alert Dialog with one Button
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(ProfilActivity.this);

                // Setting Dialog Title
                alertDialog.setTitle("Change Email");

                // Setting Dialog Message
                alertDialog.setMessage("Enter an Email");
                final EditText input = new EditText(ProfilActivity.this);

                alertDialog.setView(input);
                input.setText(txtEmail.getText());
                // Setting Icon to Dialog
//                alertDialog.setIcon(R.drawable.key);

                // Setting Positive "Yes" Button
                alertDialog.setPositiveButton("YES",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Write your code here to execute after dialog

                                RequestQueue queue = Volley.newRequestQueue(ProfilActivity.this);
                                String url = "http://172.16.55.12/pbm/uas/users/update_user.php";
//        String url = "http://192.168.43.37/pbm/uas/transactions/get_transaction.php";

                                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                                        new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {

                                                RequestQueue queue = Volley.newRequestQueue(ProfilActivity.this);
                                                String url = "http://172.16.55.12/pbm/uas/users/get_user.php";
//        String url = "http://192.168.43.37/pbm/uas/transactions/get_transaction.php";

                                                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                                                        new Response.Listener<String>() {
                                                            @Override
                                                            public void onResponse(String response) {
//                        System.out.println(response);
                                                                try {
                                                                    JSONObject jsonObject = new JSONObject(response);
                                                                    String status = jsonObject.getString("success");

                                                                    if (status.equalsIgnoreCase("1")) {
                                                                        JSONArray dataTransaction = jsonObject.getJSONArray("users");
//                                System.out.println(dataTransaction);
                                                                        for (int i = 0; i < dataTransaction.length(); i++) {
                                                                            JSONObject item = dataTransaction.getJSONObject(i);

                                                                            String fullName = item.getString("fullname");
                                                                            String email = item.getString("email");
                                                                            String password = item.getString("password");

                                                                            txtEmail.setText(email);
                                                                            txtFullname.setText(fullName);
                                                                            txtPassword.setText(password);
                                                                        }
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
                                                        paramV.put("user_id", String.valueOf(user_id));
                                                        return paramV;
                                                    }
                                                };

                                                queue.add(stringRequest);

                                                Toast.makeText(ProfilActivity.this, "Berhasil Update Email", Toast.LENGTH_SHORT).show();

                                            }
                                        }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Log.e("Error", error.getMessage());
                                    }
                                }) {
                                    protected Map<String, String> getParams() {
                                        Map<String, String> paramV = new HashMap<>();
                                        paramV.put("user_id", String.valueOf(user_id));
                                        paramV.put("email", String.valueOf(input.getText()));
                                        paramV.put("fullname", String.valueOf(txtFullname.getText()));
                                        paramV.put("password", String.valueOf(txtPassword.getText()));
                                        return paramV;
                                    }
                                };

                                queue.add(stringRequest);

//                                Intent myIntent1 = new Intent(view.getContext(), Show.class);
//                                startActivityForResult(myIntent1, 0);
                            }
                        });
                // Setting Negative "NO" Button
                alertDialog.setNegativeButton("NO",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Write your code here to execute after dialog
                                dialog.cancel();
                            }
                        });

                // closed

                // Showing Alert Message
                alertDialog.show();
            }
        });

        imgEditFullname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Creating alert Dialog with one Button
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(ProfilActivity.this);

                // Setting Dialog Title
                alertDialog.setTitle("Change Fullname");

                // Setting Dialog Message
                alertDialog.setMessage("Enter a Fullname");
                final EditText input = new EditText(ProfilActivity.this);

                alertDialog.setView(input);
                input.setText(txtFullname.getText());
                // Setting Icon to Dialog
//                alertDialog.setIcon(R.drawable.key);

                // Setting Positive "Yes" Button
                alertDialog.setPositiveButton("YES",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Write your code here to execute after dialog

                                RequestQueue queue = Volley.newRequestQueue(ProfilActivity.this);
                                String url = "http://172.16.55.12/pbm/uas/users/update_user.php";
//        String url = "http://192.168.43.37/pbm/uas/transactions/get_transaction.php";

                                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                                        new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {

                                                RequestQueue queue = Volley.newRequestQueue(ProfilActivity.this);
                                                String url = "http://172.16.55.12/pbm/uas/users/get_user.php";
//        String url = "http://192.168.43.37/pbm/uas/transactions/get_transaction.php";

                                                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                                                        new Response.Listener<String>() {
                                                            @Override
                                                            public void onResponse(String response) {
//                        System.out.println(response);
                                                                try {
                                                                    JSONObject jsonObject = new JSONObject(response);
                                                                    String status = jsonObject.getString("success");

                                                                    if (status.equalsIgnoreCase("1")) {
                                                                        JSONArray dataTransaction = jsonObject.getJSONArray("users");
//                                System.out.println(dataTransaction);
                                                                        for (int i = 0; i < dataTransaction.length(); i++) {
                                                                            JSONObject item = dataTransaction.getJSONObject(i);

                                                                            String fullName = item.getString("fullname");
                                                                            String email = item.getString("email");
                                                                            String password = item.getString("password");

                                                                            txtEmail.setText(email);
                                                                            txtFullname.setText(fullName);
                                                                            txtPassword.setText(password);
                                                                        }
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
                                                        paramV.put("user_id", String.valueOf(user_id));
                                                        return paramV;
                                                    }
                                                };

                                                queue.add(stringRequest);

                                                Toast.makeText(ProfilActivity.this, "Berhasil Update Fullname", Toast.LENGTH_SHORT).show();

                                            }
                                        }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Log.e("Error", error.getMessage());
                                    }
                                }) {
                                    protected Map<String, String> getParams() {
                                        Map<String, String> paramV = new HashMap<>();
                                        paramV.put("user_id", String.valueOf(user_id));
                                        paramV.put("email", String.valueOf(txtEmail.getText()));
                                        paramV.put("fullname", String.valueOf(input.getText()));
                                        paramV.put("password", String.valueOf(txtPassword.getText()));
                                        return paramV;
                                    }
                                };

                                queue.add(stringRequest);

                            }
                        });
                // Setting Negative "NO" Button
                alertDialog.setNegativeButton("NO",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Write your code here to execute after dialog
                                dialog.cancel();
                            }
                        });

                // closed

                // Showing Alert Message
                alertDialog.show();
            }
        });

        imgEditPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Creating alert Dialog with one Button
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(ProfilActivity.this);

                // Setting Dialog Title
                alertDialog.setTitle("Change Password");

                // Setting Dialog Message
                alertDialog.setMessage("Enter a Password");
                final EditText input = new EditText(ProfilActivity.this);

                alertDialog.setView(input);
                input.setText(txtPassword.getText());
                // Setting Icon to Dialog
//                alertDialog.setIcon(R.drawable.key);

                // Setting Positive "Yes" Button
                alertDialog.setPositiveButton("YES",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Write your code here to execute after dialog

                                RequestQueue queue = Volley.newRequestQueue(ProfilActivity.this);
                                String url = "http://172.16.55.12/pbm/uas/users/update_user.php";
//        String url = "http://192.168.43.37/pbm/uas/transactions/get_transaction.php";

                                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                                        new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {

                                                RequestQueue queue = Volley.newRequestQueue(ProfilActivity.this);
                                                String url = "http://172.16.55.12/pbm/uas/users/get_user.php";
//        String url = "http://192.168.43.37/pbm/uas/transactions/get_transaction.php";

                                                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                                                        new Response.Listener<String>() {
                                                            @Override
                                                            public void onResponse(String response) {
//                        System.out.println(response);
                                                                try {
                                                                    JSONObject jsonObject = new JSONObject(response);
                                                                    String status = jsonObject.getString("success");

                                                                    if (status.equalsIgnoreCase("1")) {
                                                                        JSONArray dataTransaction = jsonObject.getJSONArray("users");
//                                System.out.println(dataTransaction);
                                                                        for (int i = 0; i < dataTransaction.length(); i++) {
                                                                            JSONObject item = dataTransaction.getJSONObject(i);

                                                                            String fullName = item.getString("fullname");
                                                                            String email = item.getString("email");
                                                                            String password = item.getString("password");

                                                                            txtEmail.setText(email);
                                                                            txtFullname.setText(fullName);
                                                                            txtPassword.setText(password);
                                                                        }
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
                                                        paramV.put("user_id", String.valueOf(user_id));
                                                        return paramV;
                                                    }
                                                };

                                                queue.add(stringRequest);

                                                Toast.makeText(ProfilActivity.this, "Berhasil Update Password", Toast.LENGTH_SHORT).show();
                                            }
                                        }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Log.e("Error", error.getMessage());
                                    }
                                }) {
                                    protected Map<String, String> getParams() {
                                        Map<String, String> paramV = new HashMap<>();
                                        paramV.put("user_id", String.valueOf(user_id));
                                        paramV.put("email", String.valueOf(txtEmail.getText()));
                                        paramV.put("fullname", String.valueOf(txtFullname.getText()));
                                        paramV.put("password", String.valueOf(input.getText()));
                                        return paramV;
                                    }
                                };

                                queue.add(stringRequest);
                            }
                        });
                // Setting Negative "NO" Button
                alertDialog.setNegativeButton("NO",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Write your code here to execute after dialog
                                dialog.cancel();
                            }
                        });

                // closed

                // Showing Alert Message
                alertDialog.show();
            }
        });
    }
}