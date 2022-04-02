package com.example.splitter;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

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
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        EditText email = findViewById(R.id.loginEmail);
        EditText password = findViewById(R.id.loginPassword);
        String emailStr = email.getText().toString();
        String passwordStr = password.getText().toString();

        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(Constant.TAG_DEBUG, document.getId() + " => " + document.getData());
                                Map<String, Object> userData = document.getData();
                                if (Objects.equals(userData.get("email"), emailStr) && Objects.equals(userData.get("password"), passwordStr)) {
                                    startActivity(new Intent(LoginUser.this, MainActivity.class));
                                    finish();
                                }
                            }
                        } else {
                            Log.w(Constant.TAG_WARN, "Error getting documents.", task.getException());
                        }
                    }
                });
    }

}