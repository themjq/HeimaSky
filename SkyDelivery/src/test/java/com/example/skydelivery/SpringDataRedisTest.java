package com.example.skydelivery;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.TimeUnit;

//@SpringBootTest
public class SpringDataRedisTest {
    @Autowired
    private RedisTemplate redisTemplate;
    @Test
    public void testRedisTemplete(){
        System.out.println(redisTemplate);

    }
    @Test
    public void testString(){
        redisTemplate.opsForValue().set("city","北京");
        String city = (String) redisTemplate.opsForValue().get("city");
        System.out.println(city);
        redisTemplate.opsForValue().set("code","1234",3, TimeUnit.MINUTES);
        redisTemplate.opsForValue().setIfAbsent("lock","1");
        redisTemplate.opsForValue().setIfAbsent("lock","2");
    }
    @Test
    public void testHash(){
        HashOperations hashOperations = redisTemplate.opsForHash();
        hashOperations.put("100","name","tom");
        hashOperations.put("100","age","20");
        String name=(String)hashOperations.get("100","age");
        System.out.println(name);
        System.out.println(hashOperations.keys("100"));
        System.out.println(hashOperations.values("100"));

        hashOperations.delete("100","age");
    }
}
