package com.xdu.formteamtalent;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

@SpringBootTest
class FormTeamTalentApplicationTests {

    @Test
    public void test1() {
        // 获取当前格式化时间
        String sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        // 获取格式化时间对应的时间戳（秒级）
        long timestamp = (Timestamp.valueOf(sdf).getTime()) / 1000;

        System.out.println(sdf);
        System.out.println(timestamp);
    }

    @Test
    public void test2() {
        // 获取当前格式化时间
        String sdf = new SimpleDateFormat("HH:mm:ss").format(new Date());

        System.out.println(sdf);
    }
}
