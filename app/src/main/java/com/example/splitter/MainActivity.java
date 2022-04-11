package com.example.splitter;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences(Constant.MY_PREFERENCE, Context.MODE_PRIVATE);

        TextView tv1 = findViewById(R.id.tv1);
        tv1.setText(sharedPreferences.getString(Constant.PASSWORD, ""));
    }
}