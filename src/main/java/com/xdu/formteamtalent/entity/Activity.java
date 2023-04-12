package com.xdu.formteamtalent.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
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
@ApiModel("活动")
public class Activity implements Serializable {
    @TableId
    @JsonProperty("aId")
    @ApiModelProperty("活动编号")
    private String aId;

    @JsonProperty("aName")
    @ApiModelProperty("活动名称")
    private String aName;

    @JsonProperty("aHolderId")
    @ApiModelProperty("活动组织者用户编号")
    private String aHolderId;

    @JsonProperty("aDesc")
    @ApiModelProperty("活动描述")
    private String aDesc;

    @JsonProperty("aEndDate")
    @ApiModelProperty("活动截止时间")
    private String aEndDate;

    @JsonProperty("aIsPublic")
    @ApiModelProperty("活动是否公开")
    private Integer aIsPublic;

    @JsonProperty("aQrcodePath")
    @ApiModelProperty("活动二维码 url 地址")
    private String aQrcodePath;

    @JsonProperty("status")
    @ApiModelProperty("活动当前状态")
    private Integer status;
}
