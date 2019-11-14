package com.sdw.soft.cocoim.remoting.handler;

import com.alibaba.fastjson.JSONObject;
import com.sdw.soft.cocoim.connection.Connection;
import com.sdw.soft.cocoim.remoting.command.RemotingCommand;
import com.sdw.soft.cocoim.remoting.command.RemotingCommandType;
import com.sdw.soft.cocoim.remoting.route.Broker;
import com.sdw.soft.cocoim.remoting.route.RouteManager;
import com.sdw.soft.cocoim.utils.SystemHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author: shangyd
 * @create: 2019-11-13 18:17:46
 **/
public class RegisterRemotingProcessor implements RemotingMessageProcessor {

    private static final Logger logger = LoggerFactory.getLogger(RegisterRemotingProcessor.class);

    private RouteManager routeManager;

    public RegisterRemotingProcessor(RouteManager routeManager) {
        this.routeManager = routeManager;
    }

    @Override
    public void handleMessage(Connection connection, RemotingCommand command) {
        logger.info("Register request:{}", JSONObject.toJSON(command));
        Broker broker = new Broker((String) command.getProperties().get("type"), (String) command.getProperties().get("host"), Integer.valueOf(command.getProperties().get("port").toString()), SystemHelper.now());

        routeManager.registerBroker(broker);
        RemotingCommand cmd = new RemotingCommand(RemotingCommandType.RESPONSE, null);
        cmd.setOpaque(command.getOpaque());
        connection.channel().writeAndFlush(cmd);
    }
}
