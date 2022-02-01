package com.example.firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class SplashActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if (firebaseAuth.getCurrentUser() != null){
            getUserData(firebaseAuth.getCurrentUser().getUid());

        }else {

            Intent intent = new Intent(this,LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void getUserData(String uid) {
        firestore.collection("project1").document(uid)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                UserData userData = task.getResult().toObject(UserData.class);
                checkUserType(userData.isProvider());

            }
        });


    }

    private void checkUserType(boolean provider) {
        if (provider){
            Intent intent = new Intent(SplashActivity.this, MainProviderActivity.class);
            startActivity(intent);
            finish();


        }else {
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

    }


}