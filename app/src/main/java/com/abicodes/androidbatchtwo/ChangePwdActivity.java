package com.abicodes.androidbatchtwo;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.abicodes.androidbatchtwo.on_boarding.LoginActivity;

public class ChangePwdActivity extends AppCompatActivity {

    EditText et_password,et_re_password;
    Button btn_change;
    ImageView eye_icon,eye_icon2;

    private String EmailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private String MobilePattern = "^\\+[0-9]{10,13}$";
    private String PasswordPattern = "[a-zA-Z0-9\\\\!\\\\@\\\\#\\\\$]{8,24}";

    @Override
    public void onBackPressed() {
        // Simply Do noting!
        Toast.makeText(ChangePwdActivity.this,"You can't go back. Please fill the fields",Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pwd);

        et_password = findViewById(R.id.et_password);
        et_re_password = findViewById(R.id.et_re_password);
        btn_change = findViewById(R.id.bt_forget_pwd);
        eye_icon = findViewById(R.id.show_pass_btn);
        eye_icon2 = findViewById(R.id.show_pass_btn2);

        btn_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate();
            }
        });
        eye_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show_hide_password();
            }
        });

        eye_icon2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show_hide_password2();
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
    private void show_hide_password2() {
        if(et_re_password.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())){
            //Image visibility off
            eye_icon2.setImageResource(R.drawable.ic_visibility_off);
            //Show Password
            et_re_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        }
        else{
            //Image visibility
            eye_icon2.setImageResource(R.drawable.ic_visibility);
            //Hide Password
            et_re_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }
    }

    private void validate() {
        String password,re_password;

        password = et_password.getText().toString();
        re_password = et_re_password.getText().toString();

        if(!(password.isEmpty())){
            if(password.matches(PasswordPattern)){
                if(!re_password.isEmpty()){
                    if(re_password.matches(password)){
                        //Toast.makeText(ChangePwdActivity.this,"Code resent to the email.\nCheck the email and type it here",Toast.LENGTH_LONG).show();
                        signin_user();
                    }
                    else{
                        et_re_password.setError("Password is mismatched");
                    }
                }
                else{
                    et_re_password.setError("Please re-enter the password");
                }
            }
            else{
                et_password.setError("Password length should 8-24");
            }
        }
        else{
            et_password.setError("Please fill the password");
        }

    }

    private void signin_user() {
        Toast.makeText(ChangePwdActivity.this,"Your account password changed",Toast.LENGTH_LONG).show();
        Intent intent = new Intent(ChangePwdActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}