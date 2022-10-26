package com.xdu.formteamtalent.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_user_team")
public class UserTeam {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String u_id;
    private Long t_id;
}
