package com.example.vitabuddy;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync(proxyTargetClass = true)
@ComponentScan(basePackages = { "com.example.vitabuddy" })
@MapperScan(basePackages = { "com.example.vitabuddy" })

//DB 연결정보 : 프로퍼티 사용
//로컬 경로 / 서버 경로
@PropertySources({
@PropertySource(value={"file:c:/webservice/vitabuddy/configure.properties",
"file:/usr/local/project/properties/configure.properties", },
ignoreResourceNotFound=true)
})
public class VitabuddyApplication {

    public static void main(String[] args) {
        SpringApplication.run(VitabuddyApplication.class, args);
    }

}
