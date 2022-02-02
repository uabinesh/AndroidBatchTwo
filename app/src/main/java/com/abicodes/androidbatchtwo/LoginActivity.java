package com.abicodes.androidbatchtwo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    TextView tv_signup,tv_forget;
    EditText et_email,et_password;
    Button bt_login;
    ImageView eye_icon;

    private final String EmailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private String MobilePattern = "^\\+[0-9]{10,13}$";
    private String PasswordPattern = "[a-zA-Z0-9\\\\!\\\\@\\\\#\\\\$]{8,24}";

    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        tv_signup = findViewById(R.id.tv_resend);
        tv_forget = findViewById(R.id.tv_forget);
        et_email = findViewById(R.id.et_email);
        et_password = findViewById(R.id.et_password);
        bt_login = findViewById(R.id.bt_forget_pwd);
        eye_icon = findViewById(R.id.show_pass_btn);

        mAuth = FirebaseAuth.getInstance();

        Animation animation = AnimationUtils.loadAnimation(this,R.anim.right_to_left);
        bt_login.startAnimation(animation);

        eye_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show_hide_password();
            }
        });

        bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //btn click
                //Toast.makeText(LoginActivity.this,"Logged in Successfully!",Toast.LENGTH_LONG).show();
                validate_fields();
            }
        });

        tv_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,SignUpActivity.class);
                startActivity(intent);
            }
        });

        tv_forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,ForgetPasswordActivity.class);
                startActivity(intent);
            }
        });
    }

    private void show_hide_password() {
        if(et_password.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())){
            //Image visibility off
            eye_icon.setImageResource(R.drawable.ic_visibility_off);
            //Show Password
            et_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        }
        else{
            //Image visibility
            eye_icon.setImageResource(R.drawable.ic_visibility);
            //Hide Password
            et_password.setTransformationMethod(PasswordTransformationMethod.getInstance());

        }
    }

    private void LogUser(String email,String password) {
        //log the user
        mAuth.signInWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Intent intent = new Intent(LoginActivity.this,ShowDetailsActivity.class);
                startActivity(intent);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(LoginActivity.this, "Fail msg", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void validate_fields() {
        //validate fields
        String email,password;

        email = et_email.getText().toString();
        password = et_password.getText().toString();

        if(!(email.isEmpty())){
            if(email.matches(EmailPattern)){
                if(!(password.isEmpty())){
                    if(password.matches(PasswordPattern)){
                        Toast.makeText(LoginActivity.this,"Hello user "+email,Toast.LENGTH_LONG).show();
                        LogUser(email,password);
                    }
                    else{
                        et_password.setError("Password length should 8-24");
                    }
                }
                else{
                    et_password.setError("Please fill password");
                }
            }
            else{
                et_email.setError("Invalid Email");
            }
        }
        else{
            et_email.setError("Please fill email");
        }

    }
}