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
@TableName("t_req")
public class JoinRequest {
    @JsonProperty("id")
    private String id;
    @JsonProperty("fromId")
    private String fromId;
    @JsonProperty("toId")
    private String toId;
    @JsonProperty("aId")
    private String aId;
    @JsonProperty("tId")
    private String tId;
    @JsonProperty("tName")
    private String tName;
    @JsonProperty("uName")
    private String uName;
    @JsonProperty("content")
    private String content;
    @JsonProperty("sendDate")
    private String sendDate;
    @JsonProperty("agree")
    private Integer agree;
    @JsonProperty("status")
    private Integer status;
}
