package com.sdw.soft.cocoim.remoting.command;

/**
 * @author: shangyd
 * @create: 2019-11-13 17:28:07
 **/
public enum RemotingCommandType {

    BROKER_REGISTER(1),
    BROKER_UNREGISTER(2),
    RESPONSE(3),
    ;

    public byte cmd;

    RemotingCommandType(int cmd) {
        this.cmd = (byte) cmd;
    }

    public byte getCmd() {
        return cmd;
    }
}
