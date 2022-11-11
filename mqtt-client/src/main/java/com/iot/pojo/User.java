package com.iot.pojo;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;// https://blog.csdn.net/maoyuanming0806/article/details/78011700
// sql.Date只有日期部分


@Data// 消除get/set/toString方法
@Table(name = "tb_user")
public class User {

    // id
    @Id// 标识是主键id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "u_id")
    // 指定配置列的增量 1.https://blog.csdn.net/weixin_42131342/article/details/114713122
    //               2.https://blog.csdn.net/tree_java/article/details/71158122
    // GenerationType.TABLE: 使用一个特定的数据库表格来保存主键。
    // GenerationType.SEQUENCE: 根据底层数据库的序列来生成主键，条件是数据库支持序列。
    // GenerationType.IDENTITY: 主键由数据库自动生成（主要是自动增长型）
    // GenerationType.AUTO: 主键由程序控制。
    private Long id;

    // 姓名
    @Column(name = "u_name")
    private String name;

    // 账号
    @Column(name = "u_account")
    private String account;

    // 密码
    @Column(name = "u_passwd")
    private String passwd;

    // 认证码
    @Column(name = "u_token")
    private String token;

    // 个性化设置项
    @Column(name = "u_settings")
    private String settings;

    // 创建日期
    @Column(name = "u_createtime")
    private Timestamp createTime;

    // 更新日期
    @Column(name = "u_updatetime")
    private Timestamp updateTime;

    // 是否删除 0:不删除 1:删除
    @Column(name = "u_is_delete")
    private Integer isDelete;

}
