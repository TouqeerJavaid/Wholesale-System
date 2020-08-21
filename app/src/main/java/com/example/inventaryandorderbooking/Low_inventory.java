package com.example.inventaryandorderbooking;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.inventaryandorderbooking.Model.Productinfo;
import com.example.inventaryandorderbooking.Model.User;
import com.example.inventaryandorderbooking.Model.low_inventory_adapter;
import com.example.inventaryandorderbooking.Static.Common;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Low_inventory extends AppCompatActivity {

    FirebaseAuth p_auth;
    private RecyclerView mRecyclerView;
    private low_inventory_adapter mAdapter;
    private ProgressBar mProgressCircle;
    String d;
    private DatabaseReference mDatabaseRef;
    private List<Productinfo> mUploads;
    ArrayList<String> low_inventory_products2 = new ArrayList<String>();
    DatabaseReference low_inventory_db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_low_inventory);
        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
        low_inventory_products2 = getIntent().getStringArrayListExtra("low_inventory_key");
        low_inventory_db = FirebaseDatabase.getInstance().getReference("Products");


       // System.out.println("Again thank god"+low_inventory_products2);


        // mProgressCircle = findViewById(R.id.progress_circle);


        mUploads = new ArrayList<>();

       /* mDatabaseRef = FirebaseDatabase.getInstance().getReference("Products");

        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Productinfo upload = postSnapshot.getValue(Productinfo.class);
                    mUploads.add(upload);
                    //  Log.i("v");
                    //System.out.println(upload.getImageurl());
                }

                mAdapter = new low_inventory_adapter(Low_inventory.this, mUploads);
                mRecyclerView.setAdapter(mAdapter);
                System.out.println(mUploads);
                // mProgressCircle.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(Low_inventory.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                // mProgressCircle.setVisibility(View.INVISIBLE);
            }
        });*/

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbaraddproducts);
        String a="Products";
        toolbar.setTitle(a);
        setSupportActionBar(toolbar);
        if (getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }



        /*FloatingActionButton add_shopkeepers_float_button= findViewById(R.id.add_shopkeepers_float_button);
        add_shopkeepers_float_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Low_inventory.this , Products_biodata.class);
                startActivity(i);
            }
        });*/


        for (int i=0; i<low_inventory_products2.size(); i++) {
            final String product_id = (low_inventory_products2.get(i));
            System.out.println(product_id);
            final FirebaseDatabase database = FirebaseDatabase.getInstance();
            final DatabaseReference table_product = database.getReference("Products");
            // System.out.println(id);
            table_product.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Productinfo product = snapshot.getValue(Productinfo.class);
                        if (product.getProductId().equals(product_id)){
                            Productinfo upload = snapshot.getValue(Productinfo.class);
                            mUploads.add(upload);

                        }
                        mAdapter = new low_inventory_adapter(Low_inventory.this, mUploads);
                        mRecyclerView.setAdapter(mAdapter);
                        System.out.println(mUploads);
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                   // progressDialog.dismiss();
                }
            });
        }

    }




    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                Intent iii = new Intent(Low_inventory.this , Wholesaler_home.class);
                startActivity(iii);
                finish();
                break;
            case R.id.settings_menu:
                Intent ii = new Intent(Low_inventory.this , Account_settings.class);
                startActivity(ii);
                break;
            case R.id.about_menu:
                Toast.makeText(this, "About_app", Toast.LENGTH_SHORT).show();
                break;
            case R.id.signout_menu:
                p_auth.signOut();
                finish();
                Intent i = new Intent(Low_inventory.this , MainActivity.class);
                startActivity(i);

        }

        return true;
    }
}
