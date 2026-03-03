package com.nelumbo.open_flow_coworking.exception;

import com.nelumbo.open_flow_coworking.exception.ApiExceptionError.ApiExceptionError;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

@Getter
public class OpenFlowException extends RuntimeException {

  private static final Logger log = LoggerFactory.getLogger(OpenFlowException.class);

  private final HttpStatus httpStatus;
  private final int code;

  public OpenFlowException(int code) {
    super(ApiExceptionError.getMessage(code));
    this.code = code;
    this.httpStatus = HttpStatus.valueOf(ApiExceptionError.getHttpStatusName(code));
    logError(getMessage());
  }

  public OpenFlowException(int code, Object... messageArgs) {
    super(ApiExceptionError.formatMessage(code, messageArgs));
    this.code = code;
    this.httpStatus = HttpStatus.valueOf(ApiExceptionError.getHttpStatusName(code));
    logError(getMessage());
  }

  private void logError(String detail) {
    log.error("[ERROR] {}", detail);
  }
}
