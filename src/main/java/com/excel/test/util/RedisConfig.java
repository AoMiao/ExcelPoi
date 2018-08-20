package com.excel.test.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.JedisClientConfigurationBuilderCustomizer;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration.JedisClientConfigurationBuilder;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import redis.clients.jedis.JedisPoolConfig;

/**
* <p>
* create by AooMiao on 2018-08-20
*/
@Configuration
@EnableAutoConfiguration
public class RedisConfig {

    /**
     * 注入 RedisConnectionFactory,这里会默认注入LettuceConnectionFactory，如果要用jedis需要手动注册bean
     */
    
    @Autowired
    JedisConfig jedisConfig;
    @Autowired
    JedisConnectionFactory jedisConnectionFactory;
    
    
    
    @Bean
    public JedisConnectionFactory getConnectionFactory() {
        RedisStandaloneConfiguration rf=new RedisStandaloneConfiguration();
        rf.setDatabase(jedisConfig.database);
        rf.setHostName(jedisConfig.host);
        rf.setPort(jedisConfig.port);
        //int to=Integer.parseInt(jedisConfig.timeout.substring(0,jedisConfig.timeout.length()-2));
        //JedisClientConfiguration.JedisClientConfigurationBuilder jedisClientConfiguration = JedisClientConfiguration.builder();
        //jedisClientConfiguration.connectTimeout(Duration.ofMillis(to));
        JedisClientConfiguration.JedisPoolingClientConfigurationBuilder jpb=
                (JedisClientConfiguration.JedisPoolingClientConfigurationBuilder)JedisClientConfiguration.builder();
        JedisPoolConfig jedisPoolConfig=new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(jedisConfig.maxIdle);
        jedisPoolConfig.setMinIdle(jedisConfig.minIdle);
        jedisPoolConfig.setMaxTotal(jedisConfig.maxActive);
        jedisPoolConfig.setMaxWaitMillis(Long.valueOf(jedisConfig.maxWait));
        jpb.poolConfig(jedisPoolConfig);
        JedisConnectionFactory jedisConnectionFactory=new JedisConnectionFactory(rf,jpb.build());
        return jedisConnectionFactory;
    }

    @Bean
    public RedisTemplate<String, Object> getRedisTemplate() {
        System.out.println("redisConnectionFactory:" + getConnectionFactory());
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(getConnectionFactory());
        return redisTemplate;
    }

}
