package com.sdw.soft.cocoim.protocol;

/**
 * @author shangyd
 * @version 1.0.0
 * @date 2019-11-03 15:25
 * @description
 **/
public class LoginResponsePacket extends BaseResponsePacket {
    @Override
    public byte command() {
        return Command.LOGIN_RESPONSE.getCmd();
    }

}
