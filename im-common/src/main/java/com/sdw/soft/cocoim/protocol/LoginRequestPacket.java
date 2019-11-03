package com.sdw.soft.cocoim.protocol;

import lombok.Data;

/**
 * @author shangyd
 * @version 1.0.0
 * @date 2019-11-03 15:10
 * @description
 **/
@Data
public class LoginRequestPacket extends Packet {

    private Long userId;
    private String userName;

    @Override
    public byte command() {
        return Command.LOGIN_REQUEST.getCode();
    }
}
