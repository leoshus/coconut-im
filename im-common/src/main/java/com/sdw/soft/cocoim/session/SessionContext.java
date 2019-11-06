package com.sdw.soft.cocoim.session;

import lombok.Data;

/**
 * @author: shangyd
 * @create: 2019-11-05 19:50:28
 **/
@Data
public final class SessionContext {

    private String osName;
    private String osVersion;
    private String clientVersion;
    private String deviceId;
    private Long userId;
    private String userName;
    public int heartbeat = 10000;//10s
    private byte clientType;


    public SessionContext setOsName(String osName) {
        this.osName = osName;
        return this;
    }

    public SessionContext setOsVersion(String osVersion) {
        this.osVersion = osVersion;
        return this;
    }

    public SessionContext setClientVersion(String clientVersion) {
        this.clientVersion = clientVersion;
        return this;
    }

    public SessionContext setDeviceId(String deviceId) {
        this.deviceId = deviceId;
        return this;
    }

    public SessionContext setUserId(Long userId) {
        this.userId = userId;
        return this;
    }

    public SessionContext setUserName(String userName) {
        this.userName = userName;
        return this;
    }

    public SessionContext setHeartbeat(int heartbeat) {
        this.heartbeat = heartbeat;
        return this;
    }

    public SessionContext setClientType(byte clientType) {
        this.clientType = clientType;
        return this;
    }
}
