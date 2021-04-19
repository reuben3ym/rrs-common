package com.rrs.common.security.component.authorization.exception;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;

/**
 * @author zjm
 */
@JsonSerialize(using = RrsAuth2ExceptionSerializer.class)
public class RrsAuth2Exception extends OAuth2Exception {
    private static final long serialVersionUID = 1L;
    @Getter
    private String errorCode;

    public RrsAuth2Exception(String msg) {
        super(msg);
    }

    public RrsAuth2Exception(String msg, String errorCode) {
        super(msg);
        this.errorCode = errorCode;
    }
}
