package com.sdw.soft.cocoim.exception;

public enum ErrorCode {

    Internal_Error(500, "internal error"),
    STARTED_SERVER_INITIAL_FAIL(500001, "服务初始化错误"),
    ;
    public int code;
    public String msg;

    ErrorCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
