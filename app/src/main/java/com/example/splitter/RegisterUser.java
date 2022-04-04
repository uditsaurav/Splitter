package com.example.splitter;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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
        EditText regEmail = findViewById(R.id.registerEmail);
        EditText regPassword1 = findViewById(R.id.registerPassword1);
        EditText regPassword2 = findViewById(R.id.registerPassword2);
        TextView errMsg = findViewById(R.id.errMsg);
        String emailStr = regEmail.getText().toString();
        String passwordStr = regPassword1.getText().toString();
        if (passwordStr.isEmpty()) {
            errMsg.setText("Blank Password");
            errMsg.setVisibility(View.VISIBLE);
        } else if (!passwordStr.equals(regPassword2.getText().toString())) {
            errMsg.setText("Entered passwords don't match");
            errMsg.setVisibility(View.VISIBLE);
        } else {
            LinearLayout otpLayout = findViewById(R.id.otpLayout);
            otpLayout.setVisibility(View.VISIBLE);
            EditText otpText = findViewById(R.id.otpText);
            Button verifyOtp = findViewById(R.id.otpSubmit);
            verifyOtp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String otp = otpText.getText().toString();
                    if (!otp.isEmpty()) {
                        Map<String, Object> user = new HashMap<>();
                        user.put("email", emailStr);
                        user.put("password", passwordStr);
                        DbUtils.pushDataToFireStore(null, user);
                        finish();
                    }
                }
            });
        }

    }

}