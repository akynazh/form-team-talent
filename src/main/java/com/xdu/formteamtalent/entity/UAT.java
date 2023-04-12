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
@TableName("t_uat")
@ApiModel("用户, 活动, 小组关联信息")
public class UAT {
    @TableId(type = IdType.AUTO)
    @JsonProperty("id")
    @ApiModelProperty("编号")
    private Long id;

    @JsonProperty("uId")
    @ApiModelProperty("用户编号")
    private String uId;

    @JsonProperty("aId")
    @ApiModelProperty("活动编号")
    private String aId;

    @JsonProperty("tId")
    @ApiModelProperty("小组编号")
    private String tId;
}
