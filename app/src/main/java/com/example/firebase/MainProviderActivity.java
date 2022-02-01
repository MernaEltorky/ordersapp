package com.example.firebase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MainProviderActivity extends AppCompatActivity {

    private static final String TAG = "MainProviderActivity";
    RecyclerView recyclerViewOrder ;
    OrderAdapter orderAdapter;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseFirestore firestore  = FirebaseFirestore.getInstance();
    List<OrderData> orderDataList= new ArrayList<>();
    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_provider);


        uid =firebaseAuth.getUid();
        recyclerViewOrder = findViewById(R.id.main_provider_rc);
        recyclerViewOrder.setLayoutManager(new LinearLayoutManager(MainProviderActivity.this));
        orderAdapter = new OrderAdapter(MainProviderActivity.this,orderDataList,orderInterface);
        recyclerViewOrder.setAdapter(orderAdapter);

        getOrders();

    }

    OrderAdapter.OrderInterface orderInterface = new OrderAdapter.OrderInterface() {
        @Override
        public void onOrderClick(OrderData orderData) {
            Intent intent =new Intent(MainProviderActivity.this, ProviderOrderDetailsActivity.class);
            //  ببعت الداتا اللي في كلاس ال order data لل orderDetails
            intent.putExtra("orderData",  orderData);
            startActivity(intent);
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id==R.id.menu_item_profile){
            openProfileActivity();
        }

        else if (id == R.id.menu_item_provider_orders){

            startActivity(new Intent(MainProviderActivity.this,ProviderOrderActivity.class));

        }
        else if (id==R.id.menu_item_logout){
            logout();


        }
        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        firebaseAuth.signOut();
        if(firebaseAuth.getCurrentUser() == null ){
            // go to login screen
            Intent intent = new Intent(MainProviderActivity.this,LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }

    }

    private void openProfileActivity() {

        startActivity(new Intent(this,ProfileActivity.class));
    }

    private void getOrders() {
        firestore.collection("orders")
                .whereEqualTo("accept",false)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if ( error != null){
                            Log.i(TAG,"onEvent" + error.getLocalizedMessage());
                            Toast.makeText(MainProviderActivity.this, error.getLocalizedMessage(),Toast.LENGTH_LONG);
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