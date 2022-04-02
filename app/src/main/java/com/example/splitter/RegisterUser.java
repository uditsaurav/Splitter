package com.example.splitter;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

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
                        pushDataToDB(user);
                        finish();
                    }
                }
            });
        }

    }

    void pushDataToDB(Map<String, Object> data) {
        int uid = data.hashCode();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document(String.valueOf(uid))
                .set(data)
                .addOnSuccessListener(new OnSuccessListener<>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(Constant.TAG_DEBUG, "DocumentSnapshot added ");
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(Constant.TAG_WARN, "Error adding document", e);
                    }
                });
    }
}