package com.iot.pojo;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Data// 消除get/set/toString方法
@Table(name = "tb_device")
public class Device {

    // id
    @Id// 标识是主键id
    @Column(name = "d_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // 指定配置列的增量 1.https://blog.csdn.net/weixin_42131342/article/details/114713122
    //               2.https://blog.csdn.net/tree_java/article/details/71158122
    // GenerationType.TABLE: 使用一个特定的数据库表格来保存主键。
    // GenerationType.SEQUENCE: 根据底层数据库的序列来生成主键，条件是数据库支持序列。
    // GenerationType.IDENTITY: 主键由数据库自动生成（主要是自动增长型）
    // GenerationType.AUTO: 主键由程序控制。
    private Long id;

    //设备名称
    @Column(name = "d_name")
    private String name;

    // 设备描述
    @Column(name = "d_describe")
    private String describe;

    // 创建时间
    @Column(name = "d_create_time")
    private Timestamp createTime;

    // 修改时间
    @Column(name = "d_update_time")
    private Timestamp updateTime;

    // 数据删除标志 0:不删除 1:删除
    @Column(name = "d_is_delete")
    private Integer isDelete;

    // 设备的配置信息
    @Column(name = "d_settings")
    private String settings;

}
