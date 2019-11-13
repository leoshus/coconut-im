package com.sdw.soft.cocoim.remoting.exception;

import com.sdw.soft.cocoim.exception.ErrorCode;
import lombok.Data;

/**
 * @author: shangyd
 * @create: 2019-11-13 17:44:47
 **/
@Data
public class RemotingNameServerException extends RuntimeException {

    private int code;

    public RemotingNameServerException(String message) {
        super(message);
    }

    public RemotingNameServerException(ErrorCode errorCode) {
        super(errorCode.msg);
        this.code = errorCode.code;
    }

}
