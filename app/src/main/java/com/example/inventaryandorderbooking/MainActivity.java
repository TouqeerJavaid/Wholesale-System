package com.example.inventaryandorderbooking;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;


import com.example.inventaryandorderbooking.Model.User;
import com.example.inventaryandorderbooking.Static.Common;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {


    EditText e1,e2;
    FirebaseAuth lauth;
    Button btnsignin;

    RadioGroup g;

    RadioButton wholesaler , shopkeepeer;
    String lastChecked = "wholesaler";   //This is the Flag which i m using

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        e1 = (EditText)findViewById(R.id.lemail);
        e2 = (EditText)findViewById(R.id.lpass);
        g = (RadioGroup)findViewById(R.id.select);

        btnsignin = (Button)findViewById(R.id.btnsignin);

        wholesaler = (RadioButton)findViewById(R.id.wholesaler);
        shopkeepeer = (RadioButton)findViewById(R.id.shopkeepers);



        lauth = FirebaseAuth.getInstance();

        btnsignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (g!=null){
                    if(wholesaler.isChecked()){
                        w_login();
                    }
                    else if(shopkeepeer.isChecked()){
                        s_login();
                    }
                }
            }
        });
    }

    public void signup(View view) {
        Intent i = new Intent(MainActivity.this , SignUp.class);
        startActivity(i);
    }



    public void s_login(){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...");
        final String e3 = e1.getText().toString();
        final String e4 = e2.getText().toString();
        final FirebaseDatabase database1 = FirebaseDatabase.getInstance();
        final DatabaseReference myref = database1.getReference("Wholesaler");
        progressDialog.show();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("Shopkeeper");
       // System.out.println(id);

        table_user.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int count = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);

                    if (user.getEmail().equals(e3) && user.getPassword().equals(e4)){
                        Common.currentUser = user;
                        Common.currentUser.setId(snapshot.getKey());
                        Log.d("userId", ""+user.getId());
                        progressDialog.dismiss();
                        Toast.makeText(MainActivity.this, "Wellcome", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(MainActivity.this,Shopkeeper_home.class);
                        startActivity(i);
                        finish();
                    }
                   else
                   {
                        progressDialog.dismiss();
                        Toast.makeText(MainActivity.this, "Check your email and passsword", Toast.LENGTH_SHORT).show();
                    }

                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressDialog.dismiss();
            }
        });
    }
    public void w_login(){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...");
        String e3 = e1.getText().toString();
        String e4 = e2.getText().toString();
        progressDialog.show();
        lauth.signInWithEmailAndPassword(e3 , e4).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
              if (task.isSuccessful()){
                  progressDialog.dismiss();
                  Intent i = new Intent(MainActivity.this,Wholesaler_home.class);
                  startActivity(i);
                  finish();
                  Toast.makeText(MainActivity.this, "Logged in successfully", Toast.LENGTH_SHORT).show();
              }
              else {
                  progressDialog.dismiss();
                  Toast.makeText(MainActivity.this, "Check you email and password", Toast.LENGTH_SHORT).show();
              }
            }
        });
    }
}