package com.zhaoguhong.baymax.mybatis.config;

import org.springframework.context.annotation.Configuration;

/**
 * @author guhong
 * @date 2019/5/13
 */
@Configuration
@tk.mybatis.spring.annotation.MapperScan(basePackages = "com.zhaoguhong.baymax.*.dao")
public class MybatisConfig {
}
