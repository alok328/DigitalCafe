package com.alok328raj.digitalcafe.API.Model.transaction;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Transaction {

    private Date date;
    private String _id;
    private String menu;
    private Float price;
    private Float balance;

    public Transaction(Date date, String _id, String menu, Float price) {
        this.date = date;
        this._id = _id;
        this.menu = menu;
        this.price = price;
    }

    public Transaction(Date date, String _id, String menu, Float price, Float balance) {
        this.date = date;
        this._id = _id;
        this.menu = menu;
        this.price = price;
        this.balance = balance;
    }

    public Float getBalance() {
        return balance;
    }

    public void setBalance(Float balance) {
        this.balance = balance;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getMenu() {
        return menu;
    }

    public void setMenu(String menu) {
        this.menu = menu;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }
}
