package com.abicodes.androidbatchtwo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import com.abicodes.androidbatchtwo.on_boarding.LoginActivity;
import com.abicodes.androidbatchtwo.on_boarding.ShowDetailsActivity;
import com.abicodes.androidbatchtwo.on_boarding.SignUpActivity;
import com.google.firebase.auth.FirebaseAuth;

public class FirstScreenActivity extends AppCompatActivity {

    Button btn_login,btn_signup;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_screen);

        btn_login = findViewById(R.id.btn_login);
        btn_signup = findViewById(R.id.btn_signup);
        mAuth = FirebaseAuth.getInstance();

        if(mAuth.getCurrentUser()!=null){
            Intent intent = new Intent(FirstScreenActivity.this,ShowDetailsActivity.class);
            startActivity(intent);
            finish();
        }


        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation = AnimationUtils.loadAnimation(FirstScreenActivity.this,R.anim.bounce);
                btn_login.startAnimation(animation);
                Intent intent = new Intent(FirstScreenActivity.this, LoginActivity.class);
                startActivity(intent);

            }
        });

        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FirstScreenActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
    }
}