package com.xdu.formteamtalent.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xdu.formteamtalent.entity.JoinRequest;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface JoinRequestMapper extends BaseMapper<JoinRequest> {

    @Select("SELECT * FROM t_req WHERE from_id = #{u_id} OR to_id = #{u_id}")
    List<JoinRequest> getMyRequest(String u_id);
}
