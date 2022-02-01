package com.example.firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ProviderOrderDetailsActivity extends AppCompatActivity {

    MaterialButton materialButtonAccept, materialButtonUpdate;
    TextInputEditText textInputEditTextRequestDescription,
            textInputEditTextPhoneNumber,
            textInputEditTextFirstLocation,
            textInputEditLastLocation,
            textInputEditDate,
            textInputEditTime,
            textInputEditTextState;
            CheckBox checkBoxIsFinish;
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider_order_details);

        OrderData orderData = (OrderData) getIntent().getSerializableExtra("orderData");


        materialButtonAccept= findViewById(R.id.provider_order_details_btn_accept);
        materialButtonUpdate = findViewById(R.id.provider_order_details_btn_update);
        textInputEditTextRequestDescription = findViewById(R.id.order_details_request_description);
        textInputEditTextPhoneNumber = findViewById(R.id.order_details_phone_number);
        textInputEditTextFirstLocation = findViewById(R.id.order_details_first_location);
        textInputEditLastLocation = findViewById(R.id.order_details_last_location);
        textInputEditDate = findViewById(R.id.order_details_date);
        textInputEditTime = findViewById(R.id.order_details_time);
        textInputEditTextState = findViewById(R.id.order_details_state);
        checkBoxIsFinish = findViewById(R.id.provider_order_details_cb_finish);

        textInputEditTextRequestDescription.setText(orderData.getRequestDescription());
        textInputEditTextPhoneNumber.setText(orderData.getPhoneNumber());
        textInputEditTextFirstLocation.setText(orderData.getFirstLocation());
        textInputEditLastLocation.setText(orderData.getLastLocation());
        textInputEditDate.setText(orderData.getDate());
        textInputEditTime.setText(orderData.getTime());


        if (orderData.isAccept()){
            materialButtonAccept.setVisibility(View.GONE);
        }

        materialButtonAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acceptOrder(orderData.getOrderId());
            }
        });


        materialButtonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDate(orderData.getOrderId());
            }
        });

    }

    private void acceptOrder(String orderId) {

        Map<String , Object> map = new HashMap<>();
        map.put("accept",true);
        map.put("providerId",firebaseAuth.getCurrentUser().getUid());
        firestore.collection("orders").document(orderId).update(map)
          .addOnCompleteListener(new OnCompleteListener<Void>() {
              @Override
              public void onComplete(@NonNull Task<Void> task) {
                  if (task.isSuccessful()){
                      Toast.makeText(ProviderOrderDetailsActivity.this,"orderAccept",Toast.LENGTH_LONG).show();
                      materialButtonAccept.setVisibility(View.GONE);
                  }else{

                      String errorMessage= task.getException().getLocalizedMessage();
                      Toast.makeText(ProviderOrderDetailsActivity.this,errorMessage,Toast.LENGTH_LONG).show();


                  }
              }
          });



}

     private void updateDate(String orderId){
        boolean isFinish=checkBoxIsFinish.isChecked();

        String orderState = textInputEditTextState .getText().toString().trim();

        if (orderState.isEmpty()){
            Toast.makeText(this,"please write order state", Toast.LENGTH_LONG).show();
            return;
        }

        Map<String , Object> map = new HashMap<>();
        map.put("finished" , isFinish);
        map.put("state",orderState);
        firestore.collection("orders").document(orderId)
                .update(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful()){

                    Toast.makeText(ProviderOrderDetailsActivity.this,"Order data update",Toast.LENGTH_LONG).show();

                }else {

                    String errorMessage= task.getException().getLocalizedMessage();
                    Toast.makeText(ProviderOrderDetailsActivity.this,errorMessage,Toast.LENGTH_LONG).show();

                }
            }
        });
     }

    }