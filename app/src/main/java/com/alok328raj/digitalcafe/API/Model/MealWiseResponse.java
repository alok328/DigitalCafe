package com.alok328raj.digitalcafe.API.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MealWiseResponse {

    @SerializedName("Date")
    @Expose
    private String day;

    @SerializedName("Meal")
    @Expose
    private int Meal;

    @SerializedName("Consumers")
    @Expose
    private int consumers;

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public int getMeal() {
        return Meal;
    }

    public void setMeal(int meal) {
        Meal = meal;
    }

    public int getConsumers() {
        return consumers;
    }

    public void setConsumers(int consumers) {
        this.consumers = consumers;
    }
}
