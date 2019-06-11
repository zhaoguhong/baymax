package com.zhaoguhong.baymax.common;


import lombok.Data;

/**
 * 用于 ajax 请求的响应工具类
 */
@Data
public class ResponseResult<T> {
  // 未登录
  public static final String UN_LOGIN_CODE = "401";
  // 操作失败
  public static final String ERROR_CODE = "400";
  // 服务器内部执行错误
  public static final String UNKNOWN_ERROR_CODE = "500";
  // 操作成功
  public static final String SUCCESS_CODE = "200";
  // 响应信息
  private String msg;
  // 响应code
  private String code;
  // 操作成功，响应数据
  private T data;

  public ResponseResult(String code, String msg, T data) {
    this.msg = msg;
    this.code = code;
    this.data = data;
  }

  /**
   * 创建一个成功的响应对象
   *
   * @return
   */
  public static ResponseResult<String> success() {
    return success(null);
  }

  /**
   * 创建一个成功的响应对象
   *
   * @param data
   * @return
   */
  public static <T> ResponseResult<T> success(T data) {
    return new ResponseResult<T>(SUCCESS_CODE, "请求成功", data);
  }

  /**
   * 创建一个失败的响应对象
   *
   * @param msg
   * @return
   */
  public static <T> ResponseResult<T> error(String msg) {
    return new ResponseResult<T>(ERROR_CODE, msg, null);
  }

  /**
   * 创建一个未知的失败的响应对象，一般是服务器内容错误
   *
   * @param msg
   * @return
   */
  public static <T> ResponseResult<T> unknownError(String msg) {
    return new ResponseResult<T>(UNKNOWN_ERROR_CODE, msg, null);
  }

  /**
   * 创建一个未登录响应对象
   *
   * @return
   */
  public static <T> ResponseResult<T> unLogin() {
    return new ResponseResult<T>(UN_LOGIN_CODE, "请先登录", null);
  }

}
