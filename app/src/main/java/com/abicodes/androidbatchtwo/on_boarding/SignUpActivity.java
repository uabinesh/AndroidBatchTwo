package com.abicodes.androidbatchtwo.on_boarding;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.abicodes.androidbatchtwo.MainActivity;
import com.abicodes.androidbatchtwo.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

    EditText et_name,et_email,et_password,et_re_password;
    Button btn_signup;
    TextView tv_login;
    ImageView eye_icon,eye_icon2;
    Snackbar snackbar;
    ConstraintLayout constraintLayout;

    private String EmailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private String MobilePattern = "^\\+[0-9]{10,13}$";
    private String PasswordPattern = "[a-zA-Z0-9\\\\!\\\\@\\\\#\\\\$]{8,24}";

    private FirebaseAuth mAuth;
    private FirebaseFirestore fstore;
    String Uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        et_name = findViewById(R.id.et_name);
        et_email = findViewById(R.id.et_email);
        et_password = findViewById(R.id.et_password);
        et_re_password = findViewById(R.id.et_re_password);
        btn_signup = findViewById(R.id.bt_signup);
        tv_login = findViewById(R.id.tv_login);
        eye_icon = findViewById(R.id.show_pass_btn);
        eye_icon2 = findViewById(R.id.show_pass_btn2);
        constraintLayout = findViewById(R.id.constraint_layout);

        mAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();

        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate_form();
            }
        });

        tv_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this,LoginActivity.class);
                startActivity(intent);
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

    private void signup_user(String name,String email,String password) {

        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                //success
                if(task.isSuccessful()){
                    //save user data
                    Uid = mAuth.getCurrentUser().getUid();
                    DocumentReference documentReference = fstore.collection("Users").document(Uid);

                    Map<String,Object> user = new HashMap<>();
                    user.put("name",name);
                    user.put("email",email);

                    documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(SignUpActivity.this, "Data Saved", Toast.LENGTH_SHORT).show();

                            //start next activity
                            Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                            startActivity(intent);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //snackbar = Snackbar.make(constraintLayout,"Wrong",Snackbar.LENGTH_SHORT).setAction()
                            Toast.makeText(SignUpActivity.this, "Data not Saved", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else{
                    Toast.makeText(SignUpActivity.this, "msg", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //failure
                Toast.makeText(SignUpActivity.this, "msg2", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void validate_form() {
        String name,email,password,re_password;

        name = et_name.getText().toString();
        email = et_email.getText().toString();
        password = et_password.getText().toString();
        re_password = et_re_password.getText().toString();

        if(!name.isEmpty()){
            if(!email.isEmpty()){
                if(email.matches(EmailPattern)){
                    if(!(password.isEmpty())){
                        if(password.matches(PasswordPattern)){
                            if(!re_password.isEmpty()){
                                if(re_password.matches(password)){
                                    signup_user(name,email,password);
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
                else{
                    et_email.setError("Invalid Email");
                }
            }
            else{
                et_email.setError("Please fill the email");
            }
        }
        else{
            et_name.setError("Please fill the name");
        }
    }
}
