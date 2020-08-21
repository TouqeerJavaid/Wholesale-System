package com.example.inventaryandorderbooking.Model;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.inventaryandorderbooking.R;
import com.example.inventaryandorderbooking.add_products;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.List;

public class ProductsInfoAdapter1 extends RecyclerView.Adapter<ProductsInfoAdapter1.ViewHolder> {
    Context context;
    List<Productinfo> MainImageUploadInfoList;

    DatabaseReference productRef;

    public ProductsInfoAdapter1(Context context, List<Productinfo> TempList) {

        this.MainImageUploadInfoList = TempList;

        this.context = context;
        productRef = FirebaseDatabase.getInstance().getReference("Products");
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.products_list, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        final Productinfo UploadInfo = MainImageUploadInfoList.get(position);
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

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialog(MainImageUploadInfoList.get(position));
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                new AlertDialog.Builder(context)
                        .setTitle("Delete Product")
                        .setMessage("Are you sure you want to delete dis entry?")

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Continue wif delete operation
                                productRef.child(MainImageUploadInfoList.get(position).getProductId()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            Toast.makeText(context, "Product Deleted!", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(context, add_products.class );
                                            context.startActivity(intent);
                                            ((Activity)context).finish();
                                        }
                                    }
                                });

                            }
                        })

                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton("No", null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();

                return false;
            }
        });
        
    }

    private void showAlertDialog(final Productinfo productinfo) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Update Product");
        LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );

        View formView = inflater.inflate(R.layout.single_update_product_layout, null);

        final EditText pname = (EditText) formView.findViewById(R.id.p_name);
        final EditText pquantity = (EditText) formView.findViewById(R.id.p_qunntity);
        final EditText ppurchase = (EditText) formView.findViewById(R.id.p_purchase);
        final EditText psale = (EditText) formView.findViewById(R.id.p_sale);
        final EditText plowinventory = (EditText) formView.findViewById(R.id.p_low_inventory_alerts);
        Button padd = (Button) formView.findViewById(R.id.p_add);
        ImageView pimage = (ImageView) formView.findViewById(R.id.p_image);
         String currentdate = "";


        Picasso.get().load(productinfo.getImageurl()).into(pimage);
        pname.setText(productinfo.getProduct_name());
        pquantity.setText(productinfo.getProduct_quantity());
        ppurchase.setText(productinfo.getProduct_purchase_price());
        psale.setText(productinfo.getProduct_sale_price());
        plowinventory.setText(productinfo.getProduct_low_inventory_alerts());
        builder.setView(formView);
        final AlertDialog alertDialog = builder.show();
        padd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               if(!TextUtils.isEmpty(pname.getText().toString()) && !TextUtils.isEmpty(pquantity.getText().toString()) && !TextUtils.isEmpty(ppurchase.getText().toString()) && !TextUtils.isEmpty(psale.getText().toString()) && !TextUtils.isEmpty(plowinventory.getText().toString())){
                   HashMap<String , Object> update = new HashMap<>();
                   update.put("product_name", pname.getText().toString());
                   update.put("product_quantity", pquantity.getText().toString());
                   update.put("product_purchase_price", ppurchase.getText().toString());
                   update.put("product_sale_price", psale.getText().toString());
                   update.put("product_low_inventory_alerts", plowinventory.getText().toString());

                   productRef.child(productinfo.getProductId()).updateChildren(update).addOnCompleteListener(new OnCompleteListener<Void>() {
                       @Override
                       public void onComplete(@NonNull Task<Void> task) {
                           if(task.isSuccessful()){
                               Toast.makeText(context, "Product Updated!", Toast.LENGTH_SHORT).show();
                                alertDialog.dismiss();
                                Intent intent = new Intent(context, add_products.class );
                                context.startActivity(intent);
                               ((Activity)context).finish();
                           }
                       }
                   });
               }

            }
        });



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
