package com.app.orderfoodapp.Model;

import java.util.List;

public class OrderRequest {
    private String deliveryTime;
    private int userId;
    private int shipperId;
    private List<OrderItem> items;

    public OrderRequest(String deliveryTime, int userId, int shipperId, List<OrderItem> items) {
        this.deliveryTime = deliveryTime;
        this.userId = userId;
        this.shipperId = shipperId;
        this.items = items;
    }

    public OrderRequest() {
    }

    public String getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(String deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getShipperId() {
        return shipperId;
    }

    public void setShipperId(int shipperId) {
        this.shipperId = shipperId;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }
}
