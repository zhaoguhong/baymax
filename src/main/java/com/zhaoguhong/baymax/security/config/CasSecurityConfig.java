package com.zhaoguhong.baymax.security.config;

import org.jasig.cas.client.session.SingleSignOutFilter;
import org.jasig.cas.client.validation.Cas20ServiceTicketValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.cas.ServiceProperties;
import org.springframework.security.cas.authentication.CasAssertionAuthenticationToken;
import org.springframework.security.cas.authentication.CasAuthenticationProvider;
import org.springframework.security.cas.web.CasAuthenticationEntryPoint;
import org.springframework.security.cas.web.CasAuthenticationFilter;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.stereotype.Component;

/**
 * @author guhong
 * @date 2019/5/17
 */
@Component
public class CasSecurityConfig extends WebSecurityConfig {

  @Autowired
  private CasProperties casProperties;
  @Autowired
  private AuthenticationUserDetailsService<CasAssertionAuthenticationToken> casAuthenticationUserDetailsService;

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    super.configure(http);
    if (casProperties.isEnable()) {
      http.authorizeRequests()
          // 添加 cas 认知 fifter
          .and().addFilterBefore(casAuthenticationFilter(), BasicAuthenticationFilter.class)
          // 添加 cas 登出 fifter
          .addFilterBefore(singleSignOutFilter(), CasAuthenticationFilter.class)
          .exceptionHandling()
          // cas认证入口
          .authenticationEntryPoint(casAuthenticationEntryPoint());
    }

  }

  /**
   * 配置单点项目信息
   */
  @Bean
  public ServiceProperties serviceProperties() {
    ServiceProperties sp = new ServiceProperties();
    // 单点登录成功回调地址
    sp.setService(casProperties.getClientCasUrl());
    return sp;
  }

  /**
   * cas认证入口
   */
  @Bean
  public CasAuthenticationEntryPoint casAuthenticationEntryPoint() {
    CasAuthenticationEntryPoint casAuthenticationEntryPoint = new CasAuthenticationEntryPoint();
    casAuthenticationEntryPoint.setLoginUrl(casProperties.getLoginUrl());
    casAuthenticationEntryPoint.setServiceProperties(serviceProperties());
    return casAuthenticationEntryPoint;
  }

  /**
   * cas校验器
   */
  @Bean
  public Cas20ServiceTicketValidator ticketValidator() {
    return new Cas20ServiceTicketValidator(casProperties.getServerUrl());
  }

  /**
   * cas认证Provider
   */
  @Bean
  public CasAuthenticationProvider casAuthenticationProvider() {

    CasAuthenticationProvider provider = new CasAuthenticationProvider();
    provider.setServiceProperties(serviceProperties());
    provider.setTicketValidator(ticketValidator());
    // 设置UserDetailsService
    provider.setAuthenticationUserDetailsService(casAuthenticationUserDetailsService);
    provider.setKey("CAS_PROVIDER_LOCALHOST_8123");
    return provider;
  }


  @Autowired
  public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
    // 设置cas认证provider
    auth.authenticationProvider(casAuthenticationProvider());
  }

  /**
   * 单点登出过滤器
   */
  @Bean
  public SingleSignOutFilter singleSignOutFilter() {
    SingleSignOutFilter singleSignOutFilter = new SingleSignOutFilter();
    singleSignOutFilter.setCasServerUrlPrefix(casProperties.getServerUrl());
    singleSignOutFilter.setIgnoreInitConfiguration(true);
    return singleSignOutFilter;
  }

  /**
   * 设置认证 Provider 为 cas 认证
   */
  @Override
  protected void configure(AuthenticationManagerBuilder auth)
      throws Exception {
    super.configure(auth);
    if (casProperties.isEnable()) {
      auth.authenticationProvider(casAuthenticationProvider());
    }
  }

  /**
   * cas认证过滤器
   */
  @Bean
  public CasAuthenticationFilter casAuthenticationFilter()
      throws Exception {
    CasAuthenticationFilter filter = new CasAuthenticationFilter();
    filter.setServiceProperties(serviceProperties());

    // 设置认证manager
    filter.setAuthenticationManager(authenticationManager());
    // 可以手动设置认证成功处理器，和认证失败处理器
    // filter.setAuthenticationFailureHandler(authenticationFailureHandler);
    // filter.setAuthenticationSuccessHandler(simpleUrlAuthenticationSuccessHandler());
    // 设置客户端cas登录url，默认值是 /login/cas
    //filter.setFilterProcessesUrl("/login/cas")
    return filter;
  }


}
