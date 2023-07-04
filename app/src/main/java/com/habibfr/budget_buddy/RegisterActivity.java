package com.habibfr.budget_buddy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class RegisterActivity extends AppCompatActivity {

    TextView txtLoginInRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

//        INSERT INTO `users` (`id`, `fullname`, `email`, `password`, `createdAt`) VALUES (NULL, 'Habib Fatkhul Rohman', 'habib@gmail.com', '12345', current_timestamp());
        txtLoginInRegister = findViewById(R.id.txtLoginInRegister);

        txtLoginInRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}