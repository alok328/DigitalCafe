package com.alok328raj.digitalcafe.API.RequestBody;

public class TransactionsRequestBody {
    private String menu;
    private float price;
    private String hostel;

    public TransactionsRequestBody(String menu, float price, String hostel) {
        this.menu = menu;
        this.price = price;
        this.hostel = hostel;
    }

    public String getHostel() {
        return hostel;
    }

    public void setHostel(String hostel) {
        this.hostel = hostel;
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
