package com.example.inventaryandorderbooking;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.inventaryandorderbooking.Model.Productinfo;
import com.example.inventaryandorderbooking.Static.Common;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class Wholesaler_home extends AppCompatActivity {

    FirebaseAuth w_Auth;
    FirebaseUser user;
    public int count = 0;
    TextView low_inventory_alerts;
    public static final String TAG = "";
    public  ArrayList<String> low_product_array=new ArrayList<String>();

    Button gotoproducts,addshopkeepers, ordersBtn, salesRecord;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wholesaler_home);

     //   logout = (Button)findViewById(R.id.logout);
        addshopkeepers = (Button)findViewById(R.id.addshopkeepers1);
        gotoproducts = (Button)findViewById(R.id.product_goto);
        ordersBtn = (Button) findViewById(R.id.orders);
        salesRecord = (Button) findViewById(R.id.sales_record);
        low_inventory_alerts = (TextView)findViewById(R.id.low_inventory_alerts);
        addshopkeepers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(Wholesaler_home.this , add_shopkeepers.class);
                startActivity(i);
            }
        });
        gotoproducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Wholesaler_home.this , add_products.class);
                startActivity(i);
            }
        });


        ordersBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), OrdersActivity.class);
                startActivity(intent);
            }
        });

        salesRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SalesRecordActivity.class);
                startActivity(intent);
            }
        });
        final FirebaseDatabase database1 = FirebaseDatabase.getInstance();
        final DatabaseReference myref = database1.getReference("Products");
        myref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    int qty = 0;
                    int low = 0;
                    Productinfo product = snapshot.getValue(Productinfo.class);
                     if(Integer.parseInt(product.getProduct_quantity())<=0){
                         //System.out.println(product.getProduct_name());
                     }
                     if (Integer.parseInt(product.getProduct_low_inventory_alerts())<=0){
                         //System.out.println(product.getProduct_name());
                     }
                    if (Integer.parseInt(product.getProduct_low_inventory_alerts())>=Integer.parseInt(product.getProduct_quantity())){
                        System.out.println(product.getProduct_name());
                        System.out.println(product.getProduct_quantity());
                        System.out.println(product.getProduct_low_inventory_alerts());
                        count++;
                        low_product_array.add(product.getProductId());
                        System.out.println(""+count);

                    }

                   //
                   //
                    //call_me(qty , low);
                   //  if(qty!=0 && low!=0);


                }
                System.out.println(+count);
                low_inventory_alerts.setText(String.valueOf(count));
                Intent intent = new Intent(Wholesaler_home.this , Low_inventory.class);
                intent.putStringArrayListExtra("low_inventory_key", low_product_array);
                //startActivity(intent);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
               // progressDialog.dismiss();
            }
        });
        System.out.println(low_product_array);

      /*  logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wAuth.signOut();
                finish();
                Intent i = new Intent(Wholesaler_home.this , MainActivity.class);
                startActivity(i);
            }
        });*/

        //getting the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarwholesalerhome);

        //setting the title
        String a="Dashboard";
        toolbar.setTitle(a);

        //placing toolbar in place of actionbar
        setSupportActionBar(toolbar);
    }

    private void callme() {
        System.out.println("Thank god  "+low_product_array);
        Intent intent = new Intent(Wholesaler_home.this , Low_inventory.class);
        intent.putStringArrayListExtra("low_inventory_key", low_product_array);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.wholesaler_toolbar_menu, menu);
        return true;


}
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){
            case R.id.settings_menu:
                Intent ii = new Intent(Wholesaler_home.this , Account_settings.class);
                startActivity(ii);
                break;
            case R.id.about_menu:
                Toast.makeText(this, "About_app", Toast.LENGTH_SHORT).show();
                break;
            case R.id.signout_menu:
                FirebaseAuth.getInstance().signOut();
                finish();
                Intent i = new Intent(Wholesaler_home.this , MainActivity.class);
                startActivity(i);
                Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
                break;
        }

        return true;
    }
    public void low_inventory_activity(View view) {
        callme();

    }
}
