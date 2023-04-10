package com.xdu.formteamtalent.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonProperty("uId")
    private String uId;
    @JsonProperty("uPwd")
    private String uPwd;
    @JsonProperty("uName")
    private String uName;
    @JsonProperty("uStuNum")
    private String uStuNum;
    @JsonProperty("uSchool")
    private String uSchool;
    @JsonProperty("uSex")
    private String uSex;
    @JsonProperty("uMajor")
    private String uMajor;

    public User(String uId) {
        this.uId = uId;
    }
}
