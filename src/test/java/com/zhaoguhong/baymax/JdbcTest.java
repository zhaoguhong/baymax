package com.zhaoguhong.baymax;

import com.zhaoguhong.baymax.common.Page;
import com.zhaoguhong.baymax.demo.entity.Demo;
import com.zhaoguhong.baymax.jdbc.JdbcDao;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class JdbcTest {

  @Resource
  private JdbcDao jdbcDao;

  @Test
  public void testJdbcDao() {
    String sql = "select * from Demo where id =?";
    log.info("测试jdbcDao占位符:" + jdbcDao.find(sql, 1L));

    sql = "select * from Demo where id =:id";
    Map<String, Object> param = new HashMap<>();
    param.put("id", 1L);
    log.info("测试jdbcDao命名参数:" + jdbcDao.find(sql, param));

    sql = "select * from Demo ";
    Page page = new Page<Demo>();
    log.info("测试分页:" + jdbcDao.find(page, sql, new HashMap<>()));
  }


}

