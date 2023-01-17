package com.example.raj_liangjian.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import org.springframework.context.annotation.Configuration;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 员工实体
 */
@Configuration
@Data
public class Employee implements Serializable {

    private static final long serialVersionUID = 1L;
    /*@TableId(type = IdType.AUTO)*/
    private Long id;

    private String username;

    private String name;

    private String password;

    private String phone;

    private String sex;

    private String idNumber;//身份证号码

    private Integer status;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableField(fill = FieldFill.INSERT)
    private Long createUser;

    @TableField(fill = FieldFill.INSERT_UPDATE )
    private Long updateUser;

}
