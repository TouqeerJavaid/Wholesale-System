
package com.example.inventaryandorderbooking;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.inventaryandorderbooking.Model.Productinfo;
import com.example.inventaryandorderbooking.Model.ProductsInfoAdapter1;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class add_products extends AppCompatActivity {

    FirebaseAuth p_auth;
    private RecyclerView mRecyclerView;
    private ProductsInfoAdapter1 mAdapter;

    private ProgressBar mProgressCircle;
    String d;
    private DatabaseReference mDatabaseRef;
    private List<Productinfo> mUploads;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_products);

        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);


        // mProgressCircle = findViewById(R.id.progress_circle);

        mUploads = new ArrayList<>();

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Products");

        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Productinfo upload = postSnapshot.getValue(Productinfo.class);
                    mUploads.add(upload);
                  //  Log.i("v");
                    //System.out.println(upload.getImageurl());
                }

               mAdapter = new ProductsInfoAdapter1(add_products.this, mUploads);
                mRecyclerView.setAdapter(mAdapter);
                System.out.println(mUploads);
               // mProgressCircle.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(add_products.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
               // mProgressCircle.setVisibility(View.INVISIBLE);
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbaraddproducts);
        String a="Products";
        toolbar.setTitle(a);
        setSupportActionBar(toolbar);
        if (getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }



        FloatingActionButton add_shopkeepers_float_button= findViewById(R.id.add_shopkeepers_float_button);
        add_shopkeepers_float_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(add_products.this , Products_biodata.class);
                startActivity(i);
            }
        });

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                Intent iii = new Intent(add_products.this , Wholesaler_home.class);
                startActivity(iii);
                finish();
                break;
            case R.id.settings_menu:
                Intent ii = new Intent(add_products.this , Account_settings.class);
                startActivity(ii);
                break;
            case R.id.about_menu:
                Toast.makeText(this, "About_app", Toast.LENGTH_SHORT).show();
                break;
            case R.id.signout_menu:
                p_auth.signOut();
                finish();
                Intent i = new Intent(add_products.this , MainActivity.class);
                startActivity(i);

        }

        return true;
    }
}
