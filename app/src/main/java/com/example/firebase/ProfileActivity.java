 package com.example.firebase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {
    private static final String TAG = "ProfileActivity";
    CircleImageView circleImageView;
    EditText editTextEmail,editTextUserName;
    MaterialButton materialButton;
    ProgressBar progressBar;
    FirebaseFirestore firestore =FirebaseFirestore.getInstance();
    FirebaseAuth firebaseAuth =FirebaseAuth.getInstance();
    StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        circleImageView = findViewById(R.id.profile_user_image);
        editTextEmail = findViewById(R.id.profile_user_email);
        editTextUserName = findViewById(R.id.profile_user_name);
        materialButton =findViewById(R.id.profile_btn_update);
        progressBar = findViewById(R.id.profile_progress_bar);
        uid = firebaseAuth.getCurrentUser().getUid();


        getUserData();

        materialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUsername();
            }
        });


        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectImage();
            }
        });

    }

    private void getUserData() {

        firestore.collection("project1").document(uid)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    UserData userData=task.getResult().toObject(UserData.class);
                    Log.i(TAG,"onComplete"+userData.toString());
                    updateUi(userData);
                }else{

                    String errorMessage=task.getException().getLocalizedMessage();
                    Log.i(TAG,"onComplete"+errorMessage);
                    Toast.makeText(ProfileActivity.this,errorMessage,Toast.LENGTH_LONG).show();


                }
            }
        });

    }

    private void updateUi(UserData userData) {
        editTextEmail.setText(userData.getEmail());
        editTextUserName.setText(userData.getName());

        //Picasso.get().load(userData.getProfileImageUrl()).placeholder(R.drawable.profial).into(circleImageView);
        Glide.with(this).load(userData.getProfileImageUrl()).into(circleImageView);




    }

    private void updateUsername(){

        String username=editTextUserName.getText().toString().trim();
        if (username.isEmpty()){
            Toast.makeText(this,"please write name",Toast.LENGTH_LONG).show();
            editTextUserName.setError("Name required");
            return;
        }
        HashMap<String,Object> usernameMap= new HashMap<>();
        usernameMap.put("name",username);
        firestore.collection("project1").document(uid)
                .update(usernameMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
           if (task.isSuccessful()){
               Toast.makeText(ProfileActivity.this,"name update",Toast.LENGTH_LONG).show();
           }else {

               String errorMessage=task.getException().getLocalizedMessage();
               Toast.makeText(ProfileActivity.this,errorMessage,Toast.LENGTH_LONG).show();

           }
            }
        });

    }
    //هيخليني اختار الصور 1
    private void selectImage(){
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(this);
    }


    //الصوره هترجع كا URI
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri imageUri = result.getUri();
                circleImageView.setImageURI(imageUri);
                uploadProfialImage(imageUri);
                startLoading();

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    // هرفع الصوره بقى في الابلكشين
    private void uploadProfialImage(Uri imageUri) {
        storageReference.child("ProfileImages").child(uid).putFile(imageUri)
                .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                        if (task.isSuccessful()) {

                            Toast.makeText(ProfileActivity.this, "ImageUploaded", Toast.LENGTH_LONG).show();
                            getProfileImageUrl();

                        } else {

                            String errorMessage = task.getException().getLocalizedMessage();
                            Toast.makeText(ProfileActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    // هاخد بقى  ال URL بتاع الصوره
    private void getProfileImageUrl() {
        storageReference.child("ProfileImages").child(uid).getDownloadUrl()
                .addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {

                        if (task.isSuccessful()) {
                            String profileImageUrl = task.getResult().toString();
                            Log.i(TAG, "onComplete" +profileImageUrl);
                            updateProfileImageUrl(profileImageUrl);
                        } else {

                            String errorMessage = task.getException().getLocalizedMessage();
                            Toast.makeText(ProfileActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                        }

                    }
                });
    }

    // هعمل ابديت لل URL في الفاير بيز

    private void updateProfileImageUrl(String profileImageUrl) {

        HashMap<String,Object> map= new HashMap<>();
        map.put("profileImageUrl",profileImageUrl);
        firestore.collection("project1").document(uid)
                .update(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(ProfileActivity.this,"Image Updated",Toast.LENGTH_LONG).show();
                    stopLoading();

                }else {
                    String errorMessage = task.getException().getLocalizedMessage();
                    Toast.makeText(ProfileActivity.this,errorMessage,Toast.LENGTH_LONG).show();
                    startLoading();
                }

            }
        });

    }


    private void startLoading(){
        progressBar.setVisibility(View.VISIBLE);
    }

    private void stopLoading(){

        progressBar.setVisibility(View.GONE);
    }



}