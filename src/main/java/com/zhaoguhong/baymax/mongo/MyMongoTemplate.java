package com.zhaoguhong.baymax.mongo;

import com.zhaoguhong.baymax.common.Page;
import java.util.List;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.MongoConverter;
import org.springframework.data.mongodb.core.query.Query;

/**
 * 扩展自己的 MongoTemplate
 *
 * @author guhong
 */
public class MyMongoTemplate extends MongoTemplate {


  public MyMongoTemplate(MongoDatabaseFactory factory, MongoConverter converter) {
    super(factory, converter);
  }

  /**
   * 分页
   *
   * @param page
   * @param query
   * @param entityClass
   * @param <T>
   * @return
   */
  public <T> Page<T> find(Page<T> page, Query query, Class<T> entityClass) {
    if (query == null) {
      query = new Query();
    }
    long count = this.count(query, entityClass);
    page.setTotalCount((int) count);

    Pageable pageable = PageRequest.of(page.getPageNo() - 1, page.getPageSize());
    query.with(pageable);
    List<T> entitys = this.find(query, entityClass);
    page.setEntityList(entitys);
    return page;
  }


}
