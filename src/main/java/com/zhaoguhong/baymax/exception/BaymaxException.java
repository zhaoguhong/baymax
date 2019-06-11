package com.zhaoguhong.baymax.exception;

/**
 * 项目所有自定义异常父类
 */
public class BaymaxException extends RuntimeException {

  public BaymaxException() {
  }

  public BaymaxException(String message) {
    super(message);
  }

  public BaymaxException(String message, Throwable cause) {
    super(message, cause);
  }

  public BaymaxException(Throwable cause) {
    super(cause);
  }

}
