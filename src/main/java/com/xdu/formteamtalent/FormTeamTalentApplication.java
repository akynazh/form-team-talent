package com.xdu.formteamtalent;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.xdu.formteamtalent.mapper")
public class FormTeamTalentApplication {

    public static void main(String[] args) {
        SpringApplication.run(FormTeamTalentApplication.class, args);
    }

}
