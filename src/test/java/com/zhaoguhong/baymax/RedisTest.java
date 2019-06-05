package com.zhaoguhong.baymax;


import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class RedisTest {

  @Resource
  private RedisTemplate<String, Object> redisTemplate;

  @Test
  public void testRedisTemplate() {
    redisTemplate.opsForValue().set("test", "testValue");
    Assert.assertEquals(redisTemplate.opsForValue().get("test"),"testValue");
    redisTemplate.delete("test");
    Assert.assertEquals(redisTemplate.opsForValue().get("test"),null);
  }

}

