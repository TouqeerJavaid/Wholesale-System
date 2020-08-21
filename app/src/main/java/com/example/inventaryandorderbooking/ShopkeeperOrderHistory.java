package com.example.inventaryandorderbooking;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.inventaryandorderbooking.Adapter.OrderHistoryAdapter;
import com.example.inventaryandorderbooking.Adapter.OrderListAdapter;
import com.example.inventaryandorderbooking.Model.OrderHistoryInfo;
import com.example.inventaryandorderbooking.Model.ProductCart;
import com.example.inventaryandorderbooking.Model.User;
import com.example.inventaryandorderbooking.Static.Common;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ShopkeeperOrderHistory extends AppCompatActivity {

    RecyclerView orderRecyclerView;

    DatabaseReference orderRef, userRef;
    OrderHistoryAdapter adapter;
    int totalAmount=0;
    String orderDate = "";
    List<OrderHistoryInfo> orderItemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopekeeper_order_history);


        orderRef = FirebaseDatabase.getInstance().getReference("OrderHistory").child(Common.currentUser.getId());
        userRef = FirebaseDatabase.getInstance().getReference("Shopkeeper");


        orderRecyclerView = (RecyclerView) findViewById(R.id.orderListRecyclerView);
        orderRecyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(orderRecyclerView.getContext(),
                linearLayoutManager.getOrientation());
        orderRecyclerView.addItemDecoration(dividerItemDecoration);
        orderRecyclerView.setLayoutManager(linearLayoutManager);
        orderItemList = new ArrayList<>();

        orderRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (final DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
//                    ProductCart orderItem = dataSnapshot1.getValue(ProductCart.class);
                    Log.d("key", dataSnapshot.getKey());

                    orderRef.child(dataSnapshot.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for(DataSnapshot dataSnapshot2: dataSnapshot1.getChildren()){
                                ProductCart productCart = dataSnapshot2.getValue(ProductCart.class);
                                totalAmount += Integer.parseInt(productCart.getProduct_sale_price()) * Integer.parseInt(productCart.getUserSelectedQty());
                                orderDate = productCart.getProduct_add_date();
                            }
                            addToList(new OrderHistoryInfo(orderDate, String.valueOf(totalAmount)));
                            totalAmount = 0;
                            orderDate = "";

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

//                    userRef.child(dataSnapshot.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(DataSnapshot dataSnapshot) {
//                            User userInfo = dataSnapshot.getValue(User.class);
//                            userInfo.setId(dataSnapshot.getKey());
//                            addToList(userInfo);
//                        }
//
//                        @Override
//                        public void onCancelled(DatabaseError databaseError) {
//
//                        }
//                    });



                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    public void addToList(OrderHistoryInfo orderInfo){
        orderItemList.add(orderInfo);
        Log.d("size", ""+orderItemList.size());

        adapter = new OrderHistoryAdapter(ShopkeeperOrderHistory.this, orderItemList);

        orderRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}
