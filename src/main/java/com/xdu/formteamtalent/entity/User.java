package com.xdu.formteamtalent.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_user")
public class User implements Serializable {
    @TableId
    private String u_id;
    private String u_name;
    private String u_stu_num;
    private String u_school;
    private String u_sex;
    private String u_major;

    public User(String u_id) {
        this.u_id = u_id;
    }
}
