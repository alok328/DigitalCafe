package com.alok328raj.digitalcafe.API.RequestBody;

public class BalanceRequestBody {
    private String roll;

    public BalanceRequestBody(String roll) {
        this.roll = roll;
    }

    public String getRoll() {
        return roll;
    }

    public void setRoll(String roll) {
        this.roll = roll;
    }
}
