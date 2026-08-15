package com.zhaoguhong.baymax.security.config;

import org.apereo.cas.client.session.SingleSignOutFilter;
import org.apereo.cas.client.validation.Cas20ServiceTicketValidator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.cas.ServiceProperties;
import org.springframework.security.cas.authentication.CasAssertionAuthenticationToken;
import org.springframework.security.cas.authentication.CasAuthenticationProvider;
import org.springframework.security.cas.web.CasAuthenticationEntryPoint;
import org.springframework.security.cas.web.CasAuthenticationFilter;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;

/**
 * CAS 单点登录配置。仅在 cas.enable=true 时加载。
 *
 * @author guhong
 * @date 2019/5/17
 */
@Configuration
@ConditionalOnProperty(prefix = "cas", name = "enable", havingValue = "true")
public class CasSecurityConfig {

  @Bean
  public ServiceProperties serviceProperties(CasProperties casProperties) {
    ServiceProperties serviceProperties = new ServiceProperties();
    serviceProperties.setService(casProperties.getClientCasUrl());
    return serviceProperties;
  }

  @Bean
  public CasAuthenticationEntryPoint casAuthenticationEntryPoint(
      CasProperties casProperties, ServiceProperties serviceProperties) {
    CasAuthenticationEntryPoint entryPoint = new CasAuthenticationEntryPoint();
    entryPoint.setLoginUrl(casProperties.getLoginUrl());
    entryPoint.setServiceProperties(serviceProperties);
    return entryPoint;
  }

  @Bean
  public Cas20ServiceTicketValidator ticketValidator(CasProperties casProperties) {
    return new Cas20ServiceTicketValidator(casProperties.getServerUrl());
  }

  @Bean
  public CasAuthenticationProvider casAuthenticationProvider(
      ServiceProperties serviceProperties,
      Cas20ServiceTicketValidator ticketValidator,
      AuthenticationUserDetailsService<CasAssertionAuthenticationToken> userDetailsService) {
    CasAuthenticationProvider provider = new CasAuthenticationProvider();
    provider.setServiceProperties(serviceProperties);
    provider.setTicketValidator(ticketValidator);
    provider.setAuthenticationUserDetailsService(userDetailsService);
    provider.setKey("CAS_PROVIDER_BAYMAX");
    return provider;
  }

  @Bean
  public SingleSignOutFilter singleSignOutFilter() {
    SingleSignOutFilter filter = new SingleSignOutFilter();
    filter.setIgnoreInitConfiguration(true);
    return filter;
  }

  @Bean
  public CasAuthenticationFilter casAuthenticationFilter(
      ServiceProperties serviceProperties, AuthenticationManager authenticationManager) {
    CasAuthenticationFilter filter = new CasAuthenticationFilter();
    filter.setServiceProperties(serviceProperties);
    filter.setAuthenticationManager(authenticationManager);
    return filter;
  }
}
