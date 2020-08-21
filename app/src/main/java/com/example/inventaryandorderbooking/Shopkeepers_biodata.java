package com.example.inventaryandorderbooking;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.inventaryandorderbooking.Model.User;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Shopkeepers_biodata extends AppCompatActivity {
    EditText s_name , s_email , s_password,s_phone , s_city , s_address;
    Button s_btnsignUp;
    private FirebaseAuth s_Auth;
    String uid;
    String today;
    Calendar calander;
    SimpleDateFormat simpledateformat;
    String Date;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopkeepers_biodata);

        java.util.Date date = Calendar.getInstance().getTime();
        // Display a date in day, month, year format
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        today = formatter.format(date);


        s_name = (EditText) findViewById(R.id.s_name);
        s_email = (EditText) findViewById(R.id.s_email);
        s_password = (EditText) findViewById(R.id.s_paswword);
        s_phone = (EditText) findViewById(R.id.s_phone);
        s_city = (EditText) findViewById(R.id.s_city);
        s_address = (EditText) findViewById(R.id.s_address);

        s_btnsignUp = (Button)findViewById(R.id.s_add);
        s_Auth = FirebaseAuth.getInstance();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();

        s_btnsignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                createuser();

            }
        });
    }
    private void createuser() {
        final String Name = s_name.getText().toString();
        final String Email = s_email.getText().toString();
        final String Password = s_password.getText().toString();
        final String Phone = s_phone.getText().toString();
        final String City = s_city.getText().toString();
        final String Address = s_address.getText().toString();


        if (Email.isEmpty()){
            s_email.setError("Email Required");
            s_email.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(Email).matches()){
            s_email.setError("Please Enter Valid Email");
            s_email.requestFocus();
            return;
        }

        if(Password.length()<6){
            s_password.setError("Minimum Six characters");
            s_password.requestFocus();
            return;
        }

        if (Phone.isEmpty()){
            s_phone.setError("Email Required");
            s_phone.requestFocus();
            return;
        }

        if (City.isEmpty()){
            s_city.setError("Email Required");
            s_city.requestFocus();
            return;
        }

        if (Address.isEmpty()){
            s_address.setError("Email Required");
            s_address.requestFocus();
            return;
        }

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.show();
        progressDialog.setMessage("Please Wait...");

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myref = database.getReference("Shopkeeper");

        myref.orderByChild("email").equalTo(Email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    progressDialog.dismiss();
                    Toast.makeText(Shopkeepers_biodata.this, "Email id already exist", Toast.LENGTH_SHORT).show();
                }
                else {
                    User user = new User(
                            Name,
                            Email,
                            Password,
                            Phone,
                            City,
                            Address,
                            today

                    );

                    FirebaseDatabase.getInstance().getReference("Shopkeeper")
                            .push()
                            .setValue(user);
                    progressDialog.dismiss();
                    Toast.makeText(Shopkeepers_biodata.this, "User Added Successfully", Toast.LENGTH_SHORT).show();
                    Intent ii = new Intent(Shopkeepers_biodata.this , add_shopkeepers.class);
                    startActivity(ii);

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




    }

    private void checkuserexistance() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.show();
        progressDialog.setMessage("Please Wait...");

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("Shopkeeper").child(uid);
        // System.out.p
        table_user.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    User user = snapshot.getValue(User.class);
                    System.out.println(user.getEmail());
                    if (user.getEmail().equals(s_email.getText().toString())){
                        //Toast.makeText(Shopkeepers_biodata.this, "User already exist", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        s_email.setError("User Already exist");
                        s_email.requestFocus();
                        return;
                    }
                    else
                    createuser();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


}
}
