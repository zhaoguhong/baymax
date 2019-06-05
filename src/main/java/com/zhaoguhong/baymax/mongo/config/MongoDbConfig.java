package com.zhaoguhong.baymax.mongo.config;

import com.zhaoguhong.baymax.mongo.MyMongoTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.convert.MongoConverter;

/**
 * MongoDbConfig
 *
 * @author guhong
 * @date 2019/6/5
 */
@Configuration
public class MongoDbConfig {

  /**
   * 扩展自己的mogoTemplate
   */
  @Bean
  public MyMongoTemplate mongoTemplate(MongoDbFactory mongoDbFactory,
      MongoConverter converter) {
    return new MyMongoTemplate(mongoDbFactory, converter);
  }

}
