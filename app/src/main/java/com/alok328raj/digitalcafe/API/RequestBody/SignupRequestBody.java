package com.alok328raj.digitalcafe.API.RequestBody;

public class SignupRequestBody {
    private String roll;
    private String name;
    private String email;
    private String password;

    public SignupRequestBody(String roll, String name, String email, String password) {
        this.roll = roll;
        this.name = name;
        this.email = email;
        this.password = password;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
