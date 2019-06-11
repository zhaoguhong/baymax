package com.zhaoguhong.baymax.exception;


/**
 * 用户未登录异常
 */
public class NoneLoginException extends BaymaxException {

  public NoneLoginException(String msg, Throwable t) {
    super(msg, t);
  }

  public NoneLoginException(String msg) {
    super(msg);
  }

}
