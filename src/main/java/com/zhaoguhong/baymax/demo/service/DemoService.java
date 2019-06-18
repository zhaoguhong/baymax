package com.zhaoguhong.baymax.demo.service;

import com.zhaoguhong.baymax.demo.entity.Demo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * @author guhong
 * @date 2019/6/14
 */
@Slf4j
@Service
public class DemoService {

  @Cacheable(value="mycache")
  public Demo getDemoByCache() {
    log.info("加载 demo 数据");
   return Demo.builder().build();
  }

  @CacheEvict(value="mycache")
  public void removeCache() {
    log.info("删除 demo 数据");
  }

}
