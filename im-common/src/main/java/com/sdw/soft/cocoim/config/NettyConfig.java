package com.sdw.soft.cocoim.config;

import lombok.Data;

/**
 * @author: shangyd
 * @create: 2019-11-06 15:25:50
 **/
@Data
public class NettyConfig {

    private int bossThreadNum = 1;
    private int workerThreadNum = Runtime.getRuntime().availableProcessors();
    private int tcpBackLog = 1024;
    private boolean tcpNoDelay = false;
    private boolean tcpReuseAddr = true;
    private boolean tcpKeepAlive = false;
    private int tcpSndBuf = 64 * 1024;
    private int tcpRcvBuf = 64 * 1024;
    private boolean useEpoll = false;
    private boolean pooledByteBufAllocatorEnable = false;
    private boolean security;

    private String host = "127.0.0.1";
    private boolean tcpServerEnable = true;
    private int tcpPort = 8080;
    private boolean websocketServerEnable = true;
    private int websocketPort = 8090;

    private boolean tcpSslServerEnable = false;
    private int tcpSslPort = 8083;

    private boolean tcpSslWebsocketServerEnable = false;
    private int websocketSslPort = 8093;

    /**
     * ssl settings
     */
    private boolean useClientCA = false;
    private String sslKeyStoreType = "PKCS12";
    private String sslKeyFilePath = "/conf/server.pfx";
    private String sslManagerPwd = "123456";
    private String sslStorePwd = "123456";

}
