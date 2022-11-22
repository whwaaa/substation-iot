package com.iot.vo;

import com.iot.pojo.User;

public class LoginSuccess {
    private String username;
    private String time;
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

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

    public LoginSuccess(String username, String time, User user) {
        this.username = username;
        this.time = time;
        this.user = user;
    }
}
