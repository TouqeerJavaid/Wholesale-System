package com.example.inventaryandorderbooking;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class Screenshot extends AppCompatActivity {

    ImageView iv_image;
    int invoiceId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screenshot);

        if(getIntent() != null){
            invoiceId = getIntent().getIntExtra("invoiceId", 0);
        }
        init();
    }

    private void init() {
        iv_image = (ImageView) findViewById(R.id.iv_image);
        String completePath = Environment.getExternalStorageDirectory() + "/screenshotdemo.jpg";
        Glide.with(Screenshot.this).load(completePath).into(iv_image);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, Wholesaler_home.class);
        startActivity(intent);
        finish();
    }
}
