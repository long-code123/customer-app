package com.app.orderfoodapp.Model;

public class Voucher {
    private int voucherId;
    private String description;
    private double value;
    private String conditition;
    private int orderId;

    public Voucher() {
    }

    public Voucher(int voucherId, String description, double value, String conditition, int orderId) {
        this.voucherId = voucherId;
        this.description = description;
        this.value = value;
        this.conditition = conditition;
        this.orderId = orderId;
    }

    public int getVoucherId() {
        return voucherId;
    }

    public void setVoucherId(int voucherId) {
        this.voucherId = voucherId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String getConditition() {
        return conditition;
    }

    public void setConditition(String conditition) {
        this.conditition = conditition;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }
}
