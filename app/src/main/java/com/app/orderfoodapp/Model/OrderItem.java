package com.app.orderfoodapp.Model;

public class OrderItem {
    private int foodId;
    private int quantity;

    public OrderItem(int foodId, int quantity) {
        this.foodId = foodId;
        this.quantity = quantity;
    }
    public  OrderItem(){

    }
    public int getFoodId() {
        return foodId;
    }

    public void setFoodId(int foodId) {
        this.foodId = foodId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}