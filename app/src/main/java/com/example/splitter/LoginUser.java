package com.example.splitter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Map;
import java.util.Objects;

public class LoginUser extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_user);

        Button login = findViewById(R.id.loginButton);
        login.setOnClickListener(call -> loginUser());
        TextView register = findViewById(R.id.registerLink);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginUser.this, RegisterUser.class));
            }
        });
    }

    private void loginUser() {
        EditText email = findViewById(R.id.loginEmail);
        EditText password = findViewById(R.id.loginPassword);
        String emailStr = email.getText().toString();
        String passwordStr = password.getText().toString();

        Map<String, Map<String, Object>> document = DbUtils.getDataFromFireStore();
        for (String docId : document.keySet()) {
            Map<String, Object> data = document.get(docId);
            if (data != null) {
                if (Objects.equals(data.get("email"), emailStr) && Objects.equals(data.get("password"), passwordStr)) {
                    startActivity(new Intent(LoginUser.this, MainActivity.class));
                    finish();
                    return;
                }
            }
        }
    }

}