package com.sdw.soft.cocoim.protocol.packet;

import com.sdw.soft.cocoim.protocol.Command;
import com.sdw.soft.cocoim.protocol.Packet;
import lombok.Data;

/**
 * @author: shangyd
 * @create: 2019-11-07 19:14:10
 **/
@Data
public class RegisterServicePacket extends Packet {

    private String clusterName;
    private String host;
    private int port;

    @Override
    public byte command() {
        return Command.REGISTER_SERVICE_REQUEST.cmd;
    }
}
