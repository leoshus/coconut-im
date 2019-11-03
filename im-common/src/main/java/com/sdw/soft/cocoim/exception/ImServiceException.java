package com.sdw.soft.cocoim.exception;

import lombok.Getter;
import lombok.Setter;

/**
 * @author shangyd
 * @version 1.0.0
 * @date 2019-11-03 22:39
 * @description
 **/
@Getter
@Setter
public class ImServiceException extends RuntimeException {

    private int code;

    public ImServiceException(ErrorCode errorCode) {
        super(errorCode.msg);
        this.code = errorCode.code;
    }

    public ImServiceException(ErrorCode errorCode, Throwable cause) {
        super(errorCode.msg, cause);
        this.code = errorCode.code;
    }
}
