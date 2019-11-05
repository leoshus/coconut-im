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
    private String userId;
    public int heartbeat = 10000;//10s
    private byte clientType;


}
