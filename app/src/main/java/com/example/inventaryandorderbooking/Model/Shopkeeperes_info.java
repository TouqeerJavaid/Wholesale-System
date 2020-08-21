package com.example.inventaryandorderbooking.Model;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.inventaryandorderbooking.Model.User;
import com.example.inventaryandorderbooking.R;

import java.util.List;

public class Shopkeeperes_info extends ArrayAdapter<User> {
    private Activity context;
    private List<User>userlist;
    public Shopkeeperes_info(Activity context,List<User>userlist){
        super(context , R.layout.sk_list,userlist);
        this.context = context;
        this.userlist=userlist;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listView = inflater.inflate(R.layout.sk_list, null ,true);
        TextView sk_name = (TextView) listView.findViewById(R.id.sk_name);
        TextView sk_email = (TextView) listView.findViewById(R.id.sk_email);
        TextView sk_password = (TextView) listView.findViewById(R.id.sk_password);
        TextView sk_phone= (TextView) listView.findViewById(R.id.sk_phone);
        TextView sk_city = (TextView) listView.findViewById(R.id.sk_city);
        TextView sk_address= (TextView) listView.findViewById(R.id.sk_address);
        User sk = userlist.get(position);
        sk_name.setText(sk.getName());
        sk_email.setText(sk.getEmail());
        sk_password.setText(sk.getPassword());
        sk_phone.setText(sk.getPhone());
        sk_city.setText(sk.getCity());
        sk_address.setText(sk.getAddress());



        return listView;
    }


}
