package com.zhaoguhong.baymax.security.config;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * web安全相关配置
 * @author guhong
 * @date 2019/5/17
 */
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  @Autowired
  private SecurityProperties securityProperties;

  @Autowired
  private UserDetailsService userDetailsService;

  @Override
  protected void configure(HttpSecurity http) throws Exception {

    http
        .authorizeRequests()
        // 设置可以匿名访问的url
        .antMatchers(securityProperties.getAnonymousArray()).permitAll()
        // 其它所有请求都要认证
        .anyRequest().authenticated()
        .and()
        .formLogin()
        // 自定义登录页
        .loginPage(securityProperties.getLoginPage())
        // 自定义登录请求路径
        .loginProcessingUrl(securityProperties.getLoginProcessingUrl())
        .permitAll()
        .and()
        .logout()
        .permitAll();

    // 禁用CSRF
    http.csrf().disable();
  }


  @Override
  public void configure(WebSecurity web) throws Exception {
    String[] ignoringArray = securityProperties.getIgnoringArray();
    // 忽略的资源，直接跳过spring security权限校验
    if (ArrayUtils.isNotEmpty(ignoringArray)) {
      web.ignoring().antMatchers(ignoringArray);
    }
  }

  /**
   *
   * 声明密码加密方式
   */
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth)
      throws Exception {
    auth.userDetailsService(userDetailsService)
        // 配置密码加密方式，也可以不指定，默认就是BCryptPasswordEncoder
        .passwordEncoder(passwordEncoder());
  }


}
