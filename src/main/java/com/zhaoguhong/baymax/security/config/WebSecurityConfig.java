package com.zhaoguhong.baymax.security.config;

import com.zhaoguhong.baymax.security.handler.MyAuthenticationFailureHandler;
import com.zhaoguhong.baymax.security.handler.MyAuthenticationSuccessHandler;
import com.zhaoguhong.baymax.security.handler.MyLoginUrlAuthenticationEntryPoint;
import com.zhaoguhong.baymax.security.handler.MyLogoutSuccessHandler;
import com.zhaoguhong.baymax.swagger.SwaggerConfig;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apereo.cas.client.session.SingleSignOutFilter;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.cas.authentication.CasAuthenticationProvider;
import org.springframework.security.cas.web.CasAuthenticationEntryPoint;
import org.springframework.security.cas.web.CasAuthenticationFilter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

/**
 * Web 安全相关配置。
 *
 * @author guhong
 * @date 2019/5/17
 */
@Configuration
public class WebSecurityConfig {

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public AuthenticationManager authenticationManager(
      UserDetailsService userDetailsService,
      PasswordEncoder passwordEncoder,
      ObjectProvider<CasAuthenticationProvider> casAuthenticationProvider) {
    DaoAuthenticationProvider daoAuthenticationProvider =
        new DaoAuthenticationProvider(userDetailsService);
    daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);

    List<AuthenticationProvider> providers = new ArrayList<>();
    providers.add(daoAuthenticationProvider);
    casAuthenticationProvider.ifAvailable(providers::add);
    return new ProviderManager(providers);
  }

  @Bean
  public SecurityFilterChain securityFilterChain(
      HttpSecurity http,
      SecurityProperties securityProperties,
      MyLogoutSuccessHandler logoutSuccessHandler,
      MyAuthenticationSuccessHandler authenticationSuccessHandler,
      AuthenticationManager authenticationManager,
      ObjectProvider<CasAuthenticationFilter> casAuthenticationFilter,
      ObjectProvider<SingleSignOutFilter> singleSignOutFilter,
      ObjectProvider<CasAuthenticationEntryPoint> casAuthenticationEntryPoint) throws Exception {
    List<String> permitPatterns = new ArrayList<>();
    permitPatterns.addAll(Arrays.asList(securityProperties.getAnonymousArray()));
    permitPatterns.addAll(Arrays.asList(securityProperties.getIgnoringArray()));
    permitPatterns.addAll(Arrays.asList(SwaggerConfig.ACCESS_PREFIX.split(",")));

    http
        .authenticationManager(authenticationManager)
        .authorizeHttpRequests(authorize -> {
          if (!permitPatterns.isEmpty()) {
            authorize.requestMatchers(permitPatterns.toArray(String[]::new)).permitAll();
          }
          authorize.anyRequest().authenticated();
        })
        .formLogin(form -> form
            .loginPage(securityProperties.getLoginPage())
            .successHandler(authenticationSuccessHandler)
            .failureHandler(
                new MyAuthenticationFailureHandler(securityProperties.getLoginPage()))
            .loginProcessingUrl(securityProperties.getLoginProcessingUrl())
            .permitAll())
        .logout(logout -> logout
            .logoutSuccessHandler(logoutSuccessHandler)
            .permitAll())
        .exceptionHandling(exceptions -> exceptions.authenticationEntryPoint(
            new MyLoginUrlAuthenticationEntryPoint(securityProperties.getLoginPage())))
        .csrf(AbstractHttpConfigurer::disable);

    CasAuthenticationFilter casFilter = casAuthenticationFilter.getIfAvailable();
    SingleSignOutFilter signOutFilter = singleSignOutFilter.getIfAvailable();
    CasAuthenticationEntryPoint casEntryPoint = casAuthenticationEntryPoint.getIfAvailable();
    if (casFilter != null && signOutFilter != null && casEntryPoint != null) {
      http
          .addFilterBefore(casFilter, BasicAuthenticationFilter.class)
          .addFilterBefore(signOutFilter, CasAuthenticationFilter.class)
          .exceptionHandling(exceptions -> exceptions.authenticationEntryPoint(casEntryPoint));
    }

    return http.build();
  }
}
