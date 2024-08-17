package com.atguigu.cloud.config;

import feign.Logger;
import feign.Retryer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig {
    @Bean
    public Retryer myRetryer(){
        // Fegin的默认配置是不走重试策略
        return Retryer.NEVER_RETRY;
        // 最大的请求次数为3(1+2,解释：1表示初始一次，2表示重试2次),初始间隔为100ms,重试最大间隔为1s
//        return new Retryer.Default(100,1,3);
    }

    /**
     * Feign默认是不开启日志的，用的是NONE，想要看到所有日志的话，就必须手动开启
     * @return
     */
    @Bean
    Logger.Level feignLoggerLevel(){
        return Logger.Level.FULL;
    }
}
