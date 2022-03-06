package com.zhaoguhong.baymax;


import com.zhaoguhong.baymax.common.Page;
import com.zhaoguhong.baymax.mongo.MyMongoTemplate;
import com.zhaoguhong.baymax.mongo.entity.MongoDemo;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

@SpringBootTest
@Slf4j
public class MongoDbTest {

  @Autowired
  private MyMongoTemplate mongoTemplate;
  @Test
  public void tesSave() {
    MongoDemo demo = new MongoDemo();
    demo.setTitle("Spring Boot 中使用 MongoDB");
    demo.setDescription("MongoDB");
    mongoTemplate.save(demo);
    log.info("测试保存:{}",demo.getId());
  }

  @Test
  public void tesQuery() {
    Query query = new Query(Criteria.where("id").is("5cf7867b12e51d0aa71ae253"));
    MongoDemo demoEntity = mongoTemplate.findOne(query, MongoDemo.class);
    System.out.println(demoEntity.getId());
  }

  /**
   * 测试排序
   */
  @Test
  public void testSort() {
    Query query = new Query().with(Sort.by("id"));
    List<MongoDemo> demos = mongoTemplate.find(query, MongoDemo.class);
    log.info("测试正序:{}",demos);
    Query query1 = new Query().with(Sort.by(Direction.DESC,"id"));
    List<MongoDemo> demos2 = mongoTemplate.find(query1, MongoDemo.class);
    log.info("测试倒序:{}",demos2);
  }

  /**
   * 测试分页
   */
  @Test
  public void testPage() {
    Pageable pageable = PageRequest.of(1, 3);
    Query query = new Query().with(pageable);
    long count = mongoTemplate.count(query, MongoDemo.class);
    List<MongoDemo> demos = mongoTemplate.find(query, MongoDemo.class);
    log.info("测试分页总数量:{}",count);
    log.info("测试分页:{}",demos);
  }

  /**
   * 测试分页
   */
  @Test
  public void testPage1() {
    Page<MongoDemo> page = new Page<MongoDemo>(1,3);
    mongoTemplate.find(page ,null, MongoDemo.class);
    log.info("测试分页总数量:{}",page.getTotalCount());
    log.info("测试分页:{}",page.getEntityList());
  }

}

