package com.rrs.common.security.component.authorization.exception;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.springframework.http.HttpStatus;

/**
 * @author zjm
 */
@JsonSerialize(using = RrsAuth2ExceptionSerializer.class)
public class MethodNotAllowedException extends RrsAuth2Exception {
    private static final long serialVersionUID = 1L;

    public MethodNotAllowedException(String msg, Throwable t) {
        super(msg);
    }

    @Override
    public String getOAuth2ErrorCode() {
        return "method_not_allowed";
    }

    @Override
    public int getHttpErrorCode() {
        return HttpStatus.METHOD_NOT_ALLOWED.value();
    }

}
