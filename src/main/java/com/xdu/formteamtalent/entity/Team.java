package com.xdu.formteamtalent.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
    @TableId(type = IdType.AUTO)
    private Long t_id;
    private Long a_id;
    private String t_name;
    private String t_leader_id;
    private String t_desc;
    private Integer t_is_public;
    private String t_qrcode_url;
}
