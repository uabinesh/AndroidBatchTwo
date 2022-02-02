package com.abicodes.androidbatchtwo.sharedpref;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.abicodes.androidbatchtwo.R;

public class SignActivity extends AppCompatActivity {

    EditText name;
    Button btn_next;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);

        name = findViewById(R.id.et_email);
        btn_next = findViewById(R.id.bt_forget_pwd);

        sharedPreferences = getApplicationContext().getSharedPreferences("Login",MODE_PRIVATE);
        editor = sharedPreferences.edit();

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = name.getText().toString();
                editor.putString("Username",username);
                editor.apply();
                startActivity(new Intent(SignActivity.this,ContactActivity.class));
            }
        });
    }
}