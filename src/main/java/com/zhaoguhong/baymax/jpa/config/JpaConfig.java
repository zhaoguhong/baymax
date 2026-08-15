package com.zhaoguhong.baymax.jpa.config;

import com.zhaoguhong.baymax.jpa.BaseRepositoryImpl;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * @author guhong
 * @date 2019/5/13
 */
@Configuration
@EnableJpaRepositories(
    basePackages = "com.zhaoguhong.baymax",
    repositoryBaseClass = BaseRepositoryImpl.class)
public class JpaConfig {

}
