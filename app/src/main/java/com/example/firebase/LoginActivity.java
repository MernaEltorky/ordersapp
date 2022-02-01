package com.example.firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {

    EditText editTextEmail, editTextPassword;
    private static final String TAG = "LoginActivity";
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextEmail = findViewById(R.id.login_et_email);
        editTextPassword = findViewById(R.id.login_et_password);

       // if (firebaseAuth.getCurrentUser() != null){
           // getUserData(firebaseAuth.getCurrentUser().getUid());
       // }
    }

    public void login(View view) {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill all data ", Toast.LENGTH_LONG).show();
            return;
        }

        if (password.length() < 6) {
            Toast.makeText(this, "password should be at laste 6 charcters", Toast.LENGTH_LONG).show();

            return;
        }
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            getUserData(task.getResult().getUser().getUid());
                        } else {

                            String errorMessage = task.getException().getLocalizedMessage();
                            Toast.makeText(LoginActivity.this, errorMessage, Toast.LENGTH_LONG).show();


                        }

                    }
                });
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
            Intent intent = new Intent(LoginActivity.this, MainProviderActivity.class);
            startActivity(intent);
            finish();


        }else {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

    }

    public void register(View view) {

        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
        finish();
        return;

    }

}