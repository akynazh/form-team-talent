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
@TableName("t_activity")
public class Activity implements Serializable {
    @TableId
    @JsonProperty("aId")
    private String aId;
    @JsonProperty("aName")
    private String aName;
    @JsonProperty("aHolderId")
    private String aHolderId;
    @JsonProperty("aDesc")
    private String aDesc;
    @JsonProperty("aEndDate")
    private String aEndDate;
    @JsonProperty("aIsPublic")
    private Integer aIsPublic;
    @JsonProperty("aQrcodePath")
    private String aQrcodePath;
    @JsonProperty("status")
    private Integer status;
}
