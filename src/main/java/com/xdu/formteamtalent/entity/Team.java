package com.xdu.formteamtalent.entity;

import com.baomidou.mybatisplus.annotation.IdType;
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
@TableName("t_team")
public class Team implements Serializable {
    @TableId
    @JsonProperty("tId")
    private String tId;
    @JsonProperty("aId")
    private String aId;
    @JsonProperty("tName")
    private String tName;
    @JsonProperty("tLeaderId")
    private String tLeaderId;
    @JsonProperty("tDesc")
    private String tDesc;
    @JsonProperty("tTotal")
    private Integer tTotal;
    @JsonProperty("tCount")
    private Integer tCount;
}
