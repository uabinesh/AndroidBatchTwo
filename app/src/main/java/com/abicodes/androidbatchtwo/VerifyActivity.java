package com.abicodes.androidbatchtwo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class VerifyActivity extends AppCompatActivity {

    String email;
    TextView tv_email,tv_resend;
    Button btn_verify;
    EditText et_code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify);

        tv_email=findViewById(R.id.tv_desc);
        tv_resend=findViewById(R.id.tv_resend);
        btn_verify = findViewById(R.id.bt_forget_pwd);
        et_code = findViewById(R.id.et_password);

        email=getIntent().getStringExtra("Email");

        tv_email.setText("Enter the verification code we just sent it to "+email);
        tv_resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(VerifyActivity.this,"Code resent to the email.\nCheck the email and type it here",Toast.LENGTH_LONG).show();
            }
        });

        btn_verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reset_funt();
            }
        });

    }

    private void reset_funt() {
        String code;

        code=et_code.getText().toString();
        if(!code.isEmpty()){
            Toast.makeText(VerifyActivity.this,"Your account password reset successful",Toast.LENGTH_LONG).show();
            Intent intent = new Intent(VerifyActivity.this,ChangePwdActivity.class);
            startActivity(intent);
            finish();
        }
        else{
            et_code.setError("Field is empty");
        }
    }
}