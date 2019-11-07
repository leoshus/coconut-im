package com.sdw.soft.cocoim.protocol;

public enum Command {
    HEARTBEAT(1),
    LOGIN_REQUEST(2),
    LOGIN_RESPONSE(3),
    CHAT_REQUEST(4),
    CHAT_RESPONSE(5),
    REGISTER_SERVICE_REQUEST(6),
    UNREGISTER_SERVICE(7),
    UNKNOWN(-1),
    ;
    public byte cmd;

    Command(int cmd) {
        this.cmd = (byte) cmd;
    }

    public byte getCmd() {
        return cmd;
    }

    Command toCMD(byte code) {
        Command[] values = Command.values();
        if (code > 0 && code < values.length) {
            return values[code - 1];
        }
        return UNKNOWN;
    }
}
