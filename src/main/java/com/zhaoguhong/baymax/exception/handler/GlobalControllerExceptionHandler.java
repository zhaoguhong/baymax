package com.zhaoguhong.baymax.exception.handler;


import com.zhaoguhong.baymax.common.ResponseResult;
import com.zhaoguhong.baymax.exception.BusinessException;
import com.zhaoguhong.baymax.exception.NoneLoginException;
import com.zhaoguhong.baymax.exception.dao.ExceptionLogMapper;
import com.zhaoguhong.baymax.exception.entity.ExceptionLog;
import java.util.Date;
import java.util.List;
import java.util.StringJoiner;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理
 */
@RestControllerAdvice
public class GlobalControllerExceptionHandler {

  @Autowired
  private ExceptionLogMapper exceptionLogMapper;

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
  public ResponseResult<Long> handleunknownException(Exception ex) {
    ExceptionLog log = new ExceptionLog(new Date(), ExceptionUtils.getStackTrace(ex));
    exceptionLogMapper.insert(log);
    ResponseResult<Long> result = ResponseResult.unknownError("服务器异常:" + log.getId());
    result.setData(log.getId());
    return result;
  }

}
