package com.excel.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.test.context.junit4.SpringRunner;

/**
* <p>
* create by AooMiao on 2018-08-20
*/
@SpringBootTest
@RunWith(SpringRunner.class)
@Component
public class TestRedis {
 
    @Autowired
    private RedisTemplate redisTemplate;
    
    @Test
    public void set(){
        redisTemplate.opsForValue().set("test:set","testValue1");
        System.out.println("redisTemplate:"+redisTemplate.getConnectionFactory());
    }
}