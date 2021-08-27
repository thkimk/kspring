package com.kkk.sbgtest;

public class TestException extends Exception {

    private static final long serialVersionUID = 9177852020113338422L;

    private String errorCd;

    public TestException() {
        super();
    }

    public TestException(String errorCd) {
        this.errorCd = errorCd;
    }

    public TestException(String errorCd, Throwable throwable) {
        super(throwable);
        this.errorCd = errorCd;
    }

    public TestException(Throwable throwable) {
        super(throwable);
    }

    public String getErrorCd() {
        return errorCd;
    }

    public void setErrorCd(String errorCd) {
        this.errorCd = errorCd;
    }

    public String getMsgId() {
        return "getMsgId";
    }

}
