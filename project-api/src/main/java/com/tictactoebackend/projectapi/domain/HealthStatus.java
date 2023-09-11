package com.tictactoebackend.projectapi.domain;

import java.sql.Timestamp;

public class HealthStatus {
    private String id;
    private String creator;
    private String height;
    private String weight;
    private Double bmi;
    private String targetWeight;
    private Integer targetCalories;
    private Integer targetSteps;
    private Timestamp createdAt;



    public HealthStatus() {


    }

    public HealthStatus(String id, String creator, String height, String weight, Double bmi, String targetWeight, Integer targetCalories, Integer targetSteps, Timestamp createdAt) {
        this.id = id;
        this.creator = creator;
        this.height = height;
        this.weight = weight;
        this.bmi = bmi;
        this.targetWeight = targetWeight;
        this.targetCalories = targetCalories;
        this.targetSteps = targetSteps;
        this.createdAt = createdAt;
    }

    // Getters and setters

    public String getTargetWeight() {
        return targetWeight;
    }

    public void setTargetWeight(String targetWeight) {
        this.targetWeight = targetWeight;
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

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public Double getBmi() {
        return bmi;
    }


    public void setBmi(Double bmi) {
        this.bmi = bmi;
    }


    public Integer getTargetCalories() {
        return targetCalories;
    }

    public void setTargetCalories(Integer targetCalories) {
        this.targetCalories = targetCalories;
    }

    public Integer getTargetSteps() {
        return targetSteps;
    }

    public void setTargetSteps(Integer targetSteps) {
        this.targetSteps = targetSteps;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

}
