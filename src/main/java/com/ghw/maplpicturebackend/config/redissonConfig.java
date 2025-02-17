package com.ghw.maplpicturebackend.config;

import lombok.Data;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class redissonConfig {


    @Bean
    public RedissonClient RedisConfig() {
        Config config = new Config();
        String redissonAddress = "redis://127.0.0.1:6379";
        config.useSingleServer().setAddress(redissonAddress).setDatabase(0).setPassword("040414@ghw");

        return Redisson.create(config);
    }
}
