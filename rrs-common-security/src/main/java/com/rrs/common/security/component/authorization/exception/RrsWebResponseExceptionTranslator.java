package com.rrs.common.security.component.authorization.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.common.DefaultThrowableAnalyzer;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.ClientAuthenticationException;
import org.springframework.security.oauth2.common.exceptions.InsufficientScopeException;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.security.web.util.ThrowableAnalyzer;
import org.springframework.web.HttpRequestMethodNotSupportedException;

/**
 * @author zjm
 */
public class RrsWebResponseExceptionTranslator implements WebResponseExceptionTranslator<OAuth2Exception> {

    private ThrowableAnalyzer throwableAnalyzer = new DefaultThrowableAnalyzer();

    private final static String BAD_CREDENTIALS = "Bad credentials";
    private final static String USER_IS_DISABLED = "User is disabled";

    @Override
    public ResponseEntity<OAuth2Exception> translate(Exception e) {
        Throwable[] causeChain = throwableAnalyzer.determineCauseChain(e);
        Exception ase =
                (AuthenticationException) throwableAnalyzer.getFirstThrowableOfType(AuthenticationException.class,
                        causeChain);
        // 身份验证相关异常
        if (ase != null) {
            return handleOAuth2Exception(new UnauthorizedException(e.getMessage(), e));
        }
        ase = (AccessDeniedException) throwableAnalyzer.getFirstThrowableOfType(AccessDeniedException.class,
                causeChain);
        // 拒绝访问异常
        if (ase != null) {
            return handleOAuth2Exception(new ForbiddenException(ase.getMessage(), ase));
        }
        ase = (InvalidGrantException) throwableAnalyzer.getFirstThrowableOfType(InvalidGrantException.class,
                causeChain);
        if (ase != null) {
            if (ase.getMessage() != null) {
                if (BAD_CREDENTIALS.equals(ase.getMessage())) {
                    String message = "用户名或密码错误";
                    HttpHeaders headers = new HttpHeaders();
                    headers.set(HttpHeaders.CACHE_CONTROL, "no-store");
                    headers.set(HttpHeaders.PRAGMA, "no-cache");
                    return new ResponseEntity<>(new RrsAuth2Exception(message,
                            ((InvalidGrantException) ase).getOAuth2ErrorCode()), headers, HttpStatus.OK);
                } else if (USER_IS_DISABLED.equals(ase.getMessage())) {
                    String message = "用户已锁定";
                    HttpHeaders headers = new HttpHeaders();
                    headers.set(HttpHeaders.CACHE_CONTROL, "no-store");
                    headers.set(HttpHeaders.PRAGMA, "no-cache");
                    return new ResponseEntity<>(new RrsAuth2Exception(message,
                            ((InvalidGrantException) ase).getOAuth2ErrorCode()), headers, HttpStatus.OK);
                }

            }
            return handleOAuth2Exception(new InvalidException(ase.getMessage(), ase));
        }
        ase = (HttpRequestMethodNotSupportedException) throwableAnalyzer.getFirstThrowableOfType(HttpRequestMethodNotSupportedException.class, causeChain);
        // Http方法请求异常
        if (ase != null) {
            return handleOAuth2Exception(new MethodNotAllowedException(ase.getMessage(), ase));
        }
        ase = (OAuth2Exception) throwableAnalyzer.getFirstThrowableOfType(OAuth2Exception.class, causeChain);
        // OAuth2Exception异常
        if (ase != null) {
            return handleOAuth2Exception((OAuth2Exception) ase);
        }
        return handleOAuth2Exception(new ServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), e));
    }

    private ResponseEntity<OAuth2Exception> handleOAuth2Exception(OAuth2Exception e) {
        int status = e.getHttpErrorCode();
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CACHE_CONTROL, "no-store");
        headers.set(HttpHeaders.PRAGMA, "no-cache");
        if (status == HttpStatus.UNAUTHORIZED.value() || (e instanceof InsufficientScopeException)) {
            headers.set(HttpHeaders.WWW_AUTHENTICATE, String.format("%s %s", OAuth2AccessToken.BEARER_TYPE,
                    e.getSummary()));
        }

        // 客户端异常直接返回
        if (e instanceof ClientAuthenticationException) {
            return new ResponseEntity<>(e, headers, HttpStatus.valueOf(status));
        }
        return new ResponseEntity<>(new RrsAuth2Exception(e.getMessage(), e.getOAuth2ErrorCode()), headers,
                HttpStatus.valueOf(status));

    }
}
