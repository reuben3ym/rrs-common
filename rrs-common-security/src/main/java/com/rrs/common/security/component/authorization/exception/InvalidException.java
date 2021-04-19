package com.rrs.common.security.component.authorization.exception;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * @author zjm
 */
@JsonSerialize(using = RrsAuth2ExceptionSerializer.class)
public class InvalidException extends RrsAuth2Exception {
    private static final long serialVersionUID = 1L;

    public InvalidException(String msg, Throwable t) {
        super(msg);
    }

    @Override
    public String getOAuth2ErrorCode() {
        return "invalid_exception";
    }

    @Override
    public int getHttpErrorCode() {
        return 426;
    }

}
