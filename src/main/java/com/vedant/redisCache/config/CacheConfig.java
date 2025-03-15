package com.vedant.redisCache.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

@Configuration
@EnableCaching
public class CacheConfig {


    @Bean
    public LettuceConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration();
//        redisConfig.setHostName("redis-15851.crce182.ap-south-1-1.ec2.redns.redis-cloud.com");
//        redisConfig.setPort(15851);
//        redisConfig.setPassword(RedisPassword.of("QQiuraGAf8Bm18IT3Np5CBNlNL1zFMeC")); // Use your actual Redis Cloud password
        redisConfig.setHostName("localhost");
        redisConfig.setPort(6379);
        redisConfig.setPassword(RedisPassword.of("vedant")); // Use your actual Redis Cloud password

        return new LettuceConnectionFactory(redisConfig);
    }

    @Bean
    public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {

        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                .prefixCacheNameWith("my-redis-")
                .entryTtl(Duration.ofSeconds(6000))
                .enableTimeToIdle() // if we have employee with id 3 and when we again get it then its ttl again is reset to default here it is 6000
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer())) //keys are normally serialized
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer())); //values are serialized to json

        return RedisCacheManager.builder(redisConnectionFactory)
                .cacheDefaults(redisCacheConfiguration)
                .build();
    }

}