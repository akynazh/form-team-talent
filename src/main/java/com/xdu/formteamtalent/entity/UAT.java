package com.xdu.formteamtalent.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_uat")
public class UAT {
    @TableId(type = IdType.AUTO)
    @JsonProperty("id")
    private Long id;
    @JsonProperty("uId")
    private String uId;
    @JsonProperty("aId")
    private String aId;
    @JsonProperty("tId")
    private String tId;
}
