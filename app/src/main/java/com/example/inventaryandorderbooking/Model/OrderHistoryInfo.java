package com.example.inventaryandorderbooking.Model;

public class OrderHistoryInfo {
    String orderDate, totalPrice;

    public OrderHistoryInfo() {
    }

    public OrderHistoryInfo(String orderDate, String totalPrice) {
        this.orderDate = orderDate;
        this.totalPrice = totalPrice;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }
}
