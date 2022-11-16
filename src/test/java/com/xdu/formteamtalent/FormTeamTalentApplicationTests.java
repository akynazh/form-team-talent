package com.xdu.formteamtalent;

import com.xdu.formteamtalent.entity.User;
import com.xdu.formteamtalent.service.UserService;
import com.xdu.formteamtalent.service.impl.UserServiceImpl;
import com.xdu.formteamtalent.utils.JwtUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

@SpringBootTest
class FormTeamTalentApplicationTests {
    @Autowired
    UserService userService;

    @Test
    public void getTokenForTest() {
        String token = JwtUtil.createToken("test1");
        User user = new User();
        user.setU_id("test1");
        user.setU_name("mary");
        user.setU_school("xdu");
        user.setU_stu_num("2000913sdaaf");
        userService.save(user);
        System.out.println(token);
    }
    @Test
    public void test1() {
        // 获取当前格式化时间
        String sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        // 获取格式化时间对应的时间戳（秒级）
        long timestamp = (Timestamp.valueOf(sdf).getTime()) / 1000;

        System.out.println(sdf);
        System.out.println(timestamp);

        String sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(timestamp * 1000));
        System.out.println(sdf1);
    }

    @Test
    public void test2() {
        System.out.println(System.currentTimeMillis());
    }
}
