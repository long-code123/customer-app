package com.app.orderfoodapp.Model;

import java.sql.Timestamp;

public class Store {
    private int storeId;
    private String storeName;
    private String storeImage;
    private String address;
    private Timestamp created_at;
    private Timestamp updated_at;

    public Store() {
    }

    public Store(int storeId, String storeName, String storeImage, String address, Timestamp created_at, Timestamp updated_at) {
        this.storeId = storeId;
        this.storeName = storeName;
        this.storeImage = storeImage;
        this.address = address;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getStoreImage() {
        return storeImage;
    }

    public void setStoreImage(String storeImage) {
        this.storeImage = storeImage;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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
        return "Store{" +
                "storeId=" + storeId +
                ", storeName='" + storeName + '\'' +
                ", storeImage='" + storeImage + '\'' +
                ", address='" + address + '\'' +
                ", created_at=" + created_at +
                ", updated_at=" + updated_at +
                '}';
    }
}
