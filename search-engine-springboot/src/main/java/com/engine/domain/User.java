package com.engine.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

//用户表(User)实体类
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "sys_user")
public class User implements Serializable {
    private static final long serialVersionUID = -40356785423868312L;
    @TableId
    private Long id;//主键

    private String userName;//用户名

    private String nickName;//昵称

    private String password;//密码

    private String status;//账号状态（0正常 1停用）

    private String email;// 邮箱

    private String phonenumber;//手机号

    private char sex;//用户性别（0男，1女，2未知）

    private String avatar;//头像

}
