package com.zhaoguhong.baymax.security.hander;

import com.zhaoguhong.baymax.common.ResponseResult;
import com.zhaoguhong.baymax.util.RequestUtil;
import com.zhaoguhong.baymax.util.ResponseUtil;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

/**
 * @author guhong
 */
@Component
public class MyLogoutSuccessHandler implements LogoutSuccessHandler {

  @Override
  public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) throws IOException, ServletException {
    // ajax登出
    if (RequestUtil.isAjaxRequest(request)) {
      ResponseUtil.printJson(response, ResponseResult.success());
    } else {
      response.sendRedirect("/");
    }
  }
}
