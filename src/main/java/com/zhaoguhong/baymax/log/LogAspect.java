package com.zhaoguhong.baymax.log;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Created by guhong on 2019/6/14.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface LogAspect {

  /**
   * 日志描述
   */
  String value() default "";

  /**
   * 日志级别
   */
  String level() default "INFO";

}
