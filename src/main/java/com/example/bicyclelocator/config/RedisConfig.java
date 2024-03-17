package com.example.bicyclelocator.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.Jedis;

@Configuration
public class RedisConfig {
    @Bean
    public Jedis jedis() {
        return new Jedis("localhost", 6379); // replace "localhost" with your Redis server address
    }
}
