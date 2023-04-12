package com.xdu.formteamtalent.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_req")
@ApiModel("请求")
public class JoinRequest {
    @JsonProperty("id")
    @ApiModelProperty("请求编号")
    private String id;

    @JsonProperty("fromId")
    @ApiModelProperty("请求人用户编号")
    private String fromId;

    @JsonProperty("toId")
    @ApiModelProperty("被请求人用户编号")
    private String toId;

    @JsonProperty("aId")
    @ApiModelProperty("活动编号")
    private String aId;

    @JsonProperty("tId")
    @ApiModelProperty("小组编号")
    private String tId;

    @JsonProperty("tName")
    @ApiModelProperty("小组名称")
    private String tName;

    @JsonProperty("uName")
    @ApiModelProperty("请求人名称")
    private String uName;

    @JsonProperty("content")
    @ApiModelProperty("请求内容")
    private String content;

    @JsonProperty("sendDate")
    @ApiModelProperty("请求时间")
    private String sendDate;

    @JsonProperty("agree")
    @ApiModelProperty("是否同意")
    private Integer agree;

    @JsonProperty("status")
    @ApiModelProperty("当前处理状态")
    private Integer status;
}
