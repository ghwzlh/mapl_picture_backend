package com.ghw.maplpicturebackend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
@MapperScan("com.ghw.maplpicturebackend.mapper")
@EnableAspectJAutoProxy(exposeProxy = true) //使用AopContext.currentProxy()获取当前类的代理对象，使用声明式事务时类自调用事务失效时，可以使用
public class MaplPictureBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(MaplPictureBackendApplication.class, args);
    }
}