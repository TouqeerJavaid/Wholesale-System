package com.example.inventaryandorderbooking;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.inventaryandorderbooking.Model.ProductCart;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SalesRecordActivity extends AppCompatActivity {

    EditText startDate, endDate;
    TextView totalSales, totalOrders;
    List<ProductCart> productList;
    String sDateString, eDateString;
    int startMonth, endMonth;
    int sDate, eDate;
    int sYear, eYear;
    DatabaseReference salesRef;
    Button showRecordBtn;
    int totalSale, totalOrder;
    Calendar myCalendar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_record);

       myCalendar = Calendar.getInstance();

        totalSales = (TextView) findViewById(R.id.totalSales);
        totalOrders = (TextView) findViewById(R.id.totalOrders);
        startDate = (EditText) findViewById(R.id.startDate);
        endDate = (EditText) findViewById(R.id.endDate);
        showRecordBtn = (Button) findViewById(R.id.showRecordBtn);
        salesRef = FirebaseDatabase.getInstance().getReference("SalesRecord");


        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel1();
            }

        };

        final DatePickerDialog.OnDateSetListener date2 = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel2();
            }

        };
        startDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(SalesRecordActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(SalesRecordActivity.this, date2, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        showRecordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!TextUtils.isEmpty(startDate.getText().toString()) && !TextUtils.isEmpty(endDate.getText().toString())){
                    sDateString = startDate.getText().toString();
                    eDateString = endDate.getText().toString();
//                    Toast.makeText(SalesRecordActivity.this, ""+sDateString, Toast.LENGTH_SHORT).show();
                    final String[] start = sDateString.split("/");

                    sDate = Integer.parseInt(start[0]);
                    startMonth = Integer.parseInt(start[1]);
                    sYear = Integer.parseInt(start[2]);


                    String[] end = eDateString.split("/");
                    eDate = Integer.parseInt(end[0]);
                    endMonth = Integer.parseInt(end[1]);
                    eYear = Integer.parseInt(end[2]);

                    Toast.makeText(getApplicationContext(), ""+start[0], Toast.LENGTH_SHORT).show();


                    productList = new ArrayList<>();

                    salesRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            totalOrder = 0;
                            totalSale = 0;
                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                                if (dataSnapshot1.hasChildren()) {
                                    ProductCart productCart = dataSnapshot1.getValue(ProductCart.class);

                                    String[] saleDate = productCart.getProduct_add_date().split("/");
                                    int product_date = Integer.parseInt(saleDate[0]);
                                    int product_month = Integer.parseInt(saleDate[1]);
                                    int product_year = Integer.parseInt(saleDate[2]);


                                    if ((product_date >= sDate && product_month >= startMonth && product_year >= sYear) &&
                                            (product_date <= eDate && product_month <= endMonth && product_year <= eYear)) {

                                        totalSale += Integer.parseInt(productCart.getProduct_sale_price()) * Integer.parseInt(productCart.getUserSelectedQty());
                                        Log.d("result", ""+productCart.getProduct_sale_price());

                                        totalOrder += 1;

                                    }

                                }
                            }
                            totalSales.setText("Rs : "+totalSale);
                            totalOrders.setText(""+totalOrder);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    
                }else{
                    Toast.makeText(SalesRecordActivity.this, "Kindly Enter the start and end date!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void updateLabel1() {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        startDate.setText(sdf.format(myCalendar.getTime()));


    }


    private void updateLabel2() {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        endDate.setText(sdf.format(myCalendar.getTime()));


    }
}
