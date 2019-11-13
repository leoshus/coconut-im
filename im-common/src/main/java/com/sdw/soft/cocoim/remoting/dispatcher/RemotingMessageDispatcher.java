package com.sdw.soft.cocoim.remoting.dispatcher;

import com.sdw.soft.cocoim.connection.Connection;
import com.sdw.soft.cocoim.remoting.command.RemotingCommand;
import com.sdw.soft.cocoim.remoting.command.RemotingCommandType;
import com.sdw.soft.cocoim.remoting.handler.RemotingMessageProcessor;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: shangyd
 * @create: 2019-11-13 17:56:35
 **/
public class RemotingMessageDispatcher implements MessageReceiver{

    private static final Map<RemotingCommandType, RemotingMessageProcessor> handlers = new HashMap<>();

    @Override
    public void onReceive(Connection connection, RemotingCommand command) {

        RemotingMessageProcessor processor = handlers.get(command.getType());
        if (processor != null) {
            processor.handleMessage(connection, command);
        }
    }


    public void register(RemotingCommandType type, RemotingMessageProcessor processor) {
        handlers.putIfAbsent(type, processor);
    }

}
