package com.xdu.formteamtalent.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel("用户")
public class User implements Serializable {
    @TableId
    @JsonProperty("uId")
    @ApiModelProperty("用户编号")
    private String uId;

    @JsonProperty("uPwd")
    @ApiModelProperty("用户密码")
    private String uPwd;

    @JsonProperty("uName")
    @ApiModelProperty("用户名称")
    private String uName;

    @JsonProperty("uStuNum")
    @ApiModelProperty("用户学号")
    private String uStuNum;

    @JsonProperty("uSchool")
    @ApiModelProperty("用户当前就读学校")
    private String uSchool;

    @JsonProperty("uSex")
    @ApiModelProperty("用户性别")
    private String uSex;

    @JsonProperty("uMajor")
    @ApiModelProperty("用户就读专业")
    private String uMajor;

    public User(String uId) {
        this.uId = uId;
    }
}
