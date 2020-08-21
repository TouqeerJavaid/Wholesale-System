package com.example.inventaryandorderbooking;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.inventaryandorderbooking.Model.Shopkeeperes_info;
import com.example.inventaryandorderbooking.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class add_shopkeepers extends AppCompatActivity {


    private ListView sk_list;
    DatabaseReference databaseReference, shopkeeperRef;

    List<User>shopkeeperlist;

    FirebaseAuth sAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_shopkeepers);

        sk_list =(ListView)findViewById(R.id.sk_list) ;

        databaseReference = FirebaseDatabase.getInstance().getReference("Shopkeeper");
        shopkeeperRef = FirebaseDatabase.getInstance().getReference("Shopkeeper");
        shopkeeperlist = new ArrayList<>();

        //getting the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbaraddshopkeepers);
        String a="Shopkeepers";
        toolbar.setTitle(a);
        setSupportActionBar(toolbar);
        if (getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }



        //float add button
        FloatingActionButton add_shopkeepers_float_button= findViewById(R.id.add_shopkeepers_float_button);
        add_shopkeepers_float_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(add_shopkeepers.this , Shopkeepers_biodata.class);
                startActivity(i);
            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater( );
        menuInflater.inflate(R.menu.wholesaler_toolbar_menu, menu);
        return true;
    }

    @Override
    //Shopkeepers Retrievinng//
    protected void onStart() {
        super.onStart();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                shopkeeperlist.clear();
                for (DataSnapshot shopkeeperssnapshot : dataSnapshot.getChildren()){
                    User shopkeepers = shopkeeperssnapshot.getValue(User.class);
                    shopkeepers.setId(shopkeeperssnapshot.getKey());
                    shopkeeperlist.add(shopkeepers);
                }
                Shopkeeperes_info shopkeeperinfo = new Shopkeeperes_info(add_shopkeepers.this , shopkeeperlist);
                sk_list.setAdapter(shopkeeperinfo);
                sk_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        showAlertDialog(shopkeeperlist.get(position));
                    }
                });

                sk_list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                        showDeleteAlertDialog(shopkeeperlist.get(position));
                        return false;
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void showDeleteAlertDialog(final User user) {
        Toast.makeText(this, ""+user.getId(), Toast.LENGTH_SHORT).show();
//        Toast.makeText(add_shopkeepers.this, ""+position, Toast.LENGTH_SHORT).show();
        new AlertDialog.Builder(add_shopkeepers.this)
                .setTitle("Delete Shopkeeper")
                .setMessage("Are you sure you want to delete dis entry?")

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Continue wif delete operation
                        shopkeeperRef.child(user.getId()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(add_shopkeepers.this, "Shopkeeper Deleted!", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(), add_shopkeepers.class );
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        });

                    }
                })

                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton("No", null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();


    }

    private void showAlertDialog(final User user) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Update Shopkeeper Information!");
        LayoutInflater inflater = (LayoutInflater) this.getSystemService( Context.LAYOUT_INFLATER_SERVICE );

        View formView = inflater.inflate(R.layout.single_shopkeepr_layout, null);

        final EditText s_name , s_email , s_password,s_phone , s_city , s_address;
        Button s_btnsignUp;

        s_name = (EditText) formView.findViewById(R.id.s_name);
        s_email = (EditText) formView.findViewById(R.id.s_email);
        s_password = (EditText) formView.findViewById(R.id.s_paswword);
        s_city = (EditText) formView.findViewById(R.id.s_city);
        s_address = (EditText) formView.findViewById(R.id.s_address);
        s_phone = (EditText ) formView.findViewById(R.id.s_phone);

        Button updateBtn = (Button) formView.findViewById(R.id.s_add);

        s_name.setText(user.getName());
        s_email.setText(user.getEmail());
        s_password.setText(user.getPassword());
        s_city.setText(user.getCity());
        s_address.setText(user.getAddress());

        builder.setView(formView);
        final AlertDialog alertDialog = builder.show();


        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(s_name.getText().toString()) && !TextUtils.isEmpty(s_email.getText().toString()) && !TextUtils.isEmpty(s_password.getText().toString())
                && !TextUtils.isEmpty(s_city.getText().toString()) && !TextUtils.isEmpty(s_address.getText().toString())){
                    HashMap<String , Object> update = new HashMap<>();
                    update.put("name", s_name.getText().toString());
                    update.put("city", s_city.getText().toString());
                    update.put("address", s_address.getText().toString());
                    update.put("email", s_email.getText().toString());
                    update.put("password", s_password.getText().toString());
                    update.put("phone", s_phone.getText().toString());

                    shopkeeperRef.child(user.getId()).updateChildren(update).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(add_shopkeepers.this, "Shopkeeper Information Updated!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), add_shopkeepers.class);
                                startActivity(intent);
                                finish();
                            }
                        }
                    });


                }else{
                    Toast.makeText(add_shopkeepers.this, "Kindly enter the required information!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                Intent iii = new Intent(add_shopkeepers.this , Wholesaler_home.class);
                startActivity(iii);
                finish();
                break;
            case R.id.settings_menu:
                Intent ii = new Intent(add_shopkeepers.this , Account_settings.class);
                startActivity(ii);
                break;
            case R.id.about_menu:
                Toast.makeText(this, "About_app", Toast.LENGTH_SHORT).show();
                break;
            case R.id.signout_menu:
                sAuth.signOut();
                finish();
                Intent i = new Intent(add_shopkeepers.this , MainActivity.class);
                startActivity(i);

        }

        return true;
    }
}