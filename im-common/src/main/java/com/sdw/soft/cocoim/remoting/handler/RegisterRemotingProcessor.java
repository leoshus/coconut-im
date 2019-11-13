package com.sdw.soft.cocoim.remoting.handler;

import com.sdw.soft.cocoim.connection.Connection;
import com.sdw.soft.cocoim.remoting.command.RemotingCommand;
import com.sdw.soft.cocoim.remoting.command.RemotingCommandType;
import com.sdw.soft.cocoim.remoting.route.Broker;
import com.sdw.soft.cocoim.remoting.route.RouteManager;
import com.sdw.soft.cocoim.utils.SystemHelper;

/**
 * @author: shangyd
 * @create: 2019-11-13 18:17:46
 **/
public class RegisterRemotingProcessor implements RemotingMessageProcessor {

    private RouteManager routeManager;

    public RegisterRemotingProcessor(RouteManager routeManager) {
        this.routeManager = routeManager;
    }

    @Override
    public void handleMessage(Connection connection, RemotingCommand command) {
        Broker broker = new Broker((String) command.getProperties().get("host"), Integer.valueOf(command.getProperties().get("port").toString()), SystemHelper.now());
        routeManager.addBroker(broker);
        RemotingCommand cmd = new RemotingCommand(RemotingCommandType.RESPONSE, null);
        cmd.setOpaque(command.getOpaque());
        connection.channel().writeAndFlush(cmd);
    }
}
