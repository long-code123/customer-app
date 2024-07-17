package com.app.orderfoodapp.Manager;

import com.app.orderfoodapp.Model.CartItem;

import java.util.ArrayList;
import java.util.List;

public class CartManager {

    private static CartManager instance;
    private List<CartItem> cartItems;

    private CartManager() {
        cartItems = new ArrayList<>();
    }

    public static CartManager getInstance() {
        if (instance == null) {
            instance = new CartManager();
        }
        return instance;
    }

    public void addToCart(CartItem item) {
        for (CartItem cartItem : cartItems) {
            if (cartItem.getFoodName().equals(item.getFoodName())) {
                cartItem.setQuantity(cartItem.getQuantity() + 1);
                return;
            }
        }
        cartItems.add(item);
    }

    public void removeFromCart(CartItem item) {
        for (CartItem cartItem : cartItems) {
            if (cartItem.getFoodName().equals(item.getFoodName())) {
                if (cartItem.getQuantity() > 1) {
                    cartItem.setQuantity(cartItem.getQuantity() - 1);
                } else {
                    cartItems.remove(cartItem);
                }
                return;
            }
        }
    }
    public void clearCart() {
        cartItems.clear();
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }
}
