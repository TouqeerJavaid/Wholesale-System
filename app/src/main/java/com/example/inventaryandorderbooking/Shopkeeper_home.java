package com.example.inventaryandorderbooking;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.inventaryandorderbooking.Model.ProductCart;
import com.example.inventaryandorderbooking.Model.Productinfo;
import com.example.inventaryandorderbooking.Model.shopkeeper_home_products;
import com.example.inventaryandorderbooking.Static.Common;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nex3z.notificationbadge.NotificationBadge;

import java.util.ArrayList;
import java.util.List;

public class Shopkeeper_home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
//
    NotificationBadge notificationBadge;
    private RecyclerView mRecyclerView;
    private shopkeeper_home_products mAdapter;
    private ProgressBar mProgressCircle;
    String d;
    private DatabaseReference mDatabaseRef,cartRef;
    private List<Productinfo> mUploads;
    private static final String TAG = "";
    private ListView Products;
    ImageView cartImage;
   // private ListView list_view_shopkeeper_products;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopkeeper_home);


        mRecyclerView = findViewById(R.id.products_show_shopkeepers);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
        //  list_view_shopkeeper_products = (ListView)findViewById(R.id.products_show_shopkeepers);


        mUploads = new ArrayList<>();
        notificationBadge = (NotificationBadge) findViewById(R.id.notificationbadge);
        cartImage = (ImageView) findViewById(R.id.cardIcon);
        if(Common.currentUser.getId() != null) {
            cartRef = FirebaseDatabase.getInstance().getReference("Cart").child(Common.currentUser.getId());


            cartRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    notificationBadge.setText(String.valueOf(dataSnapshot.getChildrenCount()));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }

        cartImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ProductCartActivity.class);
                startActivity(intent);

            }
        });


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


                mAdapter = new shopkeeper_home_products(Shopkeeper_home.this ,mUploads );
                mRecyclerView.setAdapter(mAdapter);
                System.out.println(mUploads);
                // mProgressCircle.setVisibility(View.INVISIBLE);
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(Shopkeeper_home.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                // mProgressCircle.setVisibility(View.INVISIBLE);
            }
        });


        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Home");
        setSupportActionBar(toolbar);
       /* FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
/*        Products.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });*/


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START))
        {
            drawer.closeDrawer(GravityCompat.START);
        } else
            {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.shopkeeper_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    //@Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        // if (id == R.id.nav_home) {
        //   // Handle the camera action
        //}
        if (id == R.id.nav_home) {

        } else if (id == R.id.nav_cart) {

            Intent intent = new Intent(this, ProductCartActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_orders) {

            Intent intent = new Intent(this, ShopkeeperOrderHistory.class);
            startActivity(intent);

        } else if (id == R.id.nav_signout) {

            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }// else if (id == R.id.nav_send) {

        // }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }




    @Override
    protected void onResume() {
        super.onResume();


        if(Common.currentUser.getId() != null) {
            cartRef = FirebaseDatabase.getInstance().getReference("Cart").child(Common.currentUser.getId());


            cartRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    notificationBadge.setText(String.valueOf(dataSnapshot.getChildrenCount()));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}

