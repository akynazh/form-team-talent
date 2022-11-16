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
@TableName("t_req")
public class JoinRequest {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String from_id;
    private String to_id;
    private String a_id;
    private String t_id;
    private String t_name;
    private String u_name;
    private String content;
    private String send_date;
    private Integer agree;
    private Integer status;
}
