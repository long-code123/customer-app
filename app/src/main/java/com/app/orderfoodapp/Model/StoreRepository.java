package com.app.orderfoodapp.Model;

import java.util.List;

public class StoreRepository {

    private static StoreRepository instance;
    private List<Store> storeList;

    private StoreRepository() {
    }

    public static StoreRepository getInstance() {
        if (instance == null) {
            instance = new StoreRepository();
        }
        return instance;
    }

    public List<Store> getStoreList() {
        return storeList;
    }

    public void setStoreList(List<Store> storeList) {
        this.storeList = storeList;
    }
    public Store getStoreById(int storeId) {
        if (storeList != null) {
            for (Store store : storeList) {
                if (store.getStoreId() == storeId) {
                    return store;
                }
            }
        }
        return null;
    }
}