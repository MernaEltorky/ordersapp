package com.example.firebase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = "RegisterActivity";
    EditText editTextName, editTextEmail, editTextPassword, editTextConfirmPassword;
    CircleImageView profileImage;
    CheckBox checkBoxIsProvider;
    Uri imageUri = null;
    private String name="";
    private String email="";
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    FirebaseFirestore firestore  = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        editTextName = findViewById(R.id.register_et_name);
        editTextEmail = findViewById(R.id.register_et_email);
        editTextPassword = findViewById(R.id.register_et_password);
        editTextConfirmPassword = findViewById(R.id.register_et_confirm_password);
        profileImage = findViewById(R.id.profile_image);
        checkBoxIsProvider = findViewById(R.id.register_Cb_provider);


    }

    public void register(View view) {

        name = editTextName.getText().toString().trim();
        email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String confirmPassword = editTextConfirmPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || name.isEmpty()) {
            Toast.makeText(this, "Please fill all data ", Toast.LENGTH_LONG).show();
            return;
        }

        if (imageUri == null) {

            Toast.makeText(this, "please select image", Toast.LENGTH_LONG).show();
            return;
        }

        if (password.length() < 6) {
            Toast.makeText(this, "password should be at laste 6 charcters", Toast.LENGTH_LONG).show();


            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Password no matching ", Toast.LENGTH_LONG).show();
            return;

        }

        createUserByEmail(email, password);


    }

    private void createUserByEmail(String email, String password) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(RegisterActivity.this, "Account created", Toast.LENGTH_LONG).show();
                            String userUid = task.getResult().getUser().getUid();
                            uploadProfialImage(userUid);
                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();

                        } else {
                            String errorMessage = task.getException().getLocalizedMessage();
                            Toast.makeText(RegisterActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void uploadProfialImage(String userUid) {
        storageReference.child("ProfileImages").child(userUid).putFile(imageUri)
                .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                        if (task.isSuccessful()) {

                            Toast.makeText(RegisterActivity.this, "ImageUploaded", Toast.LENGTH_LONG).show();

                            getProfileImageUrl(userUid);

                        } else {

                            String errorMessage = task.getException().getLocalizedMessage();
                            Toast.makeText(RegisterActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
    // هجيب الصوره
    private void getProfileImageUrl(String userUid) {
        storageReference.child("ProfileImages").child(userUid).getDownloadUrl()
                .addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {

                        if (task.isSuccessful()) {
                            String profileImageUrl = task.getResult().toString();
                            Log.i(TAG, "onComplete" +profileImageUrl);
                            upplodUserData(userUid,profileImageUrl);

                        } else {

                            String errorMessage = task.getException().getLocalizedMessage();
                            Toast.makeText(RegisterActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                        }

                    }
                });
    }


    //المفروض يبعت الداتا لل fire store


    private void upplodUserData(String userUid, String profileImageUrl) {

        UserData userData = new UserData(name, email, profileImageUrl,checkBoxIsProvider.isChecked());
        firestore.collection("project1")
                .document(userUid)
                .set(userData)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {

                            Toast.makeText(RegisterActivity.this, "User data uploud ", Toast.LENGTH_LONG);
                        } else {

                            String errorMessage = task.getException().getLocalizedMessage();
                            Toast.makeText(RegisterActivity.this, errorMessage, Toast.LENGTH_LONG);
                        }
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                imageUri = result.getUri();
                profileImage.setImageURI(imageUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    public void selectImage(View view) {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(this);


    }
}