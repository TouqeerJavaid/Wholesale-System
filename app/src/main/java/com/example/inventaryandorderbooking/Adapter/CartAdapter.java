package com.example.inventaryandorderbooking.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.inventaryandorderbooking.Model.ProductCart;
import com.example.inventaryandorderbooking.ProductCartActivity;
import com.example.inventaryandorderbooking.R;
import com.example.inventaryandorderbooking.Static.Common;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder>{
    List<ProductCart> productCartList;
    Context context;
    DatabaseReference cartRef;

    public CartAdapter(List<ProductCart> foodCartList, Context context) {
        this.productCartList = foodCartList;
        this.context = context;
        this.cartRef = cartRef;
        cartRef = FirebaseDatabase.getInstance().getReference("Cart").child(Common.currentUser.getId());


    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.single_product_cart_item_layout, viewGroup, false);

        return new ViewHolder(itemView) ;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {

        Picasso.get().load(productCartList.get(i).getImageurl()).into(viewHolder.productImage);
        viewHolder.cartQty.setText("Qty : "+productCartList.get(i).getUserSelectedQty());
        viewHolder.productPrice.setText("Rs : "+productCartList.get(i).getProduct_sale_price());
        viewHolder.productName.setText(productCartList.get(i).getProduct_name());



        viewHolder.deleteCartItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cartRef.child(productCartList.get(i).getProductId()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(context, "Item deleted from cart!", Toast.LENGTH_SHORT).show();

                    }
                });

                Common.itemCost = Integer.parseInt(productCartList.get(i).getProduct_sale_price());
                productCartList.remove(i);
                CartAdapter.this.notifyDataSetChanged();

            }
        });

    }

    @Override
    public int getItemCount() {
        return productCartList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage;
        ImageView deleteCartItem;
        TextView productName, productPrice, cartQty;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.cartImage);
            productName = itemView.findViewById(R.id.cartName);
            productPrice = itemView.findViewById(R.id.cartPrice);
            cartQty = itemView.findViewById(R.id.cartQty);

            deleteCartItem = (ImageView) itemView.findViewById(R.id.deleteItemCart);
        }
    }
}
