package com.example.inventaryandorderbooking.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class Productinfo implements Parcelable {

   private String imageurl;
   private String product_name;
   private String product_quantity;
   private String product_purchase_price;
   private String product_sale_price;
   private String product_add_date;
   private String product_low_inventory_alerts;
   private String wholesalerId;
   private String productId;
   public Productinfo() {
    }


    public Productinfo(String imageurl, String product_name, String product_quantity, String product_purchase_price, String product_sale_price, String product_add_date, String product_low_inventory_alerts, String wholesalerId, String productId) {
        this.imageurl = imageurl;
        this.product_name = product_name;
        this.product_quantity = product_quantity;
        this.product_purchase_price = product_purchase_price;
        this.product_sale_price = product_sale_price;
        this.product_add_date = product_add_date;
        this.product_low_inventory_alerts = product_low_inventory_alerts;
        this.wholesalerId = wholesalerId;
        this.productId = productId;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_quantity() {
        return product_quantity;
    }

    public void setProduct_quantity(String product_quantity) {
        this.product_quantity = product_quantity;
    }

    public String getProduct_purchase_price() {
        return product_purchase_price;
    }

    public void setProduct_purchase_price(String product_purchase_price) {
        this.product_purchase_price = product_purchase_price;
    }

    public String getProduct_sale_price() {
        return product_sale_price;
    }

    public void setProduct_sale_price(String product_sale_price) {
        this.product_sale_price = product_sale_price;
    }

    public String getProduct_add_date() {
        return product_add_date;
    }

    public void setProduct_add_date(String product_add_date) {
        this.product_add_date = product_add_date;
    }

    public String getProduct_low_inventory_alerts() {
        return product_low_inventory_alerts;
    }

    public void setProduct_low_inventory_alerts(String product_low_inventory_alerts) {
        this.product_low_inventory_alerts = product_low_inventory_alerts;
    }

    public String getWholesalerId() {
        return wholesalerId;
    }

    public void setWholesalerId(String wholesalerId) {
        this.wholesalerId = wholesalerId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public static Creator<Productinfo> getCREATOR() {
        return CREATOR;
    }

    protected Productinfo(Parcel in) {
        imageurl = in.readString();
        product_name = in.readString();
        product_quantity = in.readString();
        product_purchase_price = in.readString();
        product_sale_price = in.readString();
        product_add_date = in.readString();
        product_low_inventory_alerts = in.readString();
        wholesalerId = in.readString();
        productId = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(imageurl);
        dest.writeString(product_name);
        dest.writeString(product_quantity);
        dest.writeString(product_purchase_price);
        dest.writeString(product_sale_price);
        dest.writeString(product_add_date);
        dest.writeString(product_low_inventory_alerts);
        dest.writeString(wholesalerId);
        dest.writeString(productId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Productinfo> CREATOR = new Creator<Productinfo>() {
        @Override
        public Productinfo createFromParcel(Parcel in) {
            return new Productinfo(in);
        }

        @Override
        public Productinfo[] newArray(int size) {
            return new Productinfo[size];
        }
    };
}
