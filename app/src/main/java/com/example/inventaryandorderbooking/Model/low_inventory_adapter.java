package com.example.inventaryandorderbooking.Model;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.inventaryandorderbooking.R;

import java.util.List;
import android.support.v7.widget.RecyclerView;

public class low_inventory_adapter extends RecyclerView.Adapter<low_inventory_adapter.ViewHolder> {
    Context context;
    List<Productinfo> MainImageUploadInfoList;

    public low_inventory_adapter(Context context, List<Productinfo> TempList) {

        this.MainImageUploadInfoList = TempList;

        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.low_inventory_list, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Productinfo UploadInfo = MainImageUploadInfoList.get(position);

        holder.ProductimageNameTextView.setText(UploadInfo.getProduct_name());
        holder.ProductQuntityTextView.setText(UploadInfo.getProduct_quantity());
        holder.ProductPurchassTextView.setText(UploadInfo.getProduct_purchase_price());
        holder.ProductSaleTextView.setText(UploadInfo.getProduct_sale_price());
        holder.ProductDateextView.setText(UploadInfo.getProduct_add_date());
        holder.ProductLowInventory.setText(UploadInfo.getProduct_low_inventory_alerts());
        //Loading image from Glide library.
        Glide.with(context)
                .load(UploadInfo.getImageurl())
                .centerCrop()
                .into(holder.imageView);

    }

    @Override
    public int getItemCount() {

        return MainImageUploadInfoList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageView;
        public TextView ProductimageNameTextView;
        public TextView ProductQuntityTextView;
        public TextView ProductPurchassTextView;
        public TextView ProductSaleTextView;
        public TextView ProductDateextView;
        public TextView ProductLowInventory;


        public ViewHolder(View itemView) {
            super(itemView);

            imageView = (ImageView) itemView.findViewById(R.id.poductimageview);

            ProductimageNameTextView = (TextView) itemView.findViewById(R.id.productnameview);
            ProductQuntityTextView = (TextView) itemView.findViewById(R.id.productquantityview);
            ProductPurchassTextView = (TextView) itemView.findViewById(R.id.productpurchaseview);
            ProductSaleTextView = (TextView) itemView.findViewById(R.id.productsaleviw);
            ProductDateextView = (TextView) itemView.findViewById(R.id.productdatview);
            ProductLowInventory = (TextView) itemView.findViewById(R.id.produlowinventory);
        }
    }

}
