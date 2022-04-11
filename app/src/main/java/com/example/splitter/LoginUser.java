package com.example.splitter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Map;
import java.util.Objects;

public class LoginUser extends AppCompatActivity {

    Map<String, Map<String, Object>> document;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_user);

        sharedPreferences = getSharedPreferences(Constant.MY_PREFERENCE, Context.MODE_PRIVATE);

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

    @Override
    protected void onStart() {
        super.onStart();
        document = DbUtils.getDataFromFireStore();
    }

    private void loginUser() {
        EditText etPhone = findViewById(R.id.loginPhone);
        EditText etPassword = findViewById(R.id.loginPassword);
        String phone = etPhone.getText().toString();
        String password = etPassword.getText().toString();

        for (String docId : document.keySet()) {
            Map<String, Object> data = document.get(docId);
            if (data != null) {
                if (Objects.equals(data.get("phone"), phone) && Objects.equals(data.get("password"), password)) {
                    saveInSharedPreference(phone, password);
                    startActivity(new Intent(LoginUser.this, MainActivity.class));
                    finish();
                }
            }
        }
    }

    private void saveInSharedPreference(String phone, String password) {
        sharedPreferences = getSharedPreferences(Constant.MY_PREFERENCE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constant.PHONE, phone);
        editor.putString(Constant.PASSWORD, password);
        editor.apply();
    }

}