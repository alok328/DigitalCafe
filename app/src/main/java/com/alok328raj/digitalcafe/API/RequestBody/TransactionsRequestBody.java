package com.alok328raj.digitalcafe.API.RequestBody;

public class TransactionsRequestBody {
    private String menu;
    private float price;

    public TransactionsRequestBody(String menu, float price) {
        this.menu = menu;
        this.price = price;
    }

    public String getMenu() {
        return menu;
    }

    public void setMenu(String menu) {
        this.menu = menu;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }
}
