package com.iot.vo;

import java.util.Date;

public class DeviceState {
    private String clientId;
    private String state;
    private Date connectedTime;
    private String strEndTime;
    private String settings;

    public DeviceState(String clientId, String state, String strEndTime, String settings) {
        this.clientId = clientId;
        this.state = state;
        this.strEndTime = strEndTime;
        this.settings = settings;
    }

    public String getSettings() {
        return settings;
    }

    public void setSettings(String settings) {
        this.settings = settings;
    }

    public String getStrEndTime() {
        return strEndTime;
    }

    public void setStrEndTime(String strEndTime) {
        this.strEndTime = strEndTime;
    }

    public Date getConnectedTime() {
        return connectedTime;
    }

    public void setConnectedTime(Date connectedTime) {
        this.connectedTime = connectedTime;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
