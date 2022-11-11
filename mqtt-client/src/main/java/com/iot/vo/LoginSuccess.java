package com.iot.vo;

public class LoginSuccess {
    private String username;
    private String time;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public LoginSuccess(String username, String time) {
        this.username = username;
        this.time = time;
    }
}
