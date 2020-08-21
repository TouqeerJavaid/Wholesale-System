package com.example.inventaryandorderbooking;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.inventaryandorderbooking.Model.Productinfo;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.squareup.picasso.Picasso;import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Products_biodata extends AppCompatActivity {

    // Folder path for Firebase Storage.
    String Storage_Path = "All_Image_Uploads/";
    // Root Database Name for Firebase Database.
    String Database_Path = "All_Image_Uploads_Database";



    String [] spinnerlist = {"Pcs" ,  "Kg" ,"Meter" , "Leter"  , "Pack"};
    EditText pname;
    EditText pquantity;
    EditText ppurchase;
    EditText psale;
    EditText plowinventory;
    Button padd ,pchooseimage;
    ImageView pimage;
    private String currentdate;


    private FirebaseAuth w_Auth;
    private String Wholsaler_id = "";
    private static  final int PICK_IMAGE= 100;
    private Uri mImageUri;

    StorageReference mstorageReference;
    DatabaseReference mdatabaseReference;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_biodata);



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_p_d);
        //setting the title
        String a="Add Product";

        toolbar.setTitle(a);
        //placing toolbar in place of actionbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        java.util.Date date = Calendar.getInstance().getTime();
        // Display a date in day, month, year format
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        currentdate = formatter.format(date);

        mstorageReference = FirebaseStorage.getInstance().getReference("Products");
        mdatabaseReference = FirebaseDatabase.getInstance().getReference("Products");

        // Spinner mySpin = (Spinner) findViewById(R.id.select_measuringunit);
        //measuring unit
        // Spinner spinner = findViewById(R.id.select_measuringunit);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.numbers, android.R.layout.simple_spinner_item );


        pname = (EditText) findViewById(R.id.p_name);
        pimage = (ImageView) findViewById(R.id.p_image);
        pquantity = (EditText) findViewById(R.id.p_qunntity);
        ppurchase = (EditText) findViewById(R.id.p_purchase);
        psale = (EditText) findViewById(R.id.p_sale);
        plowinventory = (EditText)findViewById(R.id.p_low_inventory_alerts) ;
        padd = (Button) findViewById(R.id.p_add);
        //pchooseimage = (Button)findViewById(R.id.pchoseimg);
        //  selectImage = (ImageView)findViewById(R.id.p_image);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();



        pimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                opengallery();
            }
        });

        padd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                existance();
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.wholesaler_toolbar_menu, menu);
        return true;


    }
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){
            case R.id.settings_menu:
                Intent ii = new Intent(Products_biodata.this , add_products.class);
                startActivity(ii);
                break;
            case R.id.about_menu:
                Toast.makeText(this, "About_app", Toast.LENGTH_SHORT).show();
                break;
            case R.id.signout_menu:
                w_Auth.signOut();
                finish();
                Intent i = new Intent(Products_biodata.this , MainActivity.class);
                startActivity(i);
                Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
                break;
        }

        return true;
    }


    private void opengallery(){
        Intent gallery = new Intent(Intent.ACTION_PICK , MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery , PICK_IMAGE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE ){
            mImageUri = data.getData();
            Picasso.get().load(mImageUri).into(pimage);
            //  pimage.setImageURI(mImageUri);
        }
    }

    private  String getFileExtension( Uri uri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }


    private void existance() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.show();
        progressDialog.setMessage("Please Wait...");

        String Product_name = pname.getText().toString();
        String Quantity = pquantity.getText().toString();
        String Purchase_price = pquantity.getText().toString();
        String Sale_price = pquantity.getText().toString();
        String Low_inventory = plowinventory.getText().toString();

        if (Product_name.isEmpty()) {
            pname.setError("Product Name is Required");
            pname.requestFocus();
            return;

        }

        if (!Quantity.matches("[0-9]+")) {
            pquantity.setError("Numeric value required");
            pquantity.requestFocus();
            return;
        }

        if (!Purchase_price.matches("[0-9]+")) {
            ppurchase.setError("Numeric value required");
            ppurchase.requestFocus();
            return;
        }

        if (!Sale_price.matches("[0-9]+")) {
            psale.setError("Numeric value required");
            psale.requestFocus();
            return;
        }
        if (!Low_inventory.matches("[0-9]+")) {
            plowinventory.setError("Numeric value required");
            plowinventory.requestFocus();
            return;
        }


        mdatabaseReference.orderByChild("product_name").equalTo(Product_name).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    progressDialog.dismiss();
                    Toast.makeText(Products_biodata.this, "Product already exis", Toast.LENGTH_SHORT).show();
                } else {
                    progressDialog.dismiss();
                    upload();
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


    public void upload(){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.show();
        progressDialog.setMessage("Please Wait...");
        if (mImageUri != null) {
            StorageReference filestorage = mstorageReference.child(System.currentTimeMillis()
                    + "." + getFileExtension(mImageUri));
            filestorage.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    //mprogressbar.setPrgress(0);

                                }
                            }, 5000);
                            Toast.makeText(Products_biodata.this, "Upload success", Toast.LENGTH_SHORT).show();

                            //Uri downloadUrl = taskSnapshot.getDownloadUrl();
                          //  String Imgurl = downloadUrl.toString();


                            Productinfo productinfo = new Productinfo(
                                    taskSnapshot.getDownloadUrl().toString(),
                                    pname.getText().toString(),
                                    pquantity.getText().toString(),
                                    ppurchase.getText().toString(),
                                    psale.getText().toString(),
                                    currentdate,
                                    plowinventory.getText().toString(),"",""

                            );
                            productinfo.setWholesalerId(FirebaseAuth.getInstance().getCurrentUser().getUid());

                            String uploadid = mdatabaseReference.push().getKey();
                            productinfo.setProductId(uploadid);
                            System.out.println(uploadid);
                            mdatabaseReference.child(uploadid).setValue(productinfo);
                            progressDialog.dismiss();
                            Intent ii = new Intent(Products_biodata.this ,add_products.class);
                            startActivity(ii);
                        }
                    })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Products_biodata.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            //mprogressbar.setProgress((int)progress)
                        }
                    });
        }
        else {
            progressDialog.dismiss();
            Toast.makeText(this, "chose Image", Toast.LENGTH_SHORT).show();

        }

    }
}


