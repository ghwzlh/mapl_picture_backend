package com.ghw.maplpicturebackend.config;

import com.baomidou.mybatisplus.autoconfigure.ConfigurationCustomizer;
import com.ghw.maplpicturebackend.aop.SqlInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyBatisConfig {
    @Bean
    public ConfigurationCustomizer mybatisConfigurationCustomizer() {
        return configuration -> configuration.addInterceptor(new SqlInterceptor());
    }
}

