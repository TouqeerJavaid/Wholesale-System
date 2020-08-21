package com.example.inventaryandorderbooking.Model;

import com.example.inventaryandorderbooking.Model.Productinfo;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.inventaryandorderbooking.Add_to_cart;
import com.example.inventaryandorderbooking.R;
import com.example.inventaryandorderbooking.ShopkeeperProductDetails;
import com.example.inventaryandorderbooking.Shopkeeper_home;
import com.google.firebase.database.ValueEventListener;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.V;
import static android.support.constraint.Constraints.TAG;

public class shopkeeper_home_products extends RecyclerView.Adapter<shopkeeper_home_products.ViewHolder>  {
    Context context;
    List<Productinfo> MainImageUploadInfoList;


    public shopkeeper_home_products(Context context, List<Productinfo> TempList ) {

        this.MainImageUploadInfoList = TempList;
        this.context = context;
    }




    @Override
    public shopkeeper_home_products.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shopkeeper_product_list, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final shopkeeper_home_products.ViewHolder holder, final int position) {
        Productinfo UploadInfo = MainImageUploadInfoList.get(position);

        holder.ProductimageNameTextView.setText(UploadInfo.getProduct_name());
        //Loading image from Glide library.
        Glide.with(context)
                .load(UploadInfo.getImageurl())
                .centerCrop()
                .into(holder.imageView);
        // Glide.with(context).load(UploadInfo.getImageurl()).into(holder.imageView);
         /*holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //log.d("TAG" onclick: clicked on)
                Intent intent = new Intent(context, Add_to_cart.class);
                //FloatBuffer imageView;

                log.d(TAG , "onclicked" + imageView.get(position));
                intent.putExtra("image_url", imageView . get(position));
                //intent.putExtra("longitude", holder.getLon());
                context.startActivity(intent);
            }
        });*/


         holder.itemView.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Intent intent = new Intent(context, ShopkeeperProductDetails.class);
                 intent.putExtra("selectedObject", MainImageUploadInfoList.get(position));
                 context.startActivity(intent);
             }
         });

    }
    @Override
    public int getItemCount() {

        return MainImageUploadInfoList.size();
    }



    class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView imageView;
        public TextView ProductimageNameTextView;
        public TextView ProductQuntityTextView;
        public TextView ProductPurchassTextView;
        public TextView ProductSaleTextView;
        public TextView ProductDateextView;


        public ViewHolder(View itemView)  {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
            ProductimageNameTextView = (TextView) itemView.findViewById(R.id.ImageNameTextView);
            ProductQuntityTextView = (TextView) itemView.findViewById(R.id.productquantityview);
            ProductPurchassTextView = (TextView) itemView.findViewById(R.id.productpurchaseview);
            ProductSaleTextView = (TextView) itemView.findViewById(R.id.productsaleviw);
            ProductDateextView = (TextView) itemView.findViewById(R.id.productdatview);
            // ViewHolder.setonclicklistener

        }

    }}
