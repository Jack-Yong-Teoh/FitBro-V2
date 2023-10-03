package com.example.fitbrov2;

public class food {
    String date, foodName, time, quantity, totalCalorie;

    public food() {

    }

    public food(String foodName) {
        this.foodName = foodName;
    }

    public food(String date, String foodName, String time, String quantity, String totalCalorie) {
        this.date = date;
        this.foodName = foodName;
        this.time = time;
        this.quantity = quantity;
        this.totalCalorie = totalCalorie;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getTotalCalorie() {
        return totalCalorie;
    }

    public void setTotalCalorie(String totalCalorie) {
        this.totalCalorie = totalCalorie;
    }
}
