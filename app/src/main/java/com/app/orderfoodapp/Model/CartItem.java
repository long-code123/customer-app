package com.app.orderfoodapp.Model;

public class CartItem {
    private int id;
    private String foodName;
    private double price;
    private int quantity;
    private String foodImage;
    private int storeId;

    public CartItem(int id, String foodName, double price, int quantity, String foodImage, int storeId) {
        this.id = id;
        this.foodName = foodName;
        this.price = price;
        this.quantity = quantity;
        this.foodImage = foodImage;
        this.storeId = storeId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getFoodImage() {
        return foodImage;
    }

    public void setFoodImage(String foodImage) {
        this.foodImage = foodImage;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }
}
