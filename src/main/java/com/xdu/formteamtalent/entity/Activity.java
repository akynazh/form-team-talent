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
@TableName("t_activity")
public class Activity implements Serializable {
    @TableId
    private String a_id;
    private String a_name;
    private String a_holder_id;
    private String a_desc;
    private String a_end_date;
    private Integer a_is_public;
    private String a_qrcode_path;
    private Integer status;
}
