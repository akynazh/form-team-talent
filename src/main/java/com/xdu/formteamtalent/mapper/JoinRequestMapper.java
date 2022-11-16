package com.xdu.formteamtalent.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xdu.formteamtalent.entity.JoinRequest;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface JoinRequestMapper extends BaseMapper<JoinRequest> {

    @Select("SELECT * FROM t_req WHERE from_id = #{u_id} OR to_id = #{u_id}")
    List<JoinRequest> getMyRequest(String u_id);

    @Select("Select * FROM t_req WHERE from_id = #{from_id} AND to_id = #{to_id} " +
            "AND a_id = #{a_id} AND t_id = #{t_id}")
    JoinRequest checkRequestExist(String from_id, String to_id, String a_id, String t_id);
}
