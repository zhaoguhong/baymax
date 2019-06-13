package com.zhaoguhong.baymax.util;

import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import java.io.IOException;
import java.io.StringWriter;

/**
 * FreeMarker 工具类
 * @author guhong
 */
public class FreeMarkerUtil {

  private static Configuration cfg = new Configuration(Configuration.VERSION_2_3_26);

  public static String generate(String templateName, Object dataModel) {
    StringWriter out = null;
    try {
      Template template = cfg.getTemplate(templateName, "utf-8");
      out = new StringWriter(2048);
      template.process(dataModel, out);
    } catch (TemplateException e) {
      e.getBlamedExpressionString();
      throw new RuntimeException("变量不存在 " + e.getBlamedExpressionString(), e);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return out != null ? out.toString() : null;
  }


  static {
    cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
    ClassTemplateLoader ctl =
        new ClassTemplateLoader(FreeMarkerUtil.class, "/templates");
    cfg.setTemplateLoader(ctl);
  }
}
