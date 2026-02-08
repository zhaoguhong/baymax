package com.zhaoguhong.baymax.exception.handler;


import com.zhaoguhong.baymax.common.ResponseResult;
import com.zhaoguhong.baymax.exception.BusinessException;
import com.zhaoguhong.baymax.exception.NoneLoginException;
import java.util.List;
import java.util.StringJoiner;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理
 */
@Slf4j
@RestControllerAdvice
public class GlobalControllerExceptionHandler {

  /**
   * 业务异常
   */
  @ExceptionHandler(value = {BusinessException.class})
  public ResponseResult<?> handleBusinessException(BusinessException ex) {
    String msg = ex.getMessage();
    if (StringUtils.isBlank(msg)) {
      msg = "操作失败";
    }
    return ResponseResult.error(msg);
  }

  /**
   * 处理未登录异常
   */
  @ExceptionHandler(value = {NoneLoginException.class})
  public ResponseResult<?> handleNoneLoginException(NoneLoginException ex) {
    return ResponseResult.unLogin();
  }

  /**
   * 处理校验异常
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseResult<?> handleValidationException(MethodArgumentNotValidException ex) {
    BindingResult result = ex.getBindingResult();
    if (result.hasErrors()) {
      StringJoiner joiner = new StringJoiner("，");
      List<ObjectError> errors = result.getAllErrors();
      errors.forEach(error -> {
        FieldError fieldError = (FieldError) error;
        joiner.add(fieldError.getField() + " " + error.getDefaultMessage());
      });
      return ResponseResult.error(joiner.toString());
    } else {
      return ResponseResult.error("操作失败");
    }
  }

  /**
   * 处理未知的错误
   */
  @ExceptionHandler(value = {Exception.class})
  public ResponseResult<?> handleunknownException(Exception ex) {
    log.error("服务器异常", ex);
    return ResponseResult.unknownError("服务器异常");
  }

}
