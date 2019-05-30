package com.zhaoguhong.baymax.security.config;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 安全相关配置项
 *
 * @author guhong
 */
@Component
@Getter
@Setter
public class SecurityProperties {

  /**
   * 匿名访问的url
   */
  @Value("${baymax.security.anonymous:}")
  private String anonymous;

  /**
   * 忽略的资源,直接跳过spring security权限校验,一般是用做静态资源
   */
  @Value("${baymax.security.ignoring:}")
  private String ignoring;

  /**
   * 登录页面
   */
  @Value("${baymax.security.loginPage}")
  private String loginPage;

  /**
   * 登录请求路径
   */
  @Value("${baymax.security.loginProcessingUrl}")
  private String loginProcessingUrl;

  public String[] getAnonymousArray() {
    return StringUtils.isBlank(anonymous) ? ArrayUtils.EMPTY_STRING_ARRAY : anonymous.split(",");
  }

  public String[] getIgnoringArray() {
    return StringUtils.isBlank(ignoring) ? ArrayUtils.EMPTY_STRING_ARRAY : ignoring.split(",");
  }

}
