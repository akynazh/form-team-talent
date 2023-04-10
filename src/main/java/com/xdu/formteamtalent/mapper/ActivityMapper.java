package com.xdu.formteamtalent.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xdu.formteamtalent.entity.Activity;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface ActivityMapper extends BaseMapper<Activity> {

    @Select("SELECT * FROM t_activity WHERE a_is_public=1 AND status=1 LIMIT #{count}")
    List<Activity> getPublicActivity(Integer count);
}
