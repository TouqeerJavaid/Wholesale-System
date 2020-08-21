package com.example.inventaryandorderbooking;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.inventaryandorderbooking.Model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Account_settings extends AppCompatActivity {

    TextView name_settings , email_settings;
    FirebaseAuth settingsAuth;
    private DatabaseReference mDatabaseRef;
    String name , email;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    String Uemail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_settings);


        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Wholesaler");
   //     name_settings = (TextView)findViewById(R.id.display_name_setting);
        email_settings= (TextView)findViewById(R.id.display_email_setting);

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    String _UID = user.getUid();
                    Uemail= user.getEmail().toString();
                } else {
                    // User is signed out
                }
            }
        };

        System.out.println(Uemail);
        name_settings.setText(Uemail);


        Toolbar toolbar = (Toolbar) findViewById(R.id.account_settings_toolbar);
        String a="Shopkeepers";
        toolbar.setTitle(a);
        setSupportActionBar(toolbar);
        if (getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }




    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.wholesaler_toolbar_menu, menu);
        return true;
    }



    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                Intent iii = new Intent(Account_settings.this , Wholesaler_home.class);
                startActivity(iii);
                finish();
                break;
            case R.id.settings_menu:
                break;
            case R.id.about_menu:
                Toast.makeText(this, "About_app", Toast.LENGTH_SHORT).show();
                break;
            case R.id.signout_menu:
                settingsAuth.signOut();
                finish();
                Intent i = new Intent(Account_settings.this , MainActivity.class);
                startActivity(i);

        }

        return true;
    }

}
