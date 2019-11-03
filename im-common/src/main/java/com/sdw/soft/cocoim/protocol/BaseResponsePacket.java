package com.sdw.soft.cocoim.protocol;

import lombok.Data;

/**
 * @author shangyd
 * @version 1.0.0
 * @date 2019-11-03 15:21
 * @description
 **/
@Data
public abstract class BaseResponsePacket extends Packet{

    private int code;
    private String msg;

}
