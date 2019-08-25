package com.zhaoguhong.baymax.security.hander;

import com.zhaoguhong.baymax.common.ResponseResult;
import com.zhaoguhong.baymax.util.RequestUtil;
import com.zhaoguhong.baymax.util.ResponseUtil;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

/**
 * 自定义认证失败处理器
 * @author guhong
 * @date 2019/8/25
 */
public class MyLoginUrlAuthenticationEntryPoint extends LoginUrlAuthenticationEntryPoint {

  /**
   * @param loginFormUrl URL where the login page can be found. Should either be relative to the
   * web-app context path (include a leading {@code /}) or an absolute URL.
   */
  public MyLoginUrlAuthenticationEntryPoint(String loginFormUrl) {
    super(loginFormUrl);
  }

  public void commence(HttpServletRequest request, HttpServletResponse response,
      AuthenticationException authException) throws IOException, ServletException {
    // ajax
    if (RequestUtil.isAjaxRequest(request)) {
      ResponseUtil.printJson(response, ResponseResult.unLogin());
    } else {
      super.commence(request, response, authException);
    }
  }
}
