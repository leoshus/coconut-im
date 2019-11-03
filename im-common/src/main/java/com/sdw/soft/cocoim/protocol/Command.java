package com.sdw.soft.cocoim.protocol;

public enum Command {
    HEARTBEAT(1),
    LOGIN_REQUEST(2),
    LOGIN_RESPONSE(3),
    CHAT_REQUEST(4),
    CHAT_RESPONSE(5),
    UNKNOWN(-1),
    ;
    private byte code;

    Command(int code) {
        this.code = (byte)code;
    }

    public byte getCode() {
        return code;
    }

    Command toCMD(byte code) {
        Command[] values = Command.values();
        if (code > 0 && code < values.length) {
            return values[code - 1];
        }
        return UNKNOWN;
    }
}
