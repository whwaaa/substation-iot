package com.iot.pojo;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Data// 消除get/set/toString方法
@Table(name = "tb_devicemsg")
public class DeviceMsg {
    // id
    @Id// 标识是主键id
    @Column(name = "m_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 设备id
    @Column(name = "d_id")
    private Long dId;

    // 设备状态 1:开启 2:关闭
    @Column(name = "m_state")
    private Integer state;

    // 消息时间
    @Column(name = "m_time")
    private Timestamp time;

    // 请求者id
    @Column(name = "u_id")
    private Long uId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getdId() {
        return dId;
    }

    public void setdId(Long dId) {
        this.dId = dId;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public Long getuId() {
        return uId;
    }

    public void setuId(Long uId) {
        this.uId = uId;
    }
}
