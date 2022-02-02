package com.abicodes.androidbatchtwo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ShowDetailsActivity extends AppCompatActivity {

    EditText et_name,et_email;
    Button btn_update,btn_logout;
    FirebaseAuth mAuth;
    FirebaseFirestore fstore;
    String Uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_details);

        et_name = findViewById(R.id.et_name);
        et_email = findViewById(R.id.et_email);
        btn_update = findViewById(R.id.bt_update);
        btn_logout = findViewById(R.id.bt_logout);

        mAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();
        Uid = mAuth.getCurrentUser().getUid();

        getData();

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ShowDetailsActivity.this);
                builder.setTitle("Re-Authentication");
                builder.setMessage("Enter your password");
                final EditText input = new EditText(ShowDetailsActivity.this);
                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                builder.setView(input);

                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String mPassword = input.getText().toString();
                        ReAuthentication(mPassword);

                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();


                String name = et_name.getText().toString();
                String email = et_email.getText().toString();

                Map<String,Object> user = new HashMap<>();
                user.put("name",name);
                user.put("email",email);
            }
        });

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent intent = new Intent(ShowDetailsActivity.this,FirstScreenActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void ReAuthentication(String mPassword) {

    }

    private void getData() {
        DocumentReference documentReference = fstore.collection("Users").document(Uid);

        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                String name = (String) documentSnapshot.getData().get("name");
                String email = (String) documentSnapshot.getData().get("email");

                setData(name,email);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

    }

    private void setData(String name, String email) {
        et_name.setText(name);
        et_email.setText(email);
    }
}