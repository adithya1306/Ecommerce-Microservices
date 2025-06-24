package com.adi.user_service.model;

public class LoginResponse {
    private String token;
    private String name;
    private boolean isAdmin;

    public LoginResponse(String token, String name, boolean isAdmin) {
        this.token = token;
        this.name = name;
        this.isAdmin = isAdmin;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }
}
