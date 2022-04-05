package com.example.splitter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class RegisterUser extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText regPhone, regPassword1, regPassword2, edtOTP;
    private TextView errMsg;
    private String phoneStr, passwordStr, verificationId;
    // callback method is called on Phone auth provider.
    private final PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            // when we receive the OTP it contains a unique id which we are storing in our string which we have already created.
            verificationId = s;
        }

        // this method is called when user receive OTP from Firebase.
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            // below line is used for getting OTP code which is sent in phone auth credentials.
            final String code = phoneAuthCredential.getSmsCode();
            if (code != null) {
                edtOTP.setText(code);
                verifyCode(code);
            }
        }

        // when firebase doesn't sends our OTP code due to any error or issue.
        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(RegisterUser.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        mAuth = FirebaseAuth.getInstance();
        regPhone = findViewById(R.id.registerPhone);
        regPassword1 = findViewById(R.id.registerPassword1);
        regPassword2 = findViewById(R.id.registerPassword2);
        errMsg = findViewById(R.id.errMsg);
        edtOTP = findViewById(R.id.idEdtOtp);
        Button generateOTPBtn = findViewById(R.id.idBtnGetOtp);
        Button verifyOTPBtn = findViewById(R.id.idBtnVerify);

        generateOTPBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                phoneStr = regPhone.getText().toString();
                passwordStr = regPassword1.getText().toString();
                if (passwordStr.isEmpty()) {
                    errMsg.setText("Blank Password");
                    errMsg.setVisibility(View.VISIBLE);
                } else if (!passwordStr.equals(regPassword2.getText().toString())) {
                    errMsg.setText("Entered passwords don't match");
                    errMsg.setVisibility(View.VISIBLE);
                }
                if (!TextUtils.isEmpty(phoneStr)) {
                    // sending OTP on user phone number.
                    String phone = "+91" + phoneStr;
                    PhoneAuthOptions options = PhoneAuthOptions.newBuilder(mAuth)
                            .setPhoneNumber(phone)
                            .setTimeout(60L, TimeUnit.SECONDS)
                            .setActivity(RegisterUser.this)
                            .setCallbacks(mCallBack)
                            .build();
                    PhoneAuthProvider.verifyPhoneNumber(options);
                }
            }
        });

        verifyOTPBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(edtOTP.getText().toString())) {
                    verifyCode(edtOTP.getText().toString());
                }
            }
        });
    }

    // below method is use to verify code from Firebase.
    private void verifyCode(String code) {
        // below line is used for getting credentials from our verification id and code.
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithCredential(credential);
    }

    private void signInWithCredential(PhoneAuthCredential credential) {
        // inside this method we are checking if the code entered is correct or not.
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Map<String, Object> user = new HashMap<>();
                            user.put("phone", phoneStr);
                            user.put("password", passwordStr);
                            DbUtils.pushDataToFireStore(phoneStr, user);
                            Intent i = new Intent(RegisterUser.this, LoginUser.class);
                            startActivity(i);
                            finish();
                        }
                    }
                });
    }
}