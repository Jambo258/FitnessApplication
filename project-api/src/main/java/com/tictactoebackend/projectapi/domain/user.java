package com.tictactoebackend.projectapi.domain;

public class user {
    private String id;
    private String username;
    private String email;
    private String password;
    private String role;
    private boolean healthdata;

    public user() {

    }

    public user(String id, String username, String email, String password, String role, boolean healthdata) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
        this.healthdata = healthdata;
    }

    public boolean getHealthData() {
        return healthdata;
    }

    public void setHealthData(boolean healthdata) {
        this.healthdata = healthdata;
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
