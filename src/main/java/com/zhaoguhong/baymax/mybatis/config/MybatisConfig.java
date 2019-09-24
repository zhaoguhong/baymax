package com.zhaoguhong.baymax.mybatis.config;

import com.github.pagehelper.autoconfigure.PageHelperAutoConfiguration;
import com.zhaoguhong.baymax.mybatis.interceptor.MyPageInterceptor;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

/**
 * @author guhong
 * @date 2019/5/13
 */
@Configuration
// 设置mapper扫描的包
@tk.mybatis.spring.annotation.MapperScan(basePackages = "com.zhaoguhong.baymax.**.dao")
@Slf4j
public class MybatisConfig {

  @Autowired
  private List<SqlSessionFactory> sqlSessionFactoryList;

  /**
   * 添加自定义的分页插件，pageHelper 的分页插件PageInterceptor是用@PostConstruct添加的，自定义的应该在其后面添加
   * 真正执行时顺序是反过来，先执行MyPageInterceptor，再执行 PageInterceptor
   *
   * 所以要保证 PageHelperAutoConfiguration 先执行
   */
  @Autowired
  public void addPageInterceptor(PageHelperAutoConfiguration pageHelperAutoConfiguration) {
    MyPageInterceptor interceptor = new MyPageInterceptor();
    for (SqlSessionFactory sqlSessionFactory : sqlSessionFactoryList) {
      sqlSessionFactory.getConfiguration().addInterceptor(interceptor);
      log.info("注册自定义分页插件成功");
    }
  }

}
