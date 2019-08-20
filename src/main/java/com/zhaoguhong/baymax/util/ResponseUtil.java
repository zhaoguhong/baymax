package com.zhaoguhong.baymax.util;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * @author guhong
 */
@Slf4j
public class ResponseUtil {

  public static void printJson(HttpServletResponse response, Object data) {
    response.setContentType("application/json;charset=utf-8");
    try {
      PrintWriter out = response.getWriter();
      out.print(JSONUtil.toJsonString(data));
      out.flush();
      out.close();
    } catch (IOException ex) {
      log.error("响应异常", ex);
    }
  }

}
