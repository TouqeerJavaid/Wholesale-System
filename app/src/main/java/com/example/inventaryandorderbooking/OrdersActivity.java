package com.example.inventaryandorderbooking;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.inventaryandorderbooking.Adapter.OrderListAdapter;
import com.example.inventaryandorderbooking.Model.OrderInfo;
import com.example.inventaryandorderbooking.Model.ProductCart;
import com.example.inventaryandorderbooking.Model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class OrdersActivity extends AppCompatActivity {

    RecyclerView orderRecyclerView;

    DatabaseReference orderRef, userRef;
    OrderListAdapter adapter;

    List<User> orderItemList;
    List<String> orderListKey;
    List<String> ordersDateInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);


        orderRef = FirebaseDatabase.getInstance().getReference("Orders");
        userRef = FirebaseDatabase.getInstance().getReference("Shopkeeper");


        orderRecyclerView = (RecyclerView) findViewById(R.id.orderListRecyclerView);
        orderRecyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(orderRecyclerView.getContext(),
                linearLayoutManager.getOrientation());
        orderRecyclerView.addItemDecoration(dividerItemDecoration);
        orderRecyclerView.setLayoutManager(linearLayoutManager);
        orderItemList = new ArrayList<>();
        ordersDateInfo = new ArrayList<>();
        orderListKey = new ArrayList<>();
        orderRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                orderListKey.clear();
                orderItemList.clear();
                ordersDateInfo.clear();
                for (final DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
//                    ProductCart orderItem = dataSnapshot1.getValue(ProductCart.class);
                    Log.d("key", dataSnapshot1.getKey());
//                    orderListKey.add(dataSnapshot1.getKey());

                    orderRef.child(dataSnapshot1.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            for (DataSnapshot dataSnapshot2 : dataSnapshot.getChildren()) {
                                Log.d("key", dataSnapshot2.getKey());
                                ProductCart productCart = dataSnapshot2.getValue(ProductCart.class);
                                ordersDateInfo.add(productCart.getProduct_add_date());
                                userRef.child(productCart.getShopkeeperId()).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        User userInfo = dataSnapshot.getValue(User.class);
                                        userInfo.setId(dataSnapshot.getKey());
                                        addToList(userInfo, dataSnapshot1.getKey());


                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                                break;
                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
//
//


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    public void addToList(User user, String orderKey) {
        orderItemList.add(user);
        orderListKey.add(orderKey);
        Log.d("size", "" + orderItemList.size());

        adapter = new OrderListAdapter(OrdersActivity.this, orderItemList, orderListKey, ordersDateInfo);

        orderRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}
