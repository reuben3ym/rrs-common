package com.rrs.common.security.component.authorization.exception;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.springframework.http.HttpStatus;

/**
 * @author zjm
 */
@JsonSerialize(using = RrsAuth2ExceptionSerializer.class)
public class ServerErrorException extends RrsAuth2Exception {
    private static final long serialVersionUID = 1L;

    public ServerErrorException(String msg, Throwable t) {
        super(msg);
    }

    @Override
    public String getOAuth2ErrorCode() {
        return "server_error";
    }

    @Override
    public int getHttpErrorCode() {
        return HttpStatus.INTERNAL_SERVER_ERROR.value();
    }

}
