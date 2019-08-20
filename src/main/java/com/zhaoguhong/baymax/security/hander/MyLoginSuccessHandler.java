package com.zhaoguhong.baymax.security.hander;

import com.zhaoguhong.baymax.common.ResponseResult;
import com.zhaoguhong.baymax.util.RequestUtil;
import com.zhaoguhong.baymax.util.ResponseUtil;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Service;

/**
 * @author guhong
 * @date 2019/8/19
 */
@Service
public class MyLoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
  /**
   * 登录成功处理器
   */
  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) throws ServletException, IOException {
    // ajax
    if (RequestUtil.isAjaxRequest(request)) {
      ResponseUtil.printJson(response,  ResponseResult.success());
    } else {
      response.sendRedirect("/");
    }
  }

}
