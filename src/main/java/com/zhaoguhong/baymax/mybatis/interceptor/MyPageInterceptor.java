package com.zhaoguhong.baymax.mybatis.interceptor;

import com.github.pagehelper.PageRowBounds;
import com.zhaoguhong.baymax.common.Page;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;


@Intercepts({
    @Signature(type = Executor.class, method = "query",
        args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
    @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class,
        RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class}),
})
public class MyPageInterceptor implements Interceptor {

  @Override
  public Object intercept(Invocation invocation) throws Throwable {
    Object[] args = invocation.getArgs();
    Object parameter = args[1];
    Page<?> page = null;

    if (parameter instanceof Page) {
      page = (Page<?>) parameter;
    } else if (parameter instanceof Map) {
      for (Object value : ((Map<String, Object>) parameter).values()) {
        if (value instanceof Page) {
          page = (Page) value;
          break;
        }
      }
    }

    PageRowBounds pageRowBounds = null;
    if (page != null) {
      pageRowBounds =
          new PageRowBounds(page.getPageSize() * (page.getPageNo() - 1), page.getPageSize());
      pageRowBounds.setCount(true);
      args[2] = pageRowBounds;
    }
    Object value = invocation.proceed();

    if (page != null) {
      page.setEntityList((List) value);
      page.setTotalCount(pageRowBounds.getTotal().intValue());
    }

    return value;
  }

  @Override
  public Object plugin(Object target) {
    if (target instanceof Executor) {
      return Plugin.wrap(target, this);
    } else {
      return target;
    }
  }

  @Override
  public void setProperties(Properties properties) {

  }

}
