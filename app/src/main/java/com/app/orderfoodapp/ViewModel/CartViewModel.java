package com.app.orderfoodapp.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.app.orderfoodapp.Model.CartItem;

import java.util.ArrayList;
import java.util.List;

public class CartViewModel extends ViewModel {
    private MutableLiveData<List<CartItem>> cartItemsLiveData;
    private MutableLiveData<Double> totalPriceLiveData;
    private int totalItems; // Tổng số món ăn trong giỏ hàng
    private double currentTotalPrice; // Tổng số tiền hiện tại của giỏ hàng
    private List<CartItem> cartItems;

    public CartViewModel() {
        cartItems = new ArrayList<>();
        cartItemsLiveData = new MutableLiveData<>();
        cartItemsLiveData.setValue(cartItems);

        totalPriceLiveData = new MutableLiveData<>();
        calculateAndSetTotalPrice(); // Tính toán và thiết lập tổng giá tiền ban đầu
    }

    public LiveData<List<CartItem>> getCartItemsLiveData() {
        return cartItemsLiveData;
    }

    public LiveData<Double> getTotalPrice() {
        return totalPriceLiveData;
    }

    public int getTotalItems() {
        return totalItems;
    }

    public double getCurrentTotalPrice() {
        return currentTotalPrice;
    }

    public void addToCart(CartItem cartItem) {
        cartItems.add(cartItem);
        cartItemsLiveData.setValue(cartItems);
        calculateAndSetTotalPrice(); // Sau khi thêm vào giỏ hàng, tính lại tổng giá tiền
    }

    public void removeFromCart(CartItem cartItem) {
        cartItems.remove(cartItem);
        cartItemsLiveData.setValue(cartItems);
        calculateAndSetTotalPrice(); // Sau khi xóa khỏi giỏ hàng, tính lại tổng giá tiền
    }

    public void updateCartItem(CartItem cartItem) {
        for (int i = 0; i < cartItems.size(); i++) {
            if (cartItems.get(i).getId() == cartItem.getId()) {
                cartItems.set(i, cartItem);
                break;
            }
        }
        cartItemsLiveData.setValue(cartItems);
        calculateAndSetTotalPrice(); // Sau khi cập nhật mặt hàng trong giỏ hàng, tính lại tổng giá tiền
    }

    public void clearCart() {
        cartItems.clear();
        cartItemsLiveData.setValue(cartItems);
        calculateAndSetTotalPrice(); // Sau khi xóa toàn bộ giỏ hàng, thiết lập lại tổng giá tiền là 0
    }

    private void calculateAndSetTotalPrice() {
        double totalPrice = 0;
        int itemsCount = 0;
        for (CartItem item : cartItems) {
            totalPrice += item.getPrice() * item.getQuantity();
            itemsCount += item.getQuantity();
        }
        currentTotalPrice = totalPrice;
        totalItems = itemsCount;
        totalPriceLiveData.setValue(totalPrice);
    }
}
