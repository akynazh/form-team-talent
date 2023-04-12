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

import java.io.Serializable;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_team")
@ApiModel("小组")
public class Team implements Serializable {
    @TableId
    @JsonProperty("tId")
    @ApiModelProperty("小组编号")
    private String tId;

    @JsonProperty("aId")
    @ApiModelProperty("活动编号")
    private String aId;

    @JsonProperty("tName")
    @ApiModelProperty("小组名称")
    private String tName;

    @JsonProperty("tLeaderId")
    @ApiModelProperty("组长编号")
    private String tLeaderId;

    @JsonProperty("tDesc")
    @ApiModelProperty("小组描述")
    private String tDesc;

    @JsonProperty("tTotal")
    @ApiModelProperty("小组限制人数")
    private Integer tTotal;

    @JsonProperty("tCount")
    @ApiModelProperty("小组现有人数")
    private Integer tCount;
}
