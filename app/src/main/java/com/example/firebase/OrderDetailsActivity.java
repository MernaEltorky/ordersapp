package com.example.firebase;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class OrderDetailsActivity extends AppCompatActivity {

    MaterialButton materialButton;
    TextInputEditText textInputEditTextRequestDescription,
     textInputEditTextPhoneNumber,
    textInputEditTextFirstLocation,
     textInputEditLastLocation,
     textInputEditDate,
     textInputEditTime,
    textInputEditTextIsAccept,
    textInputEditTextState,
    textInputEditTextIsFinished;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        //  بستقبل الداتا اللي في كلاس ال order data من الMain activity
        OrderData orderData = (OrderData) getIntent().getSerializableExtra("orderData");


        materialButton = findViewById(R.id.order_details_btn);
        textInputEditTextRequestDescription = findViewById(R.id.order_details_request_description);
        textInputEditTextPhoneNumber = findViewById(R.id.order_details_phone_number);
        textInputEditTextFirstLocation = findViewById(R.id.order_details_first_location);
        textInputEditLastLocation = findViewById(R.id.order_details_last_location);
        textInputEditDate = findViewById(R.id.order_details_date);
        textInputEditTime = findViewById(R.id.order_details_time);
        textInputEditTextIsAccept = findViewById(R.id.order_details_is_accept);
        textInputEditTextState = findViewById(R.id.order_details_state);
        textInputEditTextIsFinished= findViewById(R.id.order_details_is_finish);

        textInputEditTextRequestDescription.setText(orderData.getRequestDescription());
        textInputEditTextPhoneNumber.setText(orderData.getPhoneNumber());
        textInputEditTextFirstLocation.setText(orderData.getFirstLocation());
        textInputEditLastLocation.setText(orderData.getLastLocation());
        textInputEditDate.setText(orderData.getDate());
        textInputEditTime.setText(orderData.getTime());
        textInputEditTextIsFinished.setText(orderData.getState());

        String isAccept;
        if (orderData.isAccept()){

            isAccept = "Yes";
        } else {
            isAccept = "No";
        }

        textInputEditTextIsAccept.setText(isAccept);

        String isFinished;
        if (orderData.isFinished()){

            isFinished = "Yes";
        } else {
            isFinished= "No";
        }

        textInputEditTextIsAccept.setText(isFinished);


        materialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



    }
}