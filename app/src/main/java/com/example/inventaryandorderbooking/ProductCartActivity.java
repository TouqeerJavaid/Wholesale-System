package com.example.inventaryandorderbooking;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.inventaryandorderbooking.Adapter.CartAdapter;
import com.example.inventaryandorderbooking.Model.OrderInfo;
import com.example.inventaryandorderbooking.Model.ProductCart;
import com.example.inventaryandorderbooking.Static.Common;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ProductCartActivity extends AppCompatActivity {

    RecyclerView productCartRecyclerView;
    DatabaseReference cartRef, orderRef, orderHistoryRef;
    List<ProductCart> productCartList;
    CartAdapter adapter;
    Button placeOrder;
    TextView totlaPrice, totalCalories;
    Button placeOrderBtn;
    double itemCalories;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_cart);
        mAuth  = FirebaseAuth.getInstance();

        orderHistoryRef = FirebaseDatabase.getInstance().getReference("OrderHistory").child(Common.currentUser.getId()).push();
        orderRef = FirebaseDatabase.getInstance().getReference("Orders").push();
        cartRef = FirebaseDatabase.getInstance().getReference("Cart").child(Common.currentUser.getId());
        placeOrderBtn = (Button) findViewById(R.id.placeOrder);
        totlaPrice = (TextView) findViewById(R.id.totalPrice);
        productCartRecyclerView = (RecyclerView) findViewById(R.id.productCartRecyclerView);
        productCartRecyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(productCartRecyclerView.getContext(),
                linearLayoutManager.getOrientation());
        productCartRecyclerView.addItemDecoration(dividerItemDecoration);
        productCartRecyclerView.setLayoutManager(linearLayoutManager);

        productCartList = new ArrayList<>();

        if(Common.currentUser.getId() != null) {
            cartRef = FirebaseDatabase.getInstance().getReference("Cart").child(Common.currentUser.getId());

            cartRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    productCartList.clear();
                    for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                        ProductCart productCart = dataSnapshot1.getValue(ProductCart.class);
                        productCartList.add(productCart);

                        Log.d("cart", dataSnapshot1.getKey());
                    }

                    adapter = new CartAdapter(productCartList, ProductCartActivity.this);
                    productCartRecyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    Common.itemCost = 0;
                    for(int i =0; i<productCartList.size(); i++){
                        Log.d("value", ""+Integer.parseInt(productCartList.get(i).getProduct_sale_price()) * Integer.parseInt(productCartList.get(i).getUserSelectedQty()));
                        Common.itemCost += (Integer.parseInt(productCartList.get(i).getProduct_sale_price()) * Integer.parseInt(productCartList.get(i).getUserSelectedQty()));


                    }

                    totlaPrice.setText("Total Rs : "+ Common.itemCost);


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.d("error", databaseError.getMessage());
                }
            });
        }

        placeOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for(int i = 0; i<productCartList.size(); i++){
                    ProductCart product = productCartList.get(i);
//                    String newDateStr = postFormater.format(dateObj);
                    SimpleDateFormat currentDate = new SimpleDateFormat("dd/MM/yyyy");
                    Date todayDate = new Date();
                    String disDate = currentDate.format(todayDate);
                    product.setProduct_add_date(disDate);
                    product.setShopkeeperId(Common.currentUser.getId());
                    orderRef.child(product.getProductId()).setValue(product);
                    orderHistoryRef.child(product.getProductId()).setValue(product);
                    cartRef.child(product.getProductId()).removeValue();
                }
                Toast.makeText(ProductCartActivity.this, "Order Placed Successfully!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), Shopkeeper_home.class);
                startActivity(intent);
                finish();

            }
        });

    }
}
