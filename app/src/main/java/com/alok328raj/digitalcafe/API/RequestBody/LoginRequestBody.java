package com.alok328raj.digitalcafe.API.RequestBody;

public class LoginRequestBody {
    private String roll;
    private String password;

    public LoginRequestBody(String roll, String password) {
        this.roll = roll;
        this.password = password;
    }

    public String getRoll() {
        return roll;
    }

    public void setRoll(String roll) {
        this.roll = roll;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
