package com.iot.pojo;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Table(name = "tb_operat")
public class Operat {

    // id
    @Id// 标识是主键id
    @Column(name = "o_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 用户id
    @Column(name = "u_id")
    private String uId;

    // 发送的指令
    @Column(name = "o_cmd")
    private String cmd;

    // 发送的时间
    @Column(name = "o_time")
    private Timestamp time;

}
