package com.zhaoguhong.baymax.exception;

/**
 * 业务异常
 */
public class BusinessException  extends BaymaxException {

	public BusinessException() {
	}

	public BusinessException(String message) {
		super(message);
	}

	public BusinessException(String message, Throwable cause) {
		super(message, cause);
	}

	public BusinessException(Throwable cause) {
		super(cause);
	}

}
