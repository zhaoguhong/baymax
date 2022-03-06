package com.zhaoguhong.baymax;

import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootTest
@Slf4j
public class RedisTest {

  @Resource
  private RedisTemplate<String, Object> redisTemplate;

  @Test
  public void testRedisTemplate() {
    redisTemplate.opsForValue().set("test", "testValue");
    Assertions.assertEquals(redisTemplate.opsForValue().get("test"),"testValue");
    redisTemplate.delete("test");
    Assertions.assertEquals(redisTemplate.opsForValue().get("test"),null);
  }

}

