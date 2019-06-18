package com.zhaoguhong.baymax;

import com.zhaoguhong.baymax.demo.service.DemoService;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
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

