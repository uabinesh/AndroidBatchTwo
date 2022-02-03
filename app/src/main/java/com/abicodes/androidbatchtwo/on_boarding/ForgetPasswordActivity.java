package com.abicodes.androidbatchtwo.on_boarding;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.abicodes.androidbatchtwo.R;
import com.abicodes.androidbatchtwo.VerifyActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPasswordActivity extends AppCompatActivity {

    EditText et_email;
    Button btn_forget_password;
    FirebaseAuth mAuth;
    ConstraintLayout constraint_layout;
    Snackbar snackbar;

    private String EmailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        et_email = findViewById(R.id.et_email);
        btn_forget_password = findViewById(R.id.bt_forget_pwd);
        constraint_layout = findViewById(R.id.constraint_layout);

        mAuth = FirebaseAuth.getInstance();

        btn_forget_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate_email();
            }
        });
    }

    private void forget_password() {

        String email = et_email.getText().toString();
        mAuth.sendPasswordResetEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                snackbar = Snackbar.make(constraint_layout,"Reset link sent",Snackbar.LENGTH_SHORT).setAction("Dismiss", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        snackbar.dismiss();
                    }
                });
                snackbar.setActionTextColor(Color.CYAN);
                snackbar.show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

        Intent intent = new Intent(ForgetPasswordActivity.this, VerifyActivity.class);
        intent.putExtra("Email",email);
    }

    private void validate_email() {
        String email;
        email = et_email.getText().toString();

        if(!email.isEmpty()){
            if(email.matches(EmailPattern)){
                forget_password();
            }
            else{
                et_email.setError("Invalid Email");
            }
        }
        else{
            et_email.setError("Please fill the email");
        }
    }
}