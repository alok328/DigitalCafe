package com.alok328raj.digitalcafe.API.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BalanceResponse {

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("bal")
    @Expose
    private Float bal;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Float getBal() {
        return bal;
    }

    public void setBal(Float bal) {
        this.bal = bal;
    }
}
