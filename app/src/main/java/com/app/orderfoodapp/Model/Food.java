package com.app.orderfoodapp.Model;

import java.sql.Timestamp;

public class Food {
    private int foodId;
    private String foodName;
    private Double price;
    private String description;
    private String foodImage;
    private int categoryId;
    private int storeId;
    private Timestamp created_at;
    private Timestamp updated_at;

    public Food() {
    }

    public Food(int foodId, String foodName, Double price, String description, String foodImage, int categoryId, int storeId, Timestamp created_at, Timestamp updated_at) {
        this.foodId = foodId;
        this.foodName = foodName;
        this.price = price;
        this.description = description;
        this.foodImage = foodImage;
        this.categoryId = categoryId;
        this.storeId = storeId;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public int getFoodId() {
        return foodId;
    }

    public void setFoodId(int foodId) {
        this.foodId = foodId;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFoodImage() {
        return foodImage;
    }

    public void setFoodImage(String foodImage) {
        this.foodImage = foodImage;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public Timestamp getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Timestamp created_at) {
        this.created_at = created_at;
    }

    public Timestamp getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Timestamp updated_at) {
        this.updated_at = updated_at;
    }

    @Override
    public String toString() {
        return "Food{" +
                "foodId=" + foodId +
                ", foodName='" + foodName + '\'' +
                ", price=" + price +
                ", description='" + description + '\'' +
                ", foodImage='" + foodImage + '\'' +
                ", categoryId=" + categoryId +
                ", storeId=" + storeId +
                ", created_at=" + created_at +
                ", updated_at=" + updated_at +
                '}';
    }
}
