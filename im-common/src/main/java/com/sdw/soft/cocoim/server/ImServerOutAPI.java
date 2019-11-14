package com.sdw.soft.cocoim.server;

import com.alibaba.fastjson.JSONObject;
import com.sdw.soft.cocoim.remoting.NettyRemotingClient;
import com.sdw.soft.cocoim.remoting.RemotingClient;
import com.sdw.soft.cocoim.remoting.command.RemotingCommand;
import com.sdw.soft.cocoim.remoting.command.RemotingCommandType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: shangyd
 * @create: 2019-11-14 14:45:47
 **/
public class ImServerOutAPI {

    private static final Logger logger = LoggerFactory.getLogger(ImServerOutAPI.class);
    private RemotingClient client;

    private String nameServiceAddr = "127.0.0.1:10123";
    public ImServerOutAPI() {
        this.client = new NettyRemotingClient();
        this.client.init();
        this.client.start();
    }

    public void registerNameServer(String clusterName, String brokerAddr, int port, long timeoutMills) {
        Map<String, Object> properties = new HashMap<>();
        properties.put("type", clusterName);
        properties.put("host", brokerAddr);
        properties.put("port", port);
        RemotingCommand req = new RemotingCommand(RemotingCommandType.BROKER_REGISTER,properties);
        RemotingCommand response = client.invokeSync(req, nameServiceAddr, timeoutMills);
        logger.info("register response:{}", JSONObject.toJSON(response));
    }

}
