package com.zhaoguhong.baymax;

import com.zhaoguhong.baymax.jdbc.JdbcDao;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

@SpringBootTest
@Slf4j
public class SpringJdbcTest {

  @Autowired
  private JdbcDao jdbcDao;

  /**
   * simpleJdbcInsert新增
   */
  @Test
  public void testInsert() throws Exception {
    SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcDao.getJdbcTemplate());
    Map<String, Object> parameters = new HashMap<String, Object>();
    parameters.put("user_name", "小明");
    parameters.put("age", 1);

    Number primaryKey = simpleJdbcInsert.withTableName("demo")
        .withCatalogName("baymax")
        .usingGeneratedKeyColumns("id")// 指定主键列名
        .executeAndReturnKey(parameters);
    log.info("新增成功，主键为：{}", primaryKey);
  }

  @Test
  public void testQuery() throws Exception {
    String sql = "select * from demo";
    log.info("测试查询：{}", jdbcDao.find(sql));

    String sql1 = "select * from demo where id = ?";
    log.info("测试占位符：{}", jdbcDao.find(sql1, 1L));

    String sql2 = "select * from demo where id = :id";
    Map<String, Object> param = new HashMap<>();
    param.put("id", 1);
    log.info("测试命名参数：{}", jdbcDao.find(sql2, param));

  }

}
