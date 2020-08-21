package com.example.inventaryandorderbooking.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.inventaryandorderbooking.GenerateInvoice;
import com.example.inventaryandorderbooking.Model.OrderInfo;
import com.example.inventaryandorderbooking.Model.ProductCart;
import com.example.inventaryandorderbooking.Model.User;
import com.example.inventaryandorderbooking.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.squareup.picasso.Picasso;

import java.util.List;


public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.OrderViewHolder>{
    Context context;
    List<User> orderInfoList;
    List<String> orderListKey;
    List<String> orderDateList;
    DatabaseReference orderRef;



    public OrderListAdapter(Context context, List<User> orderInfoList, List<String> orderListKey, List<String> orderDateList) {
        this.context = context;
        this.orderInfoList = orderInfoList;
        this.orderListKey = orderListKey;
        orderRef = FirebaseDatabase.getInstance().getReference("Orders");
        this.orderDateList = orderDateList;

    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.single_order_item_layout, viewGroup, false);
        return new OrderViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final OrderViewHolder orderViewHolder, final int i) {
//        Picasso.get().load(orderInfoList.get(i).getImageurl()).into(orderViewHolder.foodImage);
//        orderViewHolder.productName.setText("Product Name :"+orderInfoList.get(i).getProduct_name());
//        orderViewHolder.productPrice.setText("Product Price : "+orderInfoList.get(i).getProduct_sale_price());
//        orderViewHolder.productQty.setText("Product Qty :"+orderInfoList.get(i).getUserSelectedQty());
//        Toast.makeText(context, ""+orderInfoList.get(i).getName(), Toast.LENGTH_SHORT).show();
         orderViewHolder.productName.setText("Shopkeeper Name : " + orderInfoList.get(i).getName());
        orderViewHolder.shopekeeperPhone.setText("Shopkeeper Phone No : "+orderInfoList.get(i).getPhone());
        orderViewHolder.shopekeeperAddress.setText("Shopkeeper Address : "+orderInfoList.get(i).getAddress());
        orderViewHolder.shopkeeperEmail.setText("Shopkeeper Email : "+orderInfoList.get(i).getEmail());
        orderViewHolder.orderDate.setText("Order Date : "+orderDateList.get(i));

        orderViewHolder.confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Order Confirm"+orderInfoList.get(i).getId(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, GenerateInvoice.class);
                intent.putExtra("orderUId", orderListKey.get(i));
                context.startActivity(intent);

            }
        });
//
        orderViewHolder.cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Order Cancel", Toast.LENGTH_SHORT).show();


                orderRef.child(orderListKey.get(i)).removeValue();
                orderInfoList.remove(i);
                OrderListAdapter.this.notifyDataSetChanged();


//
            }
        });
    }


 

    @Override
    public int getItemCount() {
        return orderInfoList.size();
    }

    public class OrderViewHolder extends RecyclerView.ViewHolder {

        TextView productName, orderDate, productQty, shopkeeperName,shopkeeperEmail, shopekeeperAddress,shopekeeperCity, shopekeeperPhone;
        ImageView foodImage;
        Button confirmBtn, cancelBtn;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);

            productName = (TextView) itemView.findViewById(R.id.orderName);
//            productPrice = (TextView) itemView.findViewById(R.id.orderPrice);
//            productQty = (TextView) itemView.findViewById(R.id.orderQty);
            orderDate = (TextView) itemView.findViewById(R.id.orderDate);
            shopekeeperAddress = (TextView) itemView.findViewById(R.id.orderAddress);
            shopkeeperEmail = (TextView) itemView.findViewById(R.id.orderCEmail);
            shopekeeperPhone = (TextView) itemView.findViewById(R.id.orderPhone);
            shopekeeperCity = (TextView) itemView.findViewById(R.id.orderCCity);

//            foodImage = (ImageView) itemView.findViewById(R.id.orderImage);

            confirmBtn = (Button) itemView.findViewById(R.id.orderConfirmBtn);
            cancelBtn = (Button) itemView.findViewById(R.id.cancelOrderBtn);
        }
    }
}
