package com.example.inventaryandorderbooking;

import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.inventaryandorderbooking.Model.ProductCart;
import com.example.inventaryandorderbooking.Model.Productinfo;
import com.example.inventaryandorderbooking.Static.Common;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

public class ShopkeeperProductDetails extends AppCompatActivity {

    Productinfo productinfo;
    DatabaseReference cartRef;
    FloatingActionButton addToCart;
    ImageView productImage;
    TextView productName, productDesc, productQty, productPrice, productDate, product_low_inventory_alert;
    ElegantNumberButton qtyNumber;
    String cartProductQty = "1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopkeeper_product_details);

        if(getIntent() != null){
            productinfo = getIntent().getParcelableExtra("selectedObject");
            Toast.makeText(this, ""+productinfo.getProduct_name(), Toast.LENGTH_SHORT).show();
            Log.d("qty", ""+productinfo.getProduct_quantity());
        }

        qtyNumber = (ElegantNumberButton) findViewById(R.id.qty_number_button);

        cartRef = FirebaseDatabase.getInstance().getReference("Cart").child(Common.currentUser.getId());
        productImage = (ImageView) findViewById(R.id.productImage);
        productName = (TextView) findViewById(R.id.productName);
        productDesc = (TextView) findViewById(R.id.productDesc);
        productQty = (TextView) findViewById(R.id.productQty);
        productDate = (TextView) findViewById(R.id.productAddedDate);
        productPrice = (TextView) findViewById(R.id.productPrice);

        addToCart = (FloatingActionButton) findViewById(R.id.add_to_cart);

        Picasso.get().load(productinfo.getImageurl()).into(productImage);
        productName.setText(productinfo.getProduct_name());
        productQty.setText(productinfo.getProduct_quantity());
        productPrice.setText("Price : "+productinfo.getProduct_sale_price());


       qtyNumber.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
           @Override
           public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
               cartProductQty = String.valueOf(newValue);
           }
       });


        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Common.currentUser.getId() != null && Integer.parseInt(productinfo.getProduct_quantity()) > 0 &&  Integer.parseInt(cartProductQty )<= Integer.parseInt(productinfo.getProduct_quantity()) ){
//                    Toast.makeText(ShopkeeperProductDetails.this, "Working!", Toast.LENGTH_SHORT).show();
                    ProductCart productCart = new ProductCart(productinfo.getImageurl(), productinfo.getProduct_name(), productinfo.getProduct_quantity(), cartProductQty, "",productinfo.getProduct_sale_price(),"","", Common.currentUser.getName(), Common.currentUser.getEmail(), Common.currentUser.getAddress(), Common.currentUser.getCity(), Common.currentUser.getPhone(), productinfo.getWholesalerId(), productinfo.getProductId() );

                    cartRef.child(productinfo.getProductId()).setValue(productCart).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(ShopkeeperProductDetails.this, "Product Added to Cart!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else{
                    Toast.makeText(ShopkeeperProductDetails.this, "Unable to add product into cart!", Toast.LENGTH_SHORT).show();
                }
            }
        });




    }
}
