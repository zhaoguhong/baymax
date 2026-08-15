package com.zhaoguhong.baymax.mybatis.config;

import com.github.pagehelper.PageInterceptor;
import com.zhaoguhong.baymax.mybatis.interceptor.MyPageInterceptor;
import java.util.Properties;
import org.apache.ibatis.plugin.Interceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

/**
 * MyBatis、TK Mapper 与 PageHelper 的组合配置。
 *
 * @author guhong
 * @date 2019/5/13
 */
@Configuration
@tk.mybatis.spring.annotation.MapperScan(basePackages = "com.zhaoguhong.baymax.**.dao")
public class MybatisConfig {

  /**
   * 先注册 PageHelper。MyBatis 以反向顺序执行插件，因此后注册的 MyPageInterceptor
   * 会先把自定义 Page 转换成 PageRowBounds，再由 PageHelper 生成分页 SQL。
   */
  @Bean
  @Order(0)
  public Interceptor pageInterceptor(
      @Value("${pagehelper.helper-dialect:mysql}") String helperDialect,
      @Value("${pagehelper.reasonable:true}") boolean reasonable) {
    Properties properties = new Properties();
    properties.setProperty("helperDialect", helperDialect);
    properties.setProperty("reasonable", Boolean.toString(reasonable));
    PageInterceptor interceptor = new PageInterceptor();
    interceptor.setProperties(properties);
    return interceptor;
  }

  @Bean
  @Order(1)
  public Interceptor myPageInterceptor() {
    return new MyPageInterceptor();
  }
}
