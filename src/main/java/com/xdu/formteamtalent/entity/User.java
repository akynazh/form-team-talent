package com.xdu.formteamtalent.entity;

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
    private String u_open_id;
    private String u_name;
    private String u_stu_num;
    private String u_school;

    public User(String u_open_id) {
        this.u_open_id = u_open_id;
    }
}
