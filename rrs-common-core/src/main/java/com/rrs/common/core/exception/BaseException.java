package com.rrs.common.core.exception;

/**
 * @author zjm
 * @date 2020年3月24日
 */
public class BaseException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    protected int errCode = 500;

    protected String errMsg;

    public BaseException() {
    }

    public BaseException(Throwable e) {
        super(e);
    }

    public BaseException(String errMsg) {
        super(errMsg);
        this.errMsg = errMsg;
    }

    public BaseException(String errMsg, Throwable e) {
        super(errMsg, e);
        this.errMsg = errMsg;
    }

    public BaseException(int errCode,String errMsg, Throwable e) {
        super(errMsg, e);
        this.errCode = errCode;
        this.errMsg = errMsg;
    }

    public int getErrCode() {
        return errCode;
    }

    public void setErrCode(int errCode) {
        this.errCode = errCode;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }
}
