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
@TableName("t_group")
public class Group implements Serializable {
    @TableId(type = IdType.AUTO)
    private Long g_id;
    private String g_name;
    private Long g_leader_id;
    private String g_desc;
    private Long a_id;
}
