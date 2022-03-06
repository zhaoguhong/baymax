package com.zhaoguhong.baymax;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
public class LogTest {

  @Test
  public void testLog(){
    log.debug("------------debug------------");
    log.info("------------info------------");
    log.warn("------------warn------------");
  }


}

