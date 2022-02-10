package com.abicodes.androidbatchtwo.on_boarding;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.abicodes.androidbatchtwo.FirstScreenActivity;
import com.abicodes.androidbatchtwo.R;
import com.abicodes.androidbatchtwo.UserListActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;

import java.io.IOException;
import java.util.HashMap;

public class ShowDetailsActivity extends AppCompatActivity {

    EditText et_name,et_email;
    TextView tv_user_list,tv_add_pic;
    Button btn_update,btn_logout,btn_upload;
    FirebaseAuth mAuth;
    FirebaseFirestore fstore;
    String Uid, name,email;
    FirebaseUser firebaseUser;
    Bitmap image;
    ImageView view_img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_details);

        et_name = findViewById(R.id.et_name);
        et_email = findViewById(R.id.et_email);
        btn_update = findViewById(R.id.bt_update);
        btn_logout = findViewById(R.id.bt_logout);
        tv_user_list = findViewById(R.id.tv_user_list);
        tv_add_pic = findViewById(R.id.tv_add_pic);
        btn_upload = findViewById(R.id.bt_upload);
        view_img = findViewById(R.id.iv_profile);

        mAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();
        Uid = mAuth.getCurrentUser().getUid();
        firebaseUser = mAuth.getCurrentUser();

        getData();

        tv_user_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShowDetailsActivity.this, UserListActivity.class);
                startActivity(intent);
            }
        });

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

            }
        });

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent intent = new Intent(ShowDetailsActivity.this, FirstScreenActivity.class);
                startActivity(intent);
                finish();
            }
        });

        tv_add_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CharSequence[] option = {"Take Picture","From Gallery","Cancel"};
                AlertDialog.Builder builder = new AlertDialog.Builder(ShowDetailsActivity.this);
                builder.setTitle("Select your option");
                builder.setItems(option, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case 0:
                                Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                startActivityForResult(takePicture,0);
                                break;
                            case 1:
                                Intent picPicture = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(picPicture,1);
                                break;
                            case 2:
                                dialog.dismiss();
                                break;
                        }
                    }
                });
                builder.show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode!=RESULT_CANCELED){
            switch (requestCode){
                case 0:
                    if(resultCode == RESULT_OK && data!=null){
                        image = (Bitmap) data.getExtras().get("data");
                        view_img.setImageBitmap(image);
                    }
                    else{
                        Toast.makeText(ShowDetailsActivity.this, "Please try again", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 1:
                    if(resultCode==RESULT_OK && data!=null){
                        Uri img_path = data.getData();
                        try {
                            image = MediaStore.Images.Media.getBitmap(ShowDetailsActivity.this.getContentResolver(),img_path);
                            view_img.setImageBitmap(image);
                        }
                        catch (IOException e){
                            e.printStackTrace();
                        }
                    }
                    else{
                        Toast.makeText(ShowDetailsActivity.this, "Please try again", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }else{
            Toast.makeText(ShowDetailsActivity.this, "Please try again", Toast.LENGTH_SHORT).show();
        }
    }

    private void ReAuthentication(String mPassword) {
        AuthCredential credential = EmailAuthProvider.getCredential(email,mPassword);
        firebaseUser.reauthenticate(credential).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                updateDetails();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ShowDetailsActivity.this, "Your password is wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateDetails() {
        String user_name = et_name.getText().toString();
        String user_email = et_email.getText().toString();

        HashMap<String,Object> user = new HashMap<>();
        user.put("name",user_name);
        user.put("email",user_email);

        firebaseUser.updateEmail(user_email).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                fstore.collection("Users").document(Uid).update(user);
                Toast.makeText(ShowDetailsActivity.this, "Successfully Updated", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ShowDetailsActivity.this, "Details not Updated", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getData() {
        DocumentReference documentReference = fstore.collection("Users").document(Uid);

        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                name = (String) documentSnapshot.getData().get("name");
                email = (String) documentSnapshot.getData().get("email");

                setData(name,email);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ShowDetailsActivity.this, "Can not get the user details", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setData(String name, String email) {
        et_name.setText(name);
        et_email.setText(email);
    }
}