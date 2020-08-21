package com.example.inventaryandorderbooking.Model;

public class OrderInfo {
    private String imageurl;
    private String product_name;
    private String product_quantity;
    private String userSelectedQty;
    private String product_purchase_price;
    private String product_sale_price;
    private String product_add_date;
    private String product_low_inventory_alerts;
    private String shopkeeperName;
    private String shopkeeperEmail;
    private String shopkeeperAddress;
    private String shopkeeperCity;
    private String shopkeeperPhone;
    private String wholesalerId;
    private String productId;

    public OrderInfo() {
    }

    public OrderInfo(String imageurl, String product_name, String product_quantity, String userSelectedQty, String product_purchase_price, String product_sale_price, String product_add_date, String product_low_inventory_alerts, String shopkeeperName, String shopkeeperEmail, String shopkeeperAddress, String shopkeeperCity, String shopkeeperPhone, String wholesalerId, String productId) {
        this.imageurl = imageurl;
        this.product_name = product_name;
        this.product_quantity = product_quantity;
        this.userSelectedQty = userSelectedQty;
        this.product_purchase_price = product_purchase_price;
        this.product_sale_price = product_sale_price;
        this.product_add_date = product_add_date;
        this.product_low_inventory_alerts = product_low_inventory_alerts;
        this.shopkeeperName = shopkeeperName;
        this.shopkeeperEmail = shopkeeperEmail;
        this.shopkeeperAddress = shopkeeperAddress;
        this.shopkeeperCity = shopkeeperCity;
        this.shopkeeperPhone = shopkeeperPhone;
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

    public String getUserSelectedQty() {
        return userSelectedQty;
    }

    public void setUserSelectedQty(String userSelectedQty) {
        this.userSelectedQty = userSelectedQty;
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

    public String getShopkeeperName() {
        return shopkeeperName;
    }

    public void setShopkeeperName(String shopkeeperName) {
        this.shopkeeperName = shopkeeperName;
    }

    public String getShopkeeperEmail() {
        return shopkeeperEmail;
    }

    public void setShopkeeperEmail(String shopkeeperEmail) {
        this.shopkeeperEmail = shopkeeperEmail;
    }

    public String getShopkeeperAddress() {
        return shopkeeperAddress;
    }

    public void setShopkeeperAddress(String shopkeeperAddress) {
        this.shopkeeperAddress = shopkeeperAddress;
    }

    public String getShopkeeperCity() {
        return shopkeeperCity;
    }

    public void setShopkeeperCity(String shopkeeperCity) {
        this.shopkeeperCity = shopkeeperCity;
    }

    public String getShopkeeperPhone() {
        return shopkeeperPhone;
    }

    public void setShopkeeperPhone(String shopkeeperPhone) {
        this.shopkeeperPhone = shopkeeperPhone;
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
}
