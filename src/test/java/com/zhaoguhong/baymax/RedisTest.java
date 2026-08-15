package com.zhaoguhong.baymax;

import jakarta.annotation.Resource;
import java.util.UUID;
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
    String key = "baymax:integration:" + UUID.randomUUID();
    try {
      redisTemplate.opsForValue().set(key, "testValue");
      Assertions.assertEquals("testValue", redisTemplate.opsForValue().get(key));
    } finally {
      redisTemplate.delete(key);
    }
    Assertions.assertNull(redisTemplate.opsForValue().get(key));
  }

}
