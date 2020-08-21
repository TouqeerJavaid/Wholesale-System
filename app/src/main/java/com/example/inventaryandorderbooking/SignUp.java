package com.example.inventaryandorderbooking;

import com.example.inventaryandorderbooking.Model.User;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.Calendar;

public class SignUp extends AppCompatActivity {


    EditText editname , editemail , editpassword;
    Button btnsignUp;
    private FirebaseAuth mAuth;
    String currentDate;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Calendar calendar = Calendar.getInstance();
        currentDate = DateFormat.getDateInstance().format(calendar.getTime());

        editname = (EditText) findViewById(R.id.editname);
        editemail = (EditText) findViewById(R.id.editemail);
        editpassword = (EditText) findViewById(R.id.editpassword);


        btnsignUp = (Button)findViewById(R.id.btnsignUp);

        mAuth = FirebaseAuth.getInstance();

        btnsignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createuser();
            }
        });
}


    private void createuser() {
        final String Name = editname.getText().toString();
        final String Email = editemail.getText().toString();
        final String Password = editpassword.getText().toString();




        if (Email.isEmpty()){
            editemail.setError("Email Required");
            editemail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(Email).matches()){
            editemail.setError("Please Enter Valid Email");
            editemail.requestFocus();
            return;
        }

        if(Password.length()<6){
            editpassword.setError("Minimum Six characters");
            editpassword.requestFocus();
            return;
        }

        mAuth.createUserWithEmailAndPassword(Email, Password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {


                                    // Sign in success, update UI with the signed-in user's information
                            String id = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            User user = new User(
                                    id,
                                    Name,
                                    Email,
                                    Password,
                                    currentDate

                            );
                            FirebaseDatabase.getInstance().getReference("Wholesaler")
                                    .setValue(user);
                            Toast.makeText(SignUp.this, "User Registered Successfully", Toast.LENGTH_SHORT).show();
                         //   id =  FirebaseAuth.getInstance().getCurrentUser().getUid();
                            // w_id.wholesaler_id_for_relation = id;
                            Intent intent = new Intent();
                            intent = new Intent(getApplicationContext(),Account_settings.class);
                            intent.putExtra("wholesaler_name_settings", editname.getText().toString());
                            intent.putExtra("wholesaler_email_settings", editemail.getText().toString());
                            startActivity(intent);

                        } else {
                            if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                Toast.makeText(getApplicationContext(), "User already Registered", Toast.LENGTH_LONG).show();
                            }
                            else {
                                Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_LONG).show();
                            }
                        }

                        // ...
                    }
                });

    }

}
