package com.tictactoebackend.projectapi.domain;

import java.sql.Timestamp;

public class Training {

    private String id;
    private String creator;
    private String currentWeight;
    private Integer dailyCalories;
    private Integer dailySteps;
    private Timestamp createdAt;



    public Training() {

    }

    public Training(String id, String creator,Integer dailyCalories, Integer dailySteps, String currentWeight, Timestamp createdAt) {
        this.id = id;
        this.creator = creator;
        this.dailyCalories = dailyCalories;
        this.dailySteps = dailySteps;
        this.currentWeight = currentWeight;
        this.createdAt = createdAt;
    }

    // Getters and setters

    public String getCurrentWeight() {
        return currentWeight;
    }

    public void setCurrentWeight(String currentWeight) {
        this.currentWeight = currentWeight;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public Integer getDailyCalories() {
        return dailyCalories;
    }

    public void setDailyCalories(Integer dailyCalories) {
        this.dailyCalories = dailyCalories;
    }

    public Integer getDailySteps() {
        return dailySteps;
    }

    public void setDailySteps(Integer dailySteps) {
        this.dailySteps = dailySteps;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

}
