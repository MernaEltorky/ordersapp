package com.example.firebase;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ProviderOrderActivity extends AppCompatActivity {
    private static final String TAG = "ProviderOrderActivity";
    RecyclerView recyclerViewOrder ;
    OrderAdapter orderAdapter;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseFirestore firestore  = FirebaseFirestore.getInstance();
    List<OrderData> orderDataList= new ArrayList<>();
    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider_order);


        uid =firebaseAuth.getUid();
        recyclerViewOrder = findViewById(R.id.main_provider_order_rc);
        recyclerViewOrder.setLayoutManager(new LinearLayoutManager(ProviderOrderActivity.this));
        orderAdapter = new OrderAdapter(ProviderOrderActivity.this,orderDataList,orderInterface);
        recyclerViewOrder.setAdapter(orderAdapter);

        getOrders();
    }
    OrderAdapter.OrderInterface orderInterface = new OrderAdapter.OrderInterface() {
        @Override
        public void onOrderClick(OrderData orderData) {
            Intent intent =new Intent(ProviderOrderActivity.this, ProviderOrderDetailsActivity.class);
            //  ببعت الداتا اللي في كلاس ال order data لل orderDetails
            intent.putExtra("orderData",  orderData);
            startActivity(intent);
        }
    };

    private void getOrders() {
        firestore.collection("orders")
                .whereEqualTo("providerId",uid)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if ( error != null){
                            Log.i(TAG,"onEvent" + error.getLocalizedMessage());
                            Toast.makeText(ProviderOrderActivity.this, error.getLocalizedMessage(),Toast.LENGTH_LONG);
                            return;
                        }

                        orderDataList.clear();
                        for (DocumentSnapshot snapshot : value.getDocuments()){
                            OrderData orderData =snapshot.toObject(OrderData.class);
                            orderDataList.add(orderData);
                            Log.i(TAG,"onEvent"+ orderData.toString());
                        }

                        orderAdapter.notifyDataSetChanged();
                        Log.i(TAG,"onEvent" + orderDataList.size());
                    }
                });
    }


}