package com.zhaoguhong.baymax;

import com.zhaoguhong.baymax.demo.service.DemoService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;

@SpringBootTest(properties = "spring.cache.redis.key-prefix=baymax-test")
@Slf4j
public class SpringCacheTest {

  @Resource
  private DemoService demoService;

  @Resource
  private CacheManager cacheManager;

  @Test
  public void testSpringCache() {
    log.info("cacheManage:{}", cacheManager);
    demoService.getDemoByCache();
    demoService.getDemoByCache();
    demoService.removeCache();
    demoService.getDemoByCache();
    demoService.getDemoByCache();
  }


}
