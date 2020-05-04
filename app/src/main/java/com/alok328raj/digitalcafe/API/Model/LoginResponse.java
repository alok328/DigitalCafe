package com.alok328raj.digitalcafe.API.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginResponse {
    @SerializedName("token")
    @Expose
    private String token;

    @SerializedName("roll")
    @Expose
    private String roll;

    public String getToken() {
        return token;
    }

    @SerializedName("name")
    @Expose
    private String name;

    public void setToken(String token) {
        this.token = token;
    }

    public String getRoll() {
        return roll;
    }

    public void setRoll(String roll) {
        this.roll = roll;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}