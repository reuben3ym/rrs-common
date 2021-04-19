package com.rrs.common.security.component.authorization.exception;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.springframework.http.HttpStatus;

/**
 * @author zjm
 */
@JsonSerialize(using = RrsAuth2ExceptionSerializer.class)
public class ForbiddenException extends RrsAuth2Exception {
    private static final long serialVersionUID = 1L;

    public ForbiddenException(String msg, Throwable t) {
        super(msg);
    }

    @Override
    public String getOAuth2ErrorCode() {
        return "access_denied";
    }

    @Override
    public int getHttpErrorCode() {
        return HttpStatus.FORBIDDEN.value();
    }

}
