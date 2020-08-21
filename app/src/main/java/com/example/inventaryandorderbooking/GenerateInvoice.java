package com.example.inventaryandorderbooking;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;

import android.widget.TextView;
import android.widget.Toast;

import com.example.inventaryandorderbooking.Model.ProductCart;
import com.example.inventaryandorderbooking.Model.Productinfo;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;


public class GenerateInvoice extends AppCompatActivity {

    TextView subTotalTv, discountTv, grandTotalTv, balanceTv, sName, sPhone, sAddress;
    TableLayout tableLayout;
    DatabaseReference orderRef, productRef, salesRecordRef;
    String selectOrderUId;
    List<ProductCart> orderList;
    List<Productinfo> productinfos;
    Button generateBtn;
    int randomId = 0;

    ScrollView scrollView;
    LinearLayout ll_linear;
    public static int REQUEST_PERMISSIONS = 1;
    boolean boolean_permission;
    boolean boolean_save;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_invoice);

        subTotalTv = (TextView) findViewById(R.id.subTotalTv);
        discountTv = (TextView) findViewById(R.id.discountTv);
        grandTotalTv = (TextView) findViewById(R.id.grandTotalTv);
        balanceTv = (TextView) findViewById(R.id.balanceTv);
        generateBtn = (Button) findViewById(R.id.generateInvoiceBtn);
        sName = (TextView) findViewById(R.id.shopekeeperName);
        sPhone = (TextView) findViewById(R.id.shopekeeperPhone);
        sAddress = (TextView) findViewById(R.id.shopkeeperAddress);

        scrollView = (ScrollView)findViewById(R.id.scrollView);
        ll_linear = (LinearLayout) findViewById(R.id.linearLayout);

        orderList = new ArrayList<>();

        if(getIntent() != null){
            selectOrderUId = getIntent().getStringExtra("orderUId");
            orderRef = FirebaseDatabase.getInstance().getReference("Orders").child(selectOrderUId);
            productRef = FirebaseDatabase.getInstance().getReference("Products");
            salesRecordRef = FirebaseDatabase.getInstance().getReference("SalesRecord");
            orderRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    orderList.clear();
                    for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                        ProductCart productCart = dataSnapshot1.getValue(ProductCart.class);
                        orderList.add(productCart);
                    }
                    generateInvoice(orderList);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        tableLayout = (TableLayout) findViewById(R.id.tableLayout);

//        serialNo = (TextView) findViewById(R.id.textView7);
//        items = (TextView) findViewById(R.id.textView8);
//        brand = (TextView) findViewById(R.id.textView9);
//        price = (TextView) findViewById(R.id.textView10);
//                getSerialNumber = serialNumber.getText().toString();
//                getItems = items.getText().toString();
//                getBrand = brand.getText().toString();
//                getPrice = price.getText().toString();

        fn_permission();

        generateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


//                if (boolean_save) {
//                    Intent intent = new Intent(getApplicationContext(),Screenshot.class);
//                    startActivity(intent);
//
//                }else {
                if (boolean_permission) {
                    Bitmap bitmap1 = loadBitmapFromView(ll_linear, ll_linear.getWidth(), ll_linear.getHeight());
                    saveBitmap(bitmap1);
                } else {

                }
//                }

//
//                takeScreenshot();
                for(int i = 0; i<orderList.size();i++){
                    final int finalI = i;
                    productRef.child(orderList.get(i).getProductId()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Productinfo product = dataSnapshot.getValue(Productinfo.class);
                            HashMap<String, Object> update = new HashMap<>();
                            String value = String.valueOf(Integer.parseInt(product.getProduct_quantity()) - Integer.parseInt(orderList.get(finalI).getUserSelectedQty()));
                            update.put("product_quantity", value);
                            productRef.child(orderList.get(finalI).getProductId()).updateChildren(update);
//                    Toast.makeText(GenerateInvoice.this, "working!"+product.getProduct_quantity(), Toast.LENGTH_SHORT).show();

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                    salesRecordRef.push().setValue(orderList.get(i));
                    orderRef.child(orderList.get(i).getProductId()).removeValue();
                }
                Toast.makeText(GenerateInvoice.this, "Invoice ScreenShort Saved!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(GenerateInvoice.this, Wholesaler_home.class);
                startActivity(intent);
                finish();



            }

        });
    }

    private void generateInvoice(final List<ProductCart> orderList){
        int total = 0;
        int discount = 0;
        int grandTotal = 0;
        int balance = 0;

        for(int i = 0; i<orderList.size(); i++) {

//            final int finalI = i;
//            productRef.child(orderList.get(i).getProductId()).addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//                    Productinfo product  = dataSnapshot.getValue(Productinfo.class);
//                    HashMap<String, Object> update = new HashMap<>();
//                    String value = String.valueOf( Integer.parseInt(product.getProduct_quantity()) - Integer.parseInt(orderList.get(finalI).getUserSelectedQty()));
//                    update.put("product_quantity", value);
//                    productRef.child(orderList.get(finalI).getProductId()).updateChildren(update);
////                    Toast.makeText(GenerateInvoice.this, "working!"+product.getProduct_quantity(), Toast.LENGTH_SHORT).show();
//
//                }
//
//                @Override
//                public void onCancelled(DatabaseError databaseError) {
//
//                }
//            });

            total += Integer.parseInt(orderList.get(i).getProduct_sale_price()) * Integer.parseInt(orderList.get(i).getUserSelectedQty());
            grandTotal = total - discount;
            balance = grandTotal;
            TableRow row = new TableRow(GenerateInvoice.this);
            LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

            row.setLayoutParams(layoutParams);
            layoutParams.setMargins(130, 10, 10, 10);


            TextView t = new TextView(GenerateInvoice.this);

            t.setLayoutParams(layoutParams);
            int index = i;
            index += 1;
            String serial  = String.valueOf(index);
            t.setText(""+index);
            t.setTextSize(TypedValue.COMPLEX_UNIT_SP,18);

            TextView t1 = new TextView(GenerateInvoice.this);
            t1.setLayoutParams(layoutParams);
            t1.setText(orderList.get(i).getProduct_name());
            t1.setTextSize(TypedValue.COMPLEX_UNIT_SP,18);

            TextView t2 = new TextView(GenerateInvoice.this);
            t2.setLayoutParams(layoutParams);
            t2.setText(orderList.get(i).getUserSelectedQty());
            t2.setTextSize(TypedValue.COMPLEX_UNIT_SP,18);

            TextView t3 = new TextView(GenerateInvoice.this);
            t3.setLayoutParams(layoutParams);
            t3.setTextSize(TypedValue.COMPLEX_UNIT_SP,18);

            t3.setText(orderList.get(i).getProduct_sale_price());


            row.addView(t);
            row.addView(t1);
            row.addView(t2);
            row.addView(t3);


            tableLayout.addView(row);
//

//            Toast.makeText(GenerateInvoice.this, "Items Submitted", Toast.LENGTH_SHORT).show();

        }


        subTotalTv.setText("Rs : "+total);
        discountTv.setText("Rs : "+discount);
        grandTotalTv.setText("Rs : "+grandTotal);
        balanceTv.setText("Rs : "+balance);
        sName.setText(orderList.get(0).getShopkeeperName());
        sAddress.setText(orderList.get(0).getShopkeeperAddress());
        sPhone.setText(orderList.get(0).getShopkeeperPhone());

    }

    private void takeScreenshot() {
        Date now = new Date();
        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);

        try {
            // image naming and path  to include sd card  appending name you choose for file
            String mPath = Environment.getExternalStorageDirectory().toString() + "/" + now + ".jpg";

            // create bitmap screen capture
            View v1 = getWindow().getDecorView();
            v1.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
            v1.setDrawingCacheEnabled(false);

            File imageFile = new File(mPath);

            FileOutputStream outputStream = new FileOutputStream(imageFile);
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.flush();
            outputStream.close();

            openScreenshot(imageFile);
        } catch (Throwable e) {
            // Several error may come out with file handling or DOM
            e.printStackTrace();
        }
    }

    private void openScreenshot(File imageFile) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        Uri uri = Uri.fromFile(imageFile);
        intent.setDataAndType(uri, "image/*");
        startActivity(intent);
    }

    private void fn_permission() {
        if ((ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)||
                (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {

            if ((ActivityCompat.shouldShowRequestPermissionRationale(GenerateInvoice.this, android.Manifest.permission.READ_EXTERNAL_STORAGE))) {
            } else {
                ActivityCompat.requestPermissions(GenerateInvoice.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_PERMISSIONS);

            }

            if ((ActivityCompat.shouldShowRequestPermissionRationale(GenerateInvoice.this, Manifest.permission.WRITE_EXTERNAL_STORAGE))) {
            } else {
                ActivityCompat.requestPermissions(GenerateInvoice.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_PERMISSIONS);

            }
        } else {
            boolean_permission = true;


        }
    }
    public void saveBitmap(Bitmap bitmap) {


        File imagePath = new File("/sdcard/"+System.currentTimeMillis()+"invoice.jpg");
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(imagePath);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
            Toast.makeText(getApplicationContext(),imagePath.getAbsolutePath()+"",Toast.LENGTH_SHORT).show();
            boolean_save = true;

            generateBtn.setText("Check Invoice");

            Log.e("ImageSave", "Saveimage");
        } catch (FileNotFoundException e) {
            Log.e("GREC", e.getMessage(), e);
        } catch (IOException e) {
            Log.e("GREC", e.getMessage(), e);
        }
    }

    public static Bitmap loadBitmapFromView(View v, int width, int height) {
        Bitmap b = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);

        v.draw(c);


        return b;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSIONS) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                boolean_permission = true;


            } else {
                Toast.makeText(getApplicationContext(), "Please allow the permission", Toast.LENGTH_LONG).show();

            }
        }
    }
}
