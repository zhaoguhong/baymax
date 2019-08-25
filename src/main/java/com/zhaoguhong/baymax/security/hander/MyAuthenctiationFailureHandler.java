package com.zhaoguhong.baymax.security.hander;

import com.zhaoguhong.baymax.common.ResponseResult;
import com.zhaoguhong.baymax.util.RequestUtil;
import com.zhaoguhong.baymax.util.ResponseUtil;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

/**
 * @author guhong
 * @date 2019/8/25
 */
public class MyAuthenctiationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

  public MyAuthenctiationFailureHandler(String loginFormUrl) {
    super(loginFormUrl);
  }

  @Override
  public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
      AuthenticationException exception) throws IOException, ServletException {
    if (RequestUtil.isAjaxRequest(request)) {
      ResponseUtil.printJson(response, ResponseResult.error("用户名和密码不匹配，请重新登录"));
    } else {
      super.onAuthenticationFailure(request, response, exception);
    }
  }

}
