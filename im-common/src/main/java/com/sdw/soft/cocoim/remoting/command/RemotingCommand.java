package com.sdw.soft.cocoim.remoting.command;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author: shangyd
 * @create: 2019-11-13 17:25:45
 **/
public class RemotingCommand {

    private static AtomicInteger idGenerator = new AtomicInteger(0);
    private CustomHeader customHeader;
    private RemotingCommandType type;
    private byte version = 1;
    private int opaque = idGenerator.getAndIncrement();

    private Map<String, Object> properties = new HashMap<>();

    public RemotingCommand(RemotingCommandType type, Map<String, Object> properties) {
        this.type = type;
        this.properties = properties;
    }

    public CustomHeader getCustomHeader() {
        return customHeader;
    }

    public void setCustomHeader(CustomHeader customHeader) {
        this.customHeader = customHeader;
    }

    public RemotingCommandType getType() {
        return type;
    }

    public void setType(RemotingCommandType type) {
        this.type = type;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }

    public byte getVersion() {
        return version;
    }

    public void setVersion(byte version) {
        this.version = version;
    }

    public int getOpaque() {
        return opaque;
    }

    public void setOpaque(int opaque) {
        this.opaque = opaque;
    }
}
