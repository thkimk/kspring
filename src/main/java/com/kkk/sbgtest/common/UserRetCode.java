package com.kkk.sbgtest.common;

public enum UserRetCode {
    SUCCESS(0, "api.result.msg.0000"),
    ERROR_0400(100, "api.result.msg.0400"),
    ;

    private int code;
    private String msg;

    private UserRetCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return this.code;
    }

    public String getMsg() {
        return this.msg;
    }

}
