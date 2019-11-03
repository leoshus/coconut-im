package com.sdw.soft.cocoim.protocol;

/**
 * @author shangyd
 * @version 1.0.0
 * @date 2019-11-02 23:43
 * @description
 **/
public class HeartBeatPacket extends Packet {



    @Override
    public byte command() {
        return Command.HEARTBEAT.getCode();
    }

}
