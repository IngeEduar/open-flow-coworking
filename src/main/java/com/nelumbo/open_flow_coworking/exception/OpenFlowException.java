package com.nelumbo.open_flow_coworking.exception;

import com.nexxar.reserfy.Exception.Errors.CommonErrors;
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
    super(CommonErrors.getMessage(code));
    this.code = code;
    this.httpStatus = HttpStatus.valueOf(CommonErrors.getHttpStatusName(code));
    logError(CommonErrors.formatLog(code));
  }

  public OpenFlowException(int code, Object... messageArgs) {
    super(CommonErrors.formatMessage(code, messageArgs));
    this.code = code;
    this.httpStatus = HttpStatus.valueOf(CommonErrors.getHttpStatusName(code));
    logError(CommonErrors.formatLog(code, messageArgs));
  }

  private void logError(String detail) {
      log.error("[ERROR] {}", detail);
  }
}
