package com.example.splitter;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Map;

public class RegisterUser extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        TextView register = findViewById(R.id.registerButton);
        register.setOnClickListener(call -> registerUser());
    }

    @SuppressLint("SetTextI18n")
    private void registerUser() {
        EditText regPhone = findViewById(R.id.registerPhone);
        EditText regPassword1 = findViewById(R.id.registerPassword1);
        EditText regPassword2 = findViewById(R.id.registerPassword2);
        TextView errMsg = findViewById(R.id.errMsg);
        String phoneStr = regPhone.getText().toString();
        String passwordStr = regPassword1.getText().toString();
        if (passwordStr.isEmpty()) {
            errMsg.setText("Blank Password");
            errMsg.setVisibility(View.VISIBLE);
        } else if (!passwordStr.equals(regPassword2.getText().toString())) {
            errMsg.setText("Entered passwords don't match");
            errMsg.setVisibility(View.VISIBLE);
        } else {
            Map<String, Object> user = new HashMap<>();
            user.put("phone", phoneStr);
            user.put("password", passwordStr);
            DbUtils.pushDataToFireStore(phoneStr, user);
            finish();
        }

    }

}