package com.example.firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class AddOrderActivity extends AppCompatActivity {
    MaterialButton materialButton;
    TextInputEditText textInputEditTextRequestDescription;
    TextInputEditText textInputEditTextPhoneNumber;
    TextInputEditText textInputEditTextFirstLocation;
    TextInputEditText textInputEditLastLocation;
    TextInputEditText textInputEditDate;
    TextInputEditText textInputEditTime;

    private String uid;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseFirestore firestore  = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_order);

        materialButton = findViewById(R.id.add_order_btn);
        textInputEditTextRequestDescription = findViewById(R.id.add_order_request_description);
        textInputEditTextPhoneNumber = findViewById(R.id.add_order_phone_number);
        textInputEditTextFirstLocation = findViewById(R.id.add_order_first_location);
        textInputEditLastLocation = findViewById(R.id.add_order_last_location);
        textInputEditDate = findViewById(R.id.add_order_date);
        textInputEditTime = findViewById(R.id.add_order_time);

        uid = firebaseAuth.getCurrentUser().getUid();


        materialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDateFromUi();
            }
        });
    }

    private void getDateFromUi() {
        String requestDescription = textInputEditTextRequestDescription.getText().toString().trim();
        String phoneNumber = textInputEditTextPhoneNumber.getText().toString().trim();
        String firstLocation = textInputEditTextFirstLocation.getText().toString().trim();
        String lastLocation = textInputEditLastLocation.getText().toString().trim();
        String Date = textInputEditDate.getText().toString().trim();
        String Time = textInputEditTime.getText().toString().trim();

        if (requestDescription.isEmpty()||phoneNumber.isEmpty()||firstLocation.isEmpty()
        ||lastLocation.isEmpty()||Date.isEmpty()||Time.isEmpty()){
            Toast.makeText(this,"please fall all data",Toast.LENGTH_LONG).show();
            return;
        }
        String orderId = String.valueOf(System.currentTimeMillis());
        OrderData orderData = new OrderData(orderId,uid,requestDescription,phoneNumber,
                firstLocation,lastLocation,Date,Time,false,false,"Not Accept ","");
        uploadOrderData(orderData);

    }

    private void uploadOrderData(OrderData orderData) {

        firestore.collection("orders").document(orderData.getOrderId())
                .set(orderData).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(AddOrderActivity.this,"Done",Toast.LENGTH_LONG).show();
                    finish();

                }else {
                    String errorMessage = task.getException().getLocalizedMessage();
                    Toast.makeText(AddOrderActivity.this,errorMessage,Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}