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
@TableName("t_activity")
public class Activity {
    @TableId(type = IdType.AUTO)
    private Long a_id;
    private String a_name;
    private Long a_holder_id;
    private String a_desc;
    private String a_end_date;
    private Integer a_is_public;
}
