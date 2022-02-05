package com.abicodes.androidbatchtwo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class UserListActivity extends AppCompatActivity {

    UserListAdapter userListAdapter;
    ArrayList<ModelUser> mData;
    RecyclerView recyclerView;
    FirebaseAuth mAuth;
    FirebaseFirestore fstore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);
        
        recyclerView = findViewById(R.id.rv_list);
        mAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();
        
        getData();
    }

    private void setDataAdapter() {
        userListAdapter = new UserListAdapter(UserListActivity.this,mData);
        recyclerView.setAdapter(userListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(UserListActivity.this));
    }

    private void getData() {
//        mData = new ArrayList<ModelUser>();
//        mData.add(new ModelUser("Abin","uabineshh@gmail.com"));
//        mData.add(new ModelUser("Abinesh","uabineshh2@gmail.com"));
//        mData.add(new ModelUser("Abineshh","uabineshh@gmail.com"));
//        mData.add(new ModelUser("Abineshh","uabineshh@gmail.com"));
//        mData.add(new ModelUser("Abineshh","uabineshh@gmail.com"));
//        mData.add(new ModelUser("Abineshh","uabineshh@gmail.com"));
//        mData.add(new ModelUser("Abineshh","uabineshh@gmail.com"));

        fstore.collection("Users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    mData = new ArrayList<ModelUser>();
                    for(QueryDocumentSnapshot documentSnapshots:task.getResult()){
                        String name = documentSnapshots.getData().get("name").toString();
                        String email = documentSnapshots.getData().get("email").toString();
                        mData.add(new ModelUser(name,email));
                    }
                    setDataAdapter();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }
}