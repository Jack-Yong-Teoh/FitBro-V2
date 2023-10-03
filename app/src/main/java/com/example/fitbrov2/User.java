package com.example.fitbrov2;

import android.widget.EditText;

public class User {

    public String username, email, password, gender, status;
    public Double ociValue, calorieConsumed, weight, height;
    public int dailyResetCheck, age;

    public User(){

    }

    public User(EditText username, EditText email){

    }

    public User(String username, String email, String password, Double ociValue, Double calorieConsumed, int dailyResetCheck, String gender, Double weight, Double height, int age, String status){
        this.username = username;
        this.email = email;
        this.password = password;
        this.ociValue = ociValue;
        this.calorieConsumed = calorieConsumed;
        this.dailyResetCheck = dailyResetCheck;
        this.gender = gender;
        this.weight = weight;
        this.height = height;
        this.age = age;
        this.status = status;
    }


}
