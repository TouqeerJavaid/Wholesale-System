package com.example.inventaryandorderbooking.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.inventaryandorderbooking.Model.OrderHistoryInfo;
import com.example.inventaryandorderbooking.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;


public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.OrderViewHolder>{
    Context context;
    List<OrderHistoryInfo> orderInfoList;
    DatabaseReference orderRef;

    public OrderHistoryAdapter(Context context, List<OrderHistoryInfo> orderInfoList) {
        this.context = context;
        this.orderInfoList = orderInfoList;
        orderRef = FirebaseDatabase.getInstance().getReference("Orders");
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.single_order_item_layout_history, viewGroup, false);
        return new OrderViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final OrderViewHolder orderViewHolder, final int i) {
//
//        Toast.makeText(context, ""+orderInfoList.get(i).getTotalPrice(), Toast.LENGTH_SHORT).show();
        orderViewHolder.orderDate.setText("Order Date : "+orderInfoList.get(i).getOrderDate());
        orderViewHolder.orderAmount.setText("Order Amount : "+orderInfoList.get(i).getTotalPrice());
    }


 

    @Override
    public int getItemCount() {
        return orderInfoList.size();
    }

    public class OrderViewHolder extends RecyclerView.ViewHolder {

        TextView orderDate, orderAmount;


        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);


            orderAmount = (TextView) itemView.findViewById(R.id.totalAmountTv);
            orderDate = (TextView) itemView.findViewById(R.id.orderDateTv);
        }
    }
}
