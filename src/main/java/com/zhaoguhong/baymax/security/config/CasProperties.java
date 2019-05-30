package com.zhaoguhong.baymax.security.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 单点配置
 */
@Component
@Data
@ConfigurationProperties(prefix = "cas")
public class CasProperties{

  /**
   * 单点开关
   */
  private boolean enable;

  /**
   * 服务端地址
   */
  private String serverUrl;

  /**
   * 客户端地址
   */
  private String clientUrl;

  /**
   * 登录地址
   */
  private String loginUrl;


  /**
   * 服务端登出地址
   */
  private String serverLogoutUrl;

  /**
   * 单点登录成功回调地址
   */
  private String clientCasUrl;

}
